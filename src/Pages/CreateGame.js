import {Header} from '../ReactComponents/displayComponents.js';
import {DateInput, EditUsernameSelect, EditUsernameTypeAhead} from "./Games";
import moment from 'moment';
import {Typeahead} from 'react-bootstrap-typeahead';
import DatePicker from 'react-datepicker';
import TimePicker from 'rc-time-picker';
import '../timeinput.css';
import {Border, Box, Button, Flex, Provider, Text} from "rebass";
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");
require("../CreateGame.css");
require("../ReactDatePicker.css");

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
            time:moment(),
            open:false,
            showKim: false,
            resultGame:{
                player1: {
                    username:''
                },
                player2:{
                    username:''
                },
                score1:'',
                score2:'',
                timeString:moment()
            },
            player1:{
              id:'',
                username:''
            },
            player2:{
                id:'',
                username:''
            },
            showGameVs:false
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
            async: true,
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
            player1:event,
            player1ID:id
        });
    };

    setPlayer2 = (event) => {
        let id = event.id;
        this.setState({
            player2:event,
            player2ID: id,
        });
    };

    handleSubmit(event) {
        console.log(this.state.value);

        let timeString = moment(this.state.time).format('HH:mm:ss');

        jQuery.ajax({
            url: "http://localhost:8080/CreateGame?player1ID="
            + this.state.player1ID + "&player2ID=" + this.state.player2ID +
            "&score1=" + this.state.score1 + "&score2=" + this.state.score2
            + "&date=" + this.state.date.format('YYYYMMMDD')+"&time="+timeString,
            type: "POST",
            dataType: "json",
            async: true,
            success: function(data) {
                if (data.message === "Should you be doing that?") {
                    this.setState({
                        result: data.success,
                        showKim: true,
                        resultText: data.message,
                        showGameVs:false,
                        resultGame:{
                            player1: {
                                username:''
                            },
                            player2:{
                                username:''
                            },
                            score1:'',
                            score2:''
                        }
                    });
                    this.clearInputs();
                    event.preventDefault();
                } else if (data.success){
                    this.setState({
                        result: data.success,
                        resultText: data.message,
                        resultGame:JSON.parse(data.game),
                        showGameVs:true,
                        showKim:false
                    });
                    this.clearInputs();
                    event.preventDefault();
                } else {
                    this.setState({
                        result: data.success,
                        resultText: data.message,
                        showGameVs:false,
                        showKim:false,
                        resultGame:{
                            player1: {
                                username:''
                            },
                            player2:{
                                username:''
                            },
                            score1:'',
                            score2:''
                        }
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
            date: date
        })

    };

    handleTimeChange = (time) => {
        this.setState({
            time: time
        })

    };

    setOpen = ({ open }) => {
        this.setState({ open });
    };
    toggleOpen = () => {
        this.setState({
            open: !this.state.open,
        });
    };

    render() {
        return (

<div className="createGameContainer">
            <div className="createGameForm">
                <Provider
                    theme={{
                        font: '"Serif"',
                    }}
                >                        <Border borderWidth={4}>

                <Flex wrap mx={50} >
                        <Box px={2} py={2} width={1/2}
                        className="createGameFormText">
                            <Text p={1} color='black' bg='white'
                                  f={35}
                                  font-family="Serif"
                            >
                                <em > Date :</em>
                            </Text>
                        </Box>


                        <Box px={2} pb={3} pt={2} width={1 / 2}>
                            <div className="Select-control-wrapper">
                                <DateInput
                                    startDate={this.state.date}
                                    onChange={this.handleDateChange}/>
                            </div>
                        </Box>

                        <Box px={2} py={2} width={1/2}
                             className="createGameFormText">
                            <Text p={1} color='black' bg='white'
                                  f={35}
                                  font-family="Serif"
                            >
                                <em > Time :</em>
                            </Text>
                        </Box>


                        <Box px={2} py={2} width={1 / 2}>
                            <div className="Select-control-wrapper">
                            <TimePicker
                                onChange={this.handleTimeChange}
                                defaultValue={moment()}
                                use12Hours
                                open={this.state.open}
                                onOpen={this.setOpen}
                                onClose={this.setOpen}/>
                            </div>
                        </Box>
                        <Box px={2} py={2} width={1/2}
                             className="createGameFormText">
                            <Text p={1} color='black' bg='white'
                                  f={35}
                                  font-family="Serif"
                            >
                                <em > Player 1 :</em>
                            </Text>
                        </Box>


                        <Box px={2} py={2} width={1 / 2}>
                            <div className="Select-control-wrapper">
                            <EditUsernameSelect
                                id="player1IDInput"
                                players={this.state.players}
                                onOptionSelected={(event) => this.setPlayer1(
                                    event)}
                            currentPlayer={this.state.player1}/>
                            </div>
                        </Box>
                        <Box px={2} py={2} width={1/2}
                             className="createGameFormText">
                            <Text p={1} color='black' bg='white'
                                  f={35}
                                  font-family="Serif"
                            >
                                <em > Player 2 :</em>
                            </Text>
                        </Box>


                        <Box px={2} py={2} width={1 / 2}>
                            <div className="Select-control-wrapper">
                            <EditUsernameSelect
                                id="player2IDInput"
                                players={this.state.players}
                                onOptionSelected={(event) => this.setPlayer2(
                                    event)}
                                currentPlayer={this.state.player2}/>
                            </div>
                        </Box>
                        <Box px={2} py={2} width={1/2}
                             className="createGameFormText">
                            <Text p={1} color='black' bg='white'
                                  f={35}
                                  font-family="Serif"
                            >
                                <em > Score 1 :</em>
                            </Text>
                        </Box>


                        <Box px={2} py={2} width={1 / 2}>
                            <div className="Select-control-wrapper">
                            <input className="scoreInput"
                                   id="score1Input"
                                   type="number"
                                   value={this.state.score1}
                                   onChange={this.handleChange}/>
                            </div>
                        </Box>
                        <Box px={2} py={2} width={1/2}
                             className="createGameFormText">
                            <Text p={1} color='black' bg='white'
                                  f={35}
                                  font-family="Serif"
                            >
                                <em > Score 2 :</em>
                            </Text>
                        </Box>


                        <Box px={2} py={2} width={1 / 2}>
                            <div className="Select-control-wrapper">
                            <input className="scoreInput"
                                   type="number"
                                   id="score2Input"
                                   value={this.state.score2}
                                   onChange={this.handleChange}/>
                            </div>
                        </Box>
                    <Box width={1} py={2} className="createGameFormText">
                        <Button onClick={this.handleSubmit}>
                            Submit
                        </Button>

                    </Box>
                    </Flex>
                </Border>
                </Provider>


            </div>
            <div className="createGameDisplay">


    <GameView resultText={this.state.resultText}
              player1Username={this.state.resultGame.player1.username}
              player2Username={this.state.resultGame.player2.username}
              score1={this.state.resultGame.score1}
              score2={this.state.resultGame.score2}
              showGame={this.state.showGameVs}
              time={moment(this.state.resultGame.timeString).format("YYYY-MM-DD hh:mm a")}/>
                {
                    this.state.showKim ?
                        <img src={require('../images/kimGif.gif')}
                             width='45%' height='45%'/> :
                        <img></img>
                }
                </div>
    <div>

    </div>
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
                    <Header id="header" className="header" selectedButton="inputButton" secondarySelected="gameInput"/>
                </div>
                <br/>
                <div id="PlayerForm">
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

class GameView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            game: props.game,
            showGame:false,
            time:''
        };
    }

    componentWillReceiveProps = (nextProps) => {
        this.setState({
            game: nextProps.game,
            showGame:nextProps.showGame,
            time:nextProps.time

        });

    };

    render() {
        return (
                <Provider
                    theme={{
                        font: '"Serif"',
                    }}
                >
                    <Flex wrap mx={-2}>
                        <Box px={2} py={2} width={1}>
                            <ResultText text={this.props.resultText}/>
                        </Box>


                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u>  {this.props.player1Username} </u>
                                </em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u> {this.props.player2Username} </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={0} width={1}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>

                                {
                                    this.props.showGame ? <em>vs.</em> : ''
                                }
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.props.score1}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.props.score2}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {
                                    this.props.showGame ? <em>{this.props.time}</em> : ''
                                }
                            </Text>
                        </Box>
                    </Flex>
                </Provider>



        );
    }
}


