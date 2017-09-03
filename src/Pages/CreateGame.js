import {Header} from '../ReactComponents/displayComponents.js';
import {PlayerTypeAhead} from "./PlayerProfilePage";
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");

class CreateGameForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            player1ID: '',
            player2ID:'',
            result:'',
            score1:'',
            score2:'',
            players:[],
            resultPlayers:''
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    players:JSON.parse(data.message),
                    resultPlayers:data.success,
                });
            }.bind(this)
        });
    };





    handleChange(event) {
        if(event.target.id === "player1IDInput"){
            this.setState({
                player1ID: event.target.value
            });
        }
        if(event.target.id === "player2IDInput"){
            this.setState({
                player2ID: event.target.value
            });
        }
        if(event.target.id === "score1Input"){
            this.setState({
                score1: event.target.value
            });
        }
        if(event.target.id === "score2Input"){
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

            url: "http://localhost:8080/CreateGame?player1ID="+this.state.player1ID+"&player2ID="+this.state.player2ID+
            "&score1="+this.state.score1+"&score2="+this.state.score2,
            type:"POST",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    result:data.success,
                });
                alert(data.message);
            }.bind(this)
        });

    };


    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    <label className="inputGameLabel">
                        <text className="inputGameLabel"> Player 1: </text>
                        <div className="choosePlayerTypeAhead">
                        <PlayerTypeAhead id="player1IDInput" players={this.state.players}
                                         onOptionSelected = {(event) => this.setPlayer1(event)}/>
                        </div>
                    </label>
                    <br/>
                    <label>
                        <text className="inputGameLabel"> Player 2: </text>
                        <div className="choosePlayerTypeAhead">
                        <PlayerTypeAhead id="player1IDInput" players={this.state.players}
                                         onOptionSelected = {(event) => this.setPlayer2(event)}/>
                        </div>
                    </label>
                    <br/>
                    <label>
                        <text className="inputGameLabel">   Score 1: </text>
                        <input id="score1Input" type="text" value={this.state.score1} onChange={this.handleChange} />
                    </label>
                    <br/>
                    <label>
                        <text className="inputGameLabel">  Score 2: </text>
                        <input id="score2Input" type="text" value={this.state.score2} onChange={this.handleChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
            </div>
        );
    }
}

export default class CreateGame extends React.Component {
    state = {
        buttonNames:["InputPlayer","Scores","Players"]
    };

    render(){

        return(
            <div>
                <div>
                    <Header id = "header" className="header"/>
                </div>
                <br/>
                <div id="PlayerForm" className="CreatePlayerForm">
                    <CreateGameForm/>
                </div>
            </div>
        );

    }
}
