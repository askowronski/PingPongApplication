import {CustomToolTipDisplayGame} from "../Pages/PlayerProfilePage";
import ToggleDisplay from 'react-toggle-display';
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
import moment from "moment";
import {ParseApiMessage} from "./EloRatingGraph";
// import '../index.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../main/css/index.css");
const ReactFC = require('react-fusioncharts');
const fusioncharts = require('fusioncharts');
const charts = require('fusioncharts/fusioncharts.charts');
const {PropTypes} = React;

export class AverageScorePerGame extends React.Component {
    state = {
        dataset: [],
        result: '',
        showOppScore: true,
        barGraphKeys: ["opponentScore", "score"],
        opacity: 1,
        showScore: true,
        scoreButtonValue: "Hide Score",
        oppScoreButtonValue: "Hide Opp Score",
        showAverage: true,
        averageButtonValue: "Hide Average",
        showOppAverage: true,
        oppAverageButtonValue: "Hide Opp Average",
        gameDisplayStats: {
            number: 0,
            player1Username: '',
            player2Username: '',
            score1: 0,
            score2: 0
        },
        playerID: 0,
        startDate: '',
        endDate: '',
        errorMessage: '',
        displayError: false,
        showGraph:false,
        showGif:true
    };

    hideBarElement = () => {
        if (this.state.opacity === 0) {
            this.setState({
                opacity: 1
            });
        } else {
            this.setState({
                opacity: 0
            });
        }
    };

    returnData = () => {
        return this.state.dataset
    };

    componentWillReceiveProps = (nextProps) => {

        const playerID = nextProps.playerID;
        const startDate = nextProps.startDate;
        const endDate = nextProps.endDate;
        if (this.props !== nextProps) {
            this.setState({
                playerID: playerID,
                startDate: startDate,
                endDate: endDate,
                showGif:true
            });
            let timeString = "";
            if (nextProps.startDate instanceof moment && nextProps.endDate
                instanceof moment) {

                timeString = "&beginningTime=" +
                    nextProps.startDate.format("YYYYMMMDD") +
                    "&endingTime=" + nextProps.endDate.format(
                        "YYYYMMMDD");
            }

            jQuery.ajax({

                url: "/GetAverageScores?id=" + playerID
                + timeString,
                type: "GET",
                dataType: "json",
                async: true,
                success: function(data) {
                    if (data.success === false) {
                        this.handleFailure(data.message)
                        this.setState({
                            showGraph:false
                        });
                    } else {

                        this.setState({
                            dataset: ParseApiMessage(data),
                            result: data.success,
                            displayError: false,
                            errorMessage: '',
                            showGraph:true,
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
            showGif:false
        });
    };

    returnYLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="middle" fontSize={'16pt'}
                  className="XAxisLabel">Score</text>
        )
    };

    returnXLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="middle" fontSize={'16pt'}
                  className="XAxisLabel">Game</text>
        )
    };

    toggleScore = () => {
        let toggle = !this.state.showScore;
        let text = "";
        if (toggle === true) {
            text = "Hide Score";
        } else {
            text = "Show Score";
        }
        this.setState({
            showScore: toggle,
            scoreButtonValue: text
        })

    };

    toggleOppScore = () => {
        let toggle = !this.state.showOppScore;
        let text = "";
        if (toggle === true) {
            text = "Hide Opp Score";
        } else {
            text = "Show Opp Score";
        }
        this.setState({
            showOppScore: toggle,
            oppScoreButtonValue: text
        })

    };

    toggleAverage = () => {
        let toggle = !this.state.showAverage;
        let text = "";
        if (toggle === true) {
            text = "Hide Elo";
        } else {
            text = "Show Elo";
        }
        this.setState({
            showAverage: toggle,
            averageButtonValue: text
        })

    };

    toggleOppAverage = () => {
        let toggle = !this.state.showOppAverage;
        let text = "";
        if (toggle === true) {
            text = "Hide Opp Elo";
        } else {
            text = "Show Opp Elo";
        }
        this.setState({
            showOppAverage: toggle,
            oppAverageButtonValue: text
        })

    };

    renderPlayersLineScore = (state) => {
        if (state === true) {
            return (<Line dataKey="score" barSize={25} fill='#4286f4'/>)
        }
    };

    renderLineOppScore = (state) => {
        if (state === true) {
            return (<Line dataKey="opponentScore" barSize={25}  stroke='#4d004d' fill='#4d004d'/>
            )
        }
    };

    renderAverageScore = (state) => {
        if (state === true) {
            return (<Line dataKey="" fill='#581887' stroke='#4d004d'/>
            )
        }
    };

    renderOppAverageScore = (state) => {
        if (state === true) {
            return (<Line dataKey="" fill='#470303' stroke='#ad0505'/>
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
        return jQuery('#infoDisplay').width() * .70;
    };

    returnStartTooltip = () => {
        return jQuery('#infoDisplay').width() * .76;
    };

    render() {
        let location = '../images/Spinner.gif';
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                    <span ><ToggleDisplay show={this.state.displayError}><text
                        className="errorMessageGraph">{this.state.errorMessage}</text></ToggleDisplay><text
                        className="graphHeaderText">Score Per Game</text></span>
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

                    <ComposedChart width={this.returnWidth()} height={400}
                                   data={this.state.dataset}
                                   margins={{top: 5, right: 30, bottom: 5}}>
                        <XAxis allowDecimals={false} type="number"
                               dataKey="label" domain={[0, 'auto']}
                               label={this.returnXLabel(475, 375)}
                               padding={{bottom: 50, right: 10}}
                               labelStyle={{paddingTop: 20, color: '#32CD32'}}/>
                        <YAxis domain={[0, 'auto']}
                               label={this.returnYLabel(30, 150)}
                               />
                        <Tooltip position={{x: this.returnStartTooltip(), y: 0}}
                                 content={<CustomToolTipDisplayGame
                                     setGameDisplay={this.setGameDisplayState}/>}/>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <ReferenceLine y={0} stroke='#000'/>
                        {this.renderPlayersLineScore(this.state.showScore)}
                        {this.renderLineOppScore(this.state.showOppScore)}
                        <Legend margins={{top: 15, right: 15, bottom: 5}}/>
                    </ComposedChart>
                    </ToggleDisplay>
                </div>
                <div className="averageScoreToggleButtons">
                    <button className="graphButton"
                            onClick={() => this.toggleScore()}>{this.state.scoreButtonValue}</button>
                    <button className="graphButton"
                            onClick={() => this.toggleOppScore()}>{this.state.oppScoreButtonValue}</button>
                </div>
            </div>
        )
    }
}