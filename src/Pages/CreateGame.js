import {Header} from '../ReactComponents/displayComponents.js';

const React = require('react');
const jQuery = require('jquery');
var _ = require('lodash');
const css = require("css-loader");
require("../stylesheet.css");
var FontAwesome = require('react-fontawesome');

class CreatePlayerForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            player1ID: '',
            player2ID:'',
            result:'',
            score1:'',
            score2:''
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }



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
    }

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
                    <label>
                        Player 1 ID:
                        <input id="player1IDInput" type="text" value={this.state.player1ID} onChange={this.handleChange} />
                    </label>
                    <br/>
                    <label>
                        Player 2 ID:
                        <input id="player2IDInput" type="text" value={this.state.player2ID} onChange={this.handleChange} />
                    </label>
                    <br/>
                    <label>
                        Score 1:
                        <input id="score1Input" type="text" value={this.state.score1} onChange={this.handleChange} />
                    </label>
                    <br/>
                    <label>
                        Score 2:
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
                    <CreatePlayerForm/>
                </div>
            </div>
        );

    }
}
