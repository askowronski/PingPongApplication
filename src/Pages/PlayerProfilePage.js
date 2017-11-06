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
import pureRender from 'react-pure-render';
import {AverageScorePerGame} from "../PlayerProfileGraphComponents/ScorePerGameGraph";
import {
    EloRatingPerGame,
    ParseApiMessage
} from "../PlayerProfileGraphComponents/EloRatingGraph";
import {Typeahead} from "react-typeahead";
import history from '../history.js';
import {NetWinsGraph} from "../PlayerProfileGraphComponents/NetWinsGraph";
import {DateInput} from "./Games";
import moment from "moment";

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
                    <div className="custom-tooltip-average">
                        <table className="GameDisplayTable">
                            <th className="GameDisplayHeader" colSpan={5}>
                                <span
                                    className="HeaderText">Net Wins/Losses</span>
                            </th>
                            <tr>
                                <td>
                                </td>
                                <td className="AverageUserHeader" colSpan={3}>
                                    <span >{payload[0].payload.wins}</span>
                                </td>
                                <td>
                                </td>

                            </tr>
                            <tr>
                                <td colSpan={5} className="GameDisplayHeader">
                                    <span
                                        className="HeaderText">{this.getIntroOfPage(
                                        label, game.timeString)}</span>
                                </td>
                            </tr>
                            <tr>
                                <td>

                                </td>
                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">You</span>
                                </td>

                                <td className="AverageUserHeader" colSpan={2}>
                                    <span
                                        className="You">{game.player2.username}</span>
                                </td>
                            </tr>
                            <tr className="bottomRow">
                                <td>
                                    Score
                                </td>
                                <td colSpan={2}>
                                    {game.score1}
                                </td>
                                <td colSpan={2}>
                                    {game.score2}
                                </td>
                            </tr>
                        </table>
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
                let game = payload[0].payload.game;

                return (
                    <div className="custom-tooltip-average">
                        <table className="GameDisplayTable">
                            <thead>
                            <th className="GameDisplayHeader" colSpan={5}>
                                <span className="HeaderText">Average</span>
                            </th>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                </td>
                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">You</span>
                                </td>

                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">Opponent</span>
                                </td>
                            </tr>
                            <tr className="GameDisplayAverageRow">
                                <td>
                                    Score
                                </td>
                                <td colSpan={2}>
                                    {payload[0].payload.averageScore}
                                </td>
                                <td colSpan={2}>
                                    {payload[0].payload.opponentAverageScore}
                                </td>

                            </tr>
                            <tr>
                                <td colSpan={5} className="GameDisplayHeader">
                                    <span
                                        className="HeaderText">{this.getIntroOfPage(
                                        label, game.timeString)}</span>
                                </td>
                            </tr>
                            <tr>
                                <td>

                                </td>
                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">You</span>
                                </td>

                                <td className="AverageUserHeader" colSpan={2}>
                                    <span
                                        className="You">{game.player2.username}</span>
                                </td>
                            </tr>
                            <tr className="bottomRow">
                                <td>
                                    Score
                                </td>
                                <td colSpan={2}>
                                    {payload[0].payload.score}
                                </td>
                                <td colSpan={2}>
                                    {payload[0].payload.opponentScore}
                                </td>
                            </tr>
                            </tbody>
                        </table>
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
        errorMessage:'',
        displayError:false
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
            errorMessage:message,
            displayError:true
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
        this.state = {
            playerID: props.history.location.state.player.id,
            players: [],
            resultPlayers: '',
            playerUsername: props.history.location.state.player.username,
            history: history.state,
            startDate: '',
            endDate: '',
            alert: ''
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
                let date1 = moment(data.message.split("=")[0]);
                let date2 = moment(data.message.split("=")[1]);
                this.setState({
                    startDate: date1,
                    endDate: date2
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
            playerUsername: username
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
                        <text className="choosePlayer">Choose Player</text>
                        <div className="choosePlayerTypeAhead">
                            <PlayerTypeAhead
                                onOptionSelected={(event) => this.setPlayer(
                                    event)}
                                players={this.state.players}/>
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
                    <td><AverageScorePerGame startDate={this.state.startDate}
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
                    <td><EloRatingPerGame playerID={this.state.playerID}
                                          startDate={this.state.startDate}
                                          endDate={this.state.endDate}/></td>
                </tr>
                </tbody>
            </table>
        )
    }
}
;
