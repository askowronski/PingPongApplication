import {Header} from '../ReactComponents/displayComponents.js';
import {DateInput, EditUsernameTypeAhead} from "./Games";
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

        let timeString = moment(this.state.time).format('HH:mm:ss');

        jQuery.ajax({
            url: "http://localhost:8080/CreateGame?player1ID="
            + this.state.player1ID + "&player2ID=" + this.state.player2ID +
            "&score1=" + this.state.score1 + "&score2=" + this.state.score2
            + "&date=" + this.state.date.format('YYYYMMMDD')+"&time="+timeString,
            type: "POST",
            dataType: "json",
            async: false,
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
                        showGameVs:true
                    });
                    this.clearInputs();
                    event.preventDefault();
                } else {
                    this.setState({
                        result: data.success,
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
                {/*<table className="createGameTable">*/}
                    {/*<td>*/}
                        {/*<div className="creatGameForm">*/}
                            {/*<form onSubmit={this.handleSubmit}*/}
                                  {/*className="createGameForm">*/}
                                {/*<table className="inputTable">*/}
                                    {/*<tbody>*/}
                                    {/*<tr className="inputRow">*/}
                                        {/*<label className="inputGameLabel">*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<text*/}
                                                    {/*className="inputGameLabel">*/}
                                                    {/*Date :*/}
                                                {/*</text>*/}
                                            {/*</td>*/}
                                        {/*</label>*/}
                                        {/*<td className="inputCell">*/}
                                                    {/*<DateInput*/}
                                                        {/*startDate={this.state.date}*/}
                                                        {/*onChange={this.handleDateChange}/>*/}
                                            {/*</td>*/}
                                    {/*</tr>*/}
                                    {/*<tr className="inputRow">*/}
                                        {/*<label className="inputGameLabel">*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<text*/}
                                                    {/*className="inputGameLabel">*/}
                                                    {/*Time :*/}
                                                {/*</text>*/}
                                            {/*</td>*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<div*/}
                                                    {/*className="choosePlayerTypeAhead">*/}
                                                    {/*<TimePicker*/}
                                                        {/*onChange={this.handleTimeChange}*/}
                                                        {/*defaultValue={moment()}*/}
                                                        {/*use12Hours*/}
                                                        {/*open={this.state.open}*/}
                                                        {/*onOpen={this.setOpen}*/}
                                                        {/*onClose={this.setOpen}/>*/}
                                                {/*</div>*/}
                                            {/*</td>*/}
                                        {/*</label>*/}
                                    {/*</tr>*/}
                                    {/*<tr className="inputRow">*/}
                                        {/*<label className="inputGameLabel">*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<text*/}
                                                    {/*className="inputGameLabel">*/}
                                                    {/*Player 1:*/}
                                                {/*</text>*/}
                                            {/*</td>*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<div*/}
                                                    {/*className="choosePlayerTypeAhead">*/}
                                                    {/*<EditUsernameTypeAhead*/}
                                                        {/*id="player1IDInput"*/}
                                                        {/*players={this.state.players}*/}
                                                        {/*onOptionSelected={(event) => this.setPlayer1(*/}
                                                            {/*event)}/>*/}
                                                {/*</div>*/}
                                            {/*</td>*/}
                                        {/*</label>*/}
                                    {/*</tr>*/}
                                    {/*<tr className="inputRow">*/}
                                        {/*<label className="inputGameLabel">*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<text*/}
                                                    {/*className="inputGameLabel">*/}
                                                    {/*Player 2:*/}
                                                {/*</text>*/}
                                            {/*</td>*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<div*/}
                                                    {/*className="choosePlayerTypeAhead">*/}
                                                    {/*<EditUsernameTypeAhead*/}
                                                        {/*id="player2IDInput"*/}
                                                        {/*players={this.state.players}*/}
                                                        {/*onOptionSelected={(event) => this.setPlayer2(*/}
                                                            {/*event)}/>*/}
                                                {/*</div>*/}
                                            {/*</td>*/}
                                        {/*</label>*/}
                                    {/*</tr>*/}
                                    {/*<tr className="inputRow">*/}
                                        {/*<label>*/}
                                            {/*<td className="inputCell" v>*/}
                                                {/*<text*/}
                                                    {/*className="inputGameLabel">*/}
                                                    {/*Score 1:*/}
                                                {/*</text>*/}
                                            {/*</td>*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<input className="scoreInput"*/}
                                                       {/*type="text"*/}
                                                       {/*id="score1Input"*/}
                                                       {/*value={this.state.score1}*/}
                                                       {/*onChange={this.handleChange}/>*/}
                                            {/*</td>*/}
                                        {/*</label>*/}
                                    {/*</tr>*/}
                                    {/*<tr className="inputRow">*/}
                                        {/*<label>*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<text*/}
                                                    {/*className="inputGameLabel">*/}
                                                    {/*Score 2:*/}
                                                {/*</text>*/}
                                            {/*</td>*/}
                                            {/*<td className="inputCell">*/}
                                                {/*<input className="scoreInput"*/}
                                                       {/*id="score2Input"*/}
                                                       {/*type="text"*/}
                                                       {/*value={this.state.score2}*/}
                                                       {/*onChange={this.handleChange}/>*/}
                                            {/*</td>*/}
                                        {/*</label>*/}

                                    {/*</tr>*/}
                                    {/*<tr rowSpan={2}>*/}
                                    {/*<input type="submit"*/}
                                           {/*className="createButton"*/}
                                           {/*value="Submit"/>*/}
                                    {/*</tr>*/}
                                    {/*</tbody>*/}
                                {/*</table>*/}
                            {/*</form>*/}
                        {/*</div>*/}

                    {/*</td>*/}
                    {/*<td>*/}
                        {/*<div>*/}


                            {/*{*/}
                                {/*this.state.showKim ?*/}
                                    {/*<img src={require('../images/kimGif.gif')}*/}
                                         {/*width='80%' height='80%'/> :*/}
                                    {/*<img></img>*/}
                            {/*}*/}

                        {/*</div>*/}


                    {/*</td>*/}
                {/*</table>*/}
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


                        <Box px={2} py={2} width={1 / 2}>
                                <DateInput
                                    startDate={this.state.date}
                                    onChange={this.handleDateChange}/>
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
                            <TimePicker
                                onChange={this.handleTimeChange}
                                defaultValue={moment()}
                                use12Hours
                                open={this.state.open}
                                onOpen={this.setOpen}
                                onClose={this.setOpen}/>
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
                            <div>
                            <EditUsernameTypeAhead
                                id="player1IDInput"
                                players={this.state.players}
                                onOptionSelected={(event) => this.setPlayer1(
                                    event)}/>
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
                            <EditUsernameTypeAhead
                                id="player2IDInput"
                                players={this.state.players}
                                onOptionSelected={(event) => this.setPlayer2(
                                    event)}/>
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
                            <input className="scoreInput"
                                   type="text"
                                   id="score1Input"
                                   value={this.state.score1}
                                   onChange={this.handleChange}/>
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
                            <input className="scoreInput"
                                   type="text"
                                   id="score2Input"
                                   value={this.state.score2}
                                   onChange={this.handleChange}/>
                        </Box>
                    <Box width={1} className="createGameFormText">
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
                </div>
    <div>
        {
            this.state.showKim ?
                <img src={require('../images/kimGif.gif')}
                     width='80%' height='80%'/> :
                <img></img>
        }
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
                    <Header id="header" className="header"/>
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

                    {/*<ResultText text={this.props.resultText}/>*/}
                    {/*<div className="gameCell1">{this.props.player1Username}</div>*/}
                    {/*<div className="gameCell2">{*/}
                    {/*this.props.showGame ? <text>vs.</text> : ''*/}
                    {/*}</div>*/}
                    {/*<div className="gameCell3">{this.props.player2Username}</div>*/}
                    {/*<div className="gameCell5">{this.props.score1}</div>*/}
                    {/*<div className="gameCell6">{this.props.score2}</div>*/}
                    {/*<div className="gameCell7">{*/}
                    {/*this.props.showGame ? <text>{this.props.time}</text> : ''*/}
                    {/*}</div>*/}
                </Provider>



        );
    }
}
