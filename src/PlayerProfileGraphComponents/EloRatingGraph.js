import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Area,
    AreaChart,
    Tooltip,
    BarChart,
    Bar,
    Legend,
    ReferenceLine,
    ComposedChart
} from 'recharts';
import ToggleDisplay from 'react-toggle-display';
import moment from "moment";
import {Box, Flex, Provider, Text} from "rebass";
import '../main/css/index.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
const ReactFC = require('react-fusioncharts');
const fusioncharts = require('fusioncharts');
const charts = require('fusioncharts/fusioncharts.charts');
const {PropTypes} = React;

export class EloRatingPerGame extends React.Component {
    state = {
        dataset: [],
        result: '',
        showOppEloRating: true,
        showEloRating: true,
        barGraphKeys: ["eloRating", "opponentEloRating"],
        opacity: 1,
        eloRatingButton: "Hide Elo Rating",
        oppEloRatingButton: "Hide Opp Elo Rating",
        oppDataSet: [],
        negativeSet: [],
        positiveSet: [],
        startDate: '',
        endDate: '',
        errorMessage: '',
        displayError: false,

        gameDisplayStats: {
            number: 0,
            player1Username: '',
            player2Username: '',
            score1: 0,
            score2: 0
        },
        showGraph: false,
        showGif:true
    };

    componentWillReceiveProps = (nextProps) => {

        const playerID = nextProps.playerID;
        if (this.props !== nextProps) {
            this.setState({
                playerID: playerID,
                startDate: nextProps.startDate,
                endDate: nextProps.endDate,
                showGif:true
            });

            let timeString = "";
            if (nextProps.startDate instanceof moment && nextProps.endDate
                instanceof moment) {
                timeString = "&beginningTime=" + moment(
                        nextProps.startDate).format("YYYYMMMDD") +
                    "&endingTime=" + moment(nextProps.endDate).format(
                        "YYYYMMMDD");
            }
            jQuery.ajax({

                url: "http://localhost:8080/GetEloRatings?id=" + playerID
                + timeString,
                type: "GET",
                dataType: "json",
                async: true,
                success: function(data) {
                    if (data.success === false) {
                        this.handleFailure(data.message);
                        this.setState({
                            showGraph:false
                        });
                    } else {
                        let oppDataSet = [];
                        let dataSet = ParseApiMessage(data);
                        for (let i = 0; i < dataSet.length; i++) {
                            let newData = dataSet[i];
                            newData.opponentEloRating = newData.opponentEloRating
                                * -1;
                            oppDataSet.push(newData);
                        }
                        this.setState({
                            dataset: ParseApiMessage(data),
                            result: data.success,
                            oppDataSet: oppDataSet,
                            negativeSet: oppDataSet,
                            positiveSet: JSON.parse(data.message),
                            displayError: false,
                            errorMessage: '',
                            showGraph: true,
                            showGif:true
                        });
                    }
                }.bind(this)
            });
        }
    };

    handleFailure = (message) => {
        this.setState({
            errorMessage: message,
            displayError: true,
            dataset: [],
            oppDataSet: [],
            negativeSet: [],
            positiveSet: [],
            showGif:false
        });
    };

    returnYLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="middle" fontSize={'16pt'}
                  className="XAxisLabel">Rating</text>
        )
    };

    returnXLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="middle" fontSize={'16pt'}
                  className="XAxisLabel">Game</text>
        )
    };

    toggleEloRating = () => {
        let toggle = !this.state.showEloRating;
        let text = "";
        if (toggle === true) {
            text = "Hide Elo Rating";
        } else {
            text = "Show Elo Rating";
        }
        this.setState({
            showEloRating: toggle,
            eloRatingButton: text
        });

    };

    toggleOppEloRating = () => {
        let toggle = !this.state.showOppEloRating;
        let text = "";
        if (toggle === true) {
            text = "Hide Opp Elo Rating";
        } else {
            text = "Show Opp Elo Rating";
        }
        this.setState({
            showOppEloRating: toggle,
            oppEloRatingButton: text
        });

    };

    renderEloRating = (state) => {
        if (state === true) {
            return (<Line dataKey="eloRating" fill='#4286f4'/>)
        }
    };

    renderOppEloRating = (state) => {
        if (state === true) {
            return (<Line dataKey="opponentEloRating" fill='#4d004d'
                          stroke='#4d004d'/>
            )
        }
    };

    setGameDisplayState = (number, player1Username, player2Username, score1,
        score2) => {
        this.setState({
            gameDisplayStats: {
                number: number,
                player1Username: player1Username,
                player2Username: player2Username,
                score1: score1,
                score2: score2
            }
        })
    };

    returnWidth = () => {
        return jQuery('#infoDisplay').width() * .72;
    };

    returnStartTooltip = () => {
        return jQuery('#infoDisplay').width() * .77;
    };

    render() {
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                    <span ><ToggleDisplay show={this.state.displayError}><text
                        className="errorMessageGraph">{this.state.errorMessage}</text></ToggleDisplay><text
                        className="graphHeaderText">Elo Rating Per Game</text></span>
                    <ToggleDisplay show={!this.state.showGraph}>
                        {
                            this.state.showGif ? <p id="loadingSpinner" style={{'text-align': 'center'}}>
                                <img src={require('../images/Spinner.gif')}
                                     width='45%' height='45%'/></p> :
                                <p id="loadingSpinner" style={{'text-align': 'center'}}>
                                    <img src={require('../images/paddle.png')}
                                         width='45%' height='45%'/></p>
                        }
                    </ToggleDisplay>

                    <ToggleDisplay show={this.state.showGraph}>
                        <LineChart width={this.returnWidth()} height={400}
                                   data={this.state.dataset}
                                   margins={{top: 5, right: 30, bottom: 5}}>
                            <XAxis allowDecimals={false} type="number"
                                   dataKey="label" domain={[0, 'auto']}
                                   label={this.returnXLabel(475, 375)}
                                   padding={{bottom: 50, right: 10}}
                                   labelStyle={{
                                       paddingTop: 20,
                                       color: '#32CD32'
                                   }}/>
                            <YAxis domain={['auto', 'auto']}
                                   label={this.returnYLabel(30, 150)}/>
                            <Tooltip
                                position={{x: this.returnStartTooltip(), y: 0}}
                                content={<CustomToolTipDisplayGameElo
                                    setGameDisplay={this.setGameDisplayState}/>}/>
                            <CartesianGrid strokeDasharray="3 3"/>
                            <ReferenceLine y={1500} className="referenceLine"
                                           stroke='#000'/>
                            {this.renderEloRating(this.state.showEloRating)}
                            {this.renderOppEloRating(
                                this.state.showOppEloRating)}
                            <Legend margins={{top: 15, right: 15, bottom: 5}}/>
                        </LineChart>
                    </ToggleDisplay>
                </div>
                <div className="averageScoreToggleButtons">
                    <button className="graphButton"
                            onClick={() => this.toggleEloRating()}>{this.state.eloRatingButton}</button>
                    <button className="graphButton"
                            onClick={() => this.toggleOppEloRating()}>{this.state.oppEloRatingButton}</button>
                </div>
            </div>
        )
    }
}

export const ParseApiMessage = (props) => {
    if (props.success === true) {
        return JSON.parse(props.message);
    } else {
        return props.message;
    }
};



export const CustomToolTipDisplayGameElo = React.createClass({
    propTypes: {
        type: PropTypes.string,
        payload: PropTypes.array,
        label: PropTypes.number,
    },


    render() {
        const {active} = this.props;

        if (active) {
            const {payload, label} = this.props;
            if (label > 0) {
                let game = payload[0].payload.game;

                return (
                    <div className="custom-tooltip-average">

                        <Provider
                            theme={{
                                font: '"Serif"',
                            }}
                        >
                            <Flex wrap mx={-2} align='center' width='auto'>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={32}><u>Game {label} </u></Text>
                                </Box>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={28}>Rating</Text>
                                </Box>


                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={26}>
                                        <em> <u> You </u>
                                        </em>
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={26}>
                                        <em> <u> Opponent </u></em>
                                    </Text>
                                </Box>
                                <Box px={2} py={0} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={26}>
                                        {payload[0].payload.eloRating}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={26}>
                                        {payload[0].payload.opponentEloRating}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        {moment(game.timeString).format(
                                            "YYYY-MM-DD hh:mm a")}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        <em> <u> You </u> </em>
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        <em> <u>  {game.player2.username} </u>
                                        </em>
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        {game.score1}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        {game.score2}
                                    </Text>
                                </Box>
                            </Flex>

                        </Provider>
                    </div>
                );
            }
        }
        return null;
    }
});