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
import {AverageScorePerGame} from "../PlayerProfileGraphComponents/ScorePerGameGraph";
import {
    EloRatingPerGame,
    ParseApiMessage
} from "../PlayerProfileGraphComponents/EloRatingGraph";
import ToggleDisplay from 'react-toggle-display';
import {Typeahead} from "react-typeahead";
import history from '../history.js';
import {NetWinsGraph} from "../PlayerProfileGraphComponents/NetWinsGraph";
import {DateInput, EditUsernameSelect} from "./Games";
import moment from "moment";
import {Box, Flex, Provider, Text} from "rebass";
import {ResultText} from "./CreateGame";

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");
const ReactFC = require('react-fusioncharts');
const fusioncharts = require('fusioncharts');
const charts = require('fusioncharts/fusioncharts.charts');
const {PropTypes} = React;

export const CustomToolTipDisplayNet = React.createClass({
    propTypes: {
        type: PropTypes.string,
        payload: PropTypes.array,
        label: PropTypes.number,
    },

    getIntroOfPage(label, timeString) {
        return "Game " + label + " - " + timeString;
    },

    render() {
        const {active} = this.props;

        if (active) {
            const {payload, label} = this.props;
            if (label > 0) {
                let game = payload[0].payload.game;

                return (
                    <div className="custom-tooltip-net">
                        <Provider
                            theme={{
                                font: '"Serif"',
                            }}
                        >
                            <Flex wrap mx={-2} align='center' width='auto'>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={32}><u>Game - {label} </u></Text>
                                </Box>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={28}>
                                        <em> <u> Net Wins/Losses </u> </em></Text>
                                </Box>


                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={26}>
                                         {payload[0].payload.wins}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        {moment(payload[0].payload.timeString ).format(
                                            "YYYY-MM-DD hh:mm a")}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>

                                    </Text>
                                </Box>
                                <Box px={2} py={0} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        <em> <u> You </u> </em>
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        <em> <u>  {payload[0].payload.player2Username} </u> </em>
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1/2}>
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

export const CustomToolTipDisplayGame = React.createClass({
    propTypes: {
        type: PropTypes.string,
        payload: PropTypes.array,
        label: PropTypes.number,
    },

    getIntroOfPage(label, timeString) {
        return "Game " + label + " - " + timeString;
    },

    render() {

        const {active} = this.props;

        if (active) {
            const {payload, label} = this.props;
            if (label > 0) {
                let number = label;
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
                                          f={32}><u>Game - {number} </u></Text>
                                </Box>
                                <Box px={2} py={2} width={1}>
                                    <Text p={1} color='black' bg='white'
                                          f={28}>Average</Text>
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
                                        {payload[0].payload.averageScore}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={26}>
                                        {payload[0].payload.opponentAverageScore}
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
                                        {payload[0].payload.score}
                                    </Text>
                                </Box>
                                <Box px={2} py={2} width={1 / 2}>
                                    <Text p={1} color='black' bg='white'
                                          f={24}>
                                        {payload[0].payload.opponentScore}
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

export class AverageScoreGraph extends React.Component {
    state = {
        dataset: [],
        result: '',
        errorMessage: '',
        displayError: false
    };

    returnData = () => {
        return this.state.dataset
    };

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetAverageScore?id=2",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                if (JSON.parse(data.success) === false) {
                    this.handleFailure(JSON.parse(data.message))
                } else {

                    this.setState({
                        dataset: JSON.parse(data.message),
                        result: data.success,
                    });
                }

            }.bind(this)
        });
        jQuery.ajax({

            url: "http://localhost:8080/GetGamesForPlayerChart?id=2",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    games: JSON.parse(data.message),
                    result: data.success,
                });

            }.bind(this)
        });

    };

    returnYLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="middle">Score</text>
        )
    };

    handleFailure = (message) => {
        this.setState({
            errorMessage: message,
            displayError: true
        });
    };

    returnXLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="middle">Through Current Game</text>
        )
    };

    render() {
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                    <BarChart width={500} height={400} data={this.state.dataset}
                              margins={{top: 5, right: 30, left: 20, bottom: 5}}
                    >
                        <XAxis dataKey="label"
                               label={this.returnXLabel(325, 262)}/>
                        <YAxis domain={[-10, 10]} name={"Win/Losses"}
                               ticks={[-10, 0, 10]}
                               label={this.returnYLabel(0, 175)}/>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <ReferenceLine y={0} stroke='#000'/>
                        <Legend  />
                        <Bar dataKey="averageScore" fill="#8884d8"/>}/>
                        <Tooltip  />

                    </BarChart>
                </div>
            </div>
        )
    }
}

export const PlayerTypeAhead = (props) => {
    let options = props.players;

    let displayOption = (option) => {
        return option.username;
    };

    let handleHint = (option) => {
        for (let i = 0; i < options.length; i++) {
            if (new RegExp('^' + option).test(options[0])) {
                props.currentPlayer = options[i];
            }
        }
        return '';
    };

    return (
        <Typeahead
            options={options}
            displayOption={displayOption}
            filterOption='username'
            value={props.currentPlayer}
            id={props.id}
            onOptionSelected={props.onOptionSelected}
            inputDisplayOption={handleHint}
            customClasses={{
                input: "typeAheadInput",
                results: "typeahead-list__container" + props.id,
                listItem: "typeahead-list__item" + props.id,
                hover: "typeahead-active" + props.id,
            }}

        />
    );
};

export class PlayerGraphTable extends React.Component {

    constructor(props) {
        super(props);
        let userNameDisplay = '';
        let userId = 0;
        let player = {
            id:'',
            username:''
        };

        if (props.history.location.state.player.id === 0) {
            userNameDisplay = "Choose A Player";

        } else {
            userNameDisplay = props.history.location.state.player.username;
            userId = props.history.location.state.player.id;
            player={
                id:userId,
                    username:userNameDisplay
            }

        }
        this.state = {
            playerID: userId,
            players: [],
            resultPlayers: '',
            playerUsername: userNameDisplay,
            history: history.state,
            startDate: '',
            endDate: '',
            alert: '',
            showEloGraph:false,
            player:player
        };
    }

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    players: ParseApiMessage(data),
                    resultPlayers: data.success,
                });
            }.bind(this)
        });
        jQuery.ajax({

            url: "http://localhost:8080/GetDateRangeForPlayersGames?id="
            + this.state.playerID,
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    startDate: moment(data.beginningDate),
                    endDate: moment(data.endingDate)
                });
            }.bind(this)
        });

    };

    onChangeStart = (date) => {
        this.setState({
            startDate: date
        })
    };

    onChangeEnd = (date) => {
        this.setState({
            endDate: date
        })
    };

    setPlayer = (event) => {
        let id = event.id;
        let username = event.username;
        this.setState({
            playerID: id,
            playerUsername: username,
            player:event
        })
    };

    renderEloRatingGraph = () => {
      this.setState({
          showEloGraph:true
      })
    };

    render() {
        return (
            <table >
                <thead>
                <tr>
                    <th className="playerProfileHeader">Player Profile &nbsp;
                        <span
                            className="UsernameHeader">{this.state.playerUsername}</span>
                    </th>
                </tr>
                <tr>
                    <div className="choosePlayerContainer">
                        <div className="choosePlayerTypeAhead">
                            <text className="choosePlayer">Choose Player</text>
                            <div className="Select-control-wrapper">
                            <EditUsernameSelect
                                players={this.state.players}
                                onOptionSelected={(event) => this.setPlayer(
                                    event)}
                                currentPlayer={this.state.player}
                            className="profile"/>
                        </div>
                            {/*<PlayerTypeAhead*/}
                                {/*onOptionSelected={(event) => this.setPlayer(*/}
                                    {/*event)}*/}
                                {/*players={this.state.players}/>*/}
                        </div>
                    </div>
                    <div className="dateInputContainer">

                        <div className="dateInput">
                            <text className="choosePlayer"> End Date</text>
                            <DateInput onChange={this.onChangeEnd}
                                       startDate={this.state.endDate}/>
                        </div>
                        <div className="dateInput">
                            <text className="choosePlayer"> Start Date</text>
                            <DateInput onChange={this.onChangeStart}
                                       startDate={this.state.startDate}/>
                        </div>
                    </div>
                </tr>
                </thead>

                <tbody>
                <tr id="infoDisplay">
                    <td><AverageScorePerGame
                        startDate={this.state.startDate}
                        endDate={this.state.endDate}
                        playerID={this.state.playerID}/>
                    </td>
                </tr>
                <tr id="infoDisplay">
                    <td><NetWinsGraph startDate={this.state.startDate}
                                      endDate={this.state.endDate}
                                      playerID={this.state.playerID}/></td>
                </tr>
                <tr>
                    <td>
                        <ToggleDisplay show={this.state.showEloGraph}>
                            <p id="loadingSpinner" style={{'text-align': 'center'}}>
                                <img src={require('../images/Spinner.gif')}
                                     width='45%' height='45%'/></p>
                        </ToggleDisplay>
                        <ToggleDisplay show={!this.state.showEloGraph}>
                        <EloRatingPerGame playerID={this.state.playerID}
                                          startDate={this.state.startDate}
                                          endDate={this.state.endDate}
                       />
                        </ToggleDisplay>
                    </td>
                </tr>
                </tbody>
            </table>
        )
    }
}
;
