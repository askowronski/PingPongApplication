import {Header} from '../ReactComponents/displayComponents.js';
import {PlayerTypeAhead} from "./PlayerProfilePage";
import ToggleDisplay from 'react-toggle-display';
import {DateInput, EditUsernameTypeAhead} from "./Games";
import moment from 'moment';
import {Typeahead} from 'react-bootstrap-typeahead';
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");
require("../CreateGame.css");

class CreateGameForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            player1ID: '',
            player2ID: '',
            result: '',
            score1: '',
            score2: '',
            players: [],
            resultPlayers: '',
            date: '',
            resultText: '',
            showKim:false
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount = () => {
        document.addEventListener('keyup', function(event) {
            if (event.keyCode === 9 && event.target.className
                == "typeahead-text-inputplayer1IDInput") {
                if (!event.shiftKey) {
                    jQuery(".typeahead-text-inputplayer2IDInput").focus();
                }
            } else if (event.keyCode === 9 && event.target.className
                == "typeahead-text-inputplayer2IDInput") {
                if (!event.shiftKey) {
                    jQuery("#score1Input").focus();
                }
            }
        });
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    players: JSON.parse(data.message),
                    resultPlayers: data.success,
                    date: moment()
                });
            }.bind(this)
        });
    };

    handleChange(event) {
        if (event.target.id === "player1IDInput") {
            this.setState({
                player1ID: event.target.value
            });
        }
        if (event.target.id === "player2IDInput") {
            this.setState({
                player2ID: event.target.value
            });
        }
        if (event.target.id === "score1Input") {
            this.setState({
                score1: event.target.value
            });
        }
        if (event.target.id === "score2Input") {
            this.setState({
                score2: event.target.value
            });
        }
    };

    setPlayer1 = (event) => {
        let id = event.id;
        this.setState({
            player1ID: id,
        });
    };

    setPlayer2 = (event) => {
        let id = event.id;
        this.setState({
            player2ID: id,
        });
    };

    handleSubmit(event) {
        console.log(this.state.value);

        jQuery.ajax({
            url: "http://localhost:8080/CreateGame?player1ID="
            + this.state.player1ID + "&player2ID=" + this.state.player2ID +
            "&score1=" + this.state.score1 + "&score2=" + this.state.score2
            + "&time=" + this.state.date.format('YYYYMMMDD'),
            type: "POST",
            dataType: "json",
            async: false,
            success: function(data) {
                if (data.success) {
                    this.setState({
                        result: data.success,
                        resultText: data.message
                    });
                    this.clearInputs();
                    event.preventDefault();
                } else {
                    this.setState({
                        showKim: true,
                        resultText: data.message
                    });
                    this.clearInputs();
                    event.preventDefault();
                }
            }.bind(this)
        });

    };

    clearInputs = () => {
        jQuery('.typeahead-text-inputplayer1IDInput').val('')
        jQuery('#score1Input').val('')
        jQuery('#score2Input').val('')
        this.setState({
            score1: '',
            score2: '',
        });

    };

    handleDateChange = (date) => {
        this.setState({
            date: moment(date)
        })

    };

    render() {
        return (


            <div>
                <table className="createGameTable">
                    <td>
                        <div className="creatGameForm">
                            <form onSubmit={this.handleSubmit}
                                  className="createGameForm">
                                <table className="inputTable">
                                    <tbody>
                                    <tr className="inputRow">
                                        <label className="inputGameLabel">
                                            <td className="inputCell">
                                                <text
                                                    className="inputGameLabel">
                                                    Date :
                                                </text>
                                            </td>
                                            <td className="inputCell">
                                                <div
                                                    className="choosePlayerTypeAhead">
                                                    <DateInput
                                                        startDate={this.state.date}
                                                        onChange={this.handleDateChange}/>
                                                </div>
                                            </td>
                                        </label>
                                    </tr>
                                    <tr className="inputRow">
                                        <label className="inputGameLabel">
                                            <td className="inputCell">
                                                <text
                                                    className="inputGameLabel">
                                                    Player 1:
                                                </text>
                                            </td>
                                            <td className="inputCell">
                                                <div
                                                    className="choosePlayerTypeAhead">
                                                    <EditUsernameTypeAhead
                                                        id="player1IDInput"
                                                        players={this.state.players}
                                                        onOptionSelected={(event) => this.setPlayer1(
                                                            event)}/>
                                                </div>
                                            </td>
                                        </label>
                                    </tr>
                                    <tr className="inputRow">
                                        <label className="inputGameLabel">
                                            <td className="inputCell">
                                                <text
                                                    className="inputGameLabel">
                                                    Player 2:
                                                </text>
                                            </td>
                                            <td className="inputCell">
                                                <div
                                                    className="choosePlayerTypeAhead">
                                                    <EditUsernameTypeAhead
                                                        id="player2IDInput"
                                                        players={this.state.players}
                                                        onOptionSelected={(event) => this.setPlayer2(
                                                            event)}/>
                                                </div>
                                            </td>
                                        </label>
                                    </tr>
                                    <tr className="inputRow">
                                        <label>
                                            <td className="inputCell" v>
                                                <text
                                                    className="inputGameLabel">
                                                    Score 1:
                                                </text>
                                            </td>
                                            <td className="inputCell">
                                                <input className="scoreInput"
                                                       type="text"
                                                       id="score1Input"
                                                       value={this.state.score1}
                                                       onChange={this.handleChange}/>
                                            </td>
                                        </label>
                                    </tr>
                                    <tr className="inputRow">
                                        <label>
                                            <td className="inputCell">
                                                <text
                                                    className="inputGameLabel">
                                                    Score 2:
                                                </text>
                                            </td>
                                            <td className="inputCell">
                                                <input className="scoreInput"
                                                       id="score2Input"
                                                       type="text"
                                                       value={this.state.score2}
                                                       onChange={this.handleChange}/>
                                            </td>
                                        </label>
                                        <input type="submit"
                                               className="createButton"
                                               value="Submit"/>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>

                    </td>
                    <td>
                        <div>
                            <ResultText text={this.state.resultText}/>

                            {
                                this.state.showKim ?<img src={require('../images/kimGif.gif')}
                                                         width='80%' height='80%'/> :<img></img>
                            }
                        </div>

                    </td>
                </table>
            </div>
        );
    }
}

export default class CreateGame extends React.Component {
    state = {
        buttonNames: ["InputPlayer", "Scores", "Players"]
    };

    render() {

        return (
            <div>
                <div>
                    <Header id="header" className="header"/>
                </div>
                <br/>
                <div id="PlayerForm" className="CreatePlayerForm">
                    <CreateGameForm/>
                </div>
            </div>
        );

    }
}

export class ResultText extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            text: props.text,
            header: ''
        };
    }

    componentWillReceiveProps = (nextProps) => {
        const resultText = nextProps.text;
        let show = '';
        if (resultText.length > 0) {
            show = 'Result';
        }

        this.setState({
            text: resultText,
            header: show
        });

    };

    render() {
        return (
            <div className="resultTextContrainer">
                <span className="ResultHeaderText"> {this.state.header}</span>
                <br/>
                <span className="resultText">{this.state.text}</span>
            </div>
        )
    };
}

class CreateGamePlayerTypeAhead extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            players: props.players,
            currentPlayer: props.currentPlayer
        };
        this.handleChange = props.handleChange;
    }

    render() {
        return (
            <Typeahead

                onChange={this.handleChange}
                options={this.state.players}
            />
        );
    }
}
