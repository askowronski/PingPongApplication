import ToggleDisplay from 'react-toggle-display';
import history from '../history.js';

const React = require('react');
const jQuery = require('jquery');
var _ = require('lodash');
const css = require("css-loader");
require("../stylesheet.css");
var Router = require('react-router').Router;

class InfoDisplayPlayer extends React.Component {

    state = {
        data: "",
        username: "",
        id: "",
        eloRating: ""
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayer?id=1",
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                this.setState({
                    data: data.message,
                    username: JSON.parse(data.message).username,
                    id: JSON.parse(data.message).id,
                    eloRating: JSON.parse(data.message).eloRating.rating
                });
            }.bind(this)
        });

    };


    render() {
        return (

            <div className="square">
                <span>Username: {this.state.username} </span>
                <br/>
                <span>Rating: {this.state.eloRating}</span>
                <br/>
                <span>ID: {this.state.id}</span>
            </div>

        );
    }
}

class InfoDisplayGame extends React.Component  {

    state = {
        data:"",
        player1ID:"",
        id:"" ,
        player2ID:"",
        score1:"",
        score2:""
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetGame?iD=2",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    data:data.message,
                    player1ID:JSON.parse(data.message).player1.id,
                    player2ID:JSON.parse(data.message).player2.id,
                    id: JSON.parse(data.message).iD,
                    score1:JSON.parse(data.message).score1,
                    score2:JSON.parse(data.message).score2
                });
            }.bind(this)
        });

    };



    render(){
        return (

            <div id="displayGameContainer" className="square">
                <span>id: {this.state.id} </span>
                <br/>
                <span>Player1 ID: {this.state.player1ID}</span>
                <br/>
                <span>Player2 ID: {this.state.player2ID}</span>
                <br/>
                <span>score1: {this.state.score1}</span>
                <br/>
                <span>score2: {this.state.score2}</span>
            </div>

        );
    }
}

class InfoDisplayHighestRating extends React.Component  {

    state = {
        data:"",
        id:"",
        username:"",
        eloRating:"",
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayerWithHighestRating",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    data:data.message,
                    id:JSON.parse(data.message).id,
                    username:JSON.parse(data.message).username,
                    eloRating: JSON.parse(data.message).eloRating.rating,
                });
            }.bind(this)
        });

    };



    render(){
        return (

            <div id="highestEloRatingContainer" className="square">
                <span id="highestEloRatingHeader">Player With Highest Elo Rating!</span>
                <br/>
                <span id="highestEloRatingUsername">Username: {this.state.username} </span>
                <br/>
                <span id="highestEloRatingRating">Rating: {this.state.eloRating}</span>
                <br/>
                <span id="highestEloRatingID">ID: {this.state.id}</span>
            </div>

        );
    }
}

class InfoDisplayTotalGameStats extends React.Component  {

    state = {
        data:"",
        totalGamesPlayed:"",
        stdDevForLosses:"",
        stdDevForGames:"",
        averageEloRating:""
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/TotalGameStats",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    data:data.message,
                    totalGamesPlayed:JSON.parse(data.message).totalGames,
                    stdDevForGames:JSON.parse(data.message).stdDevForGames,
                    stdDevForLosses: JSON.parse(data.message).stdDevForLosses,
                    averageEloRating:JSON.parse(data.message).averageEloRating
                });
            }.bind(this)
        });

    };



    render(){
        return (

            <div className="square">
                <span>Total Game Stats</span>
                <br/>
                <span>Total Games Played: {this.state.totalGamesPlayed} </span>
                <br/>
                <span>Std Dev For Games: {this.state.stdDevForGames}</span>
                <br/>
                <span>Std Dev For Losses: {this.state.stdDevForLosses}</span>
                <br/>
                <span>Average Elo Rating: {this.state.averageEloRating}</span>
            </div>

        );
    }
}


export const InfoDisplayTable = (props) => {
    return (
        <div id = "displayTable" className="displayTable">
            <table >
                <thead>
                <tr>
                    <th colSpan={2}>Home Page</th>
                </tr>
                </thead>
                <tbody>
                <tr id="infoDisplay" className="infoDisplay">
                    <td><InfoDisplayPlayer/></td>
                    <td><InfoDisplayGame/></td>
                </tr>
                <tr id="infoDisplay" className="infoDisplay">
                    <td><InfoDisplayHighestRating/></td>
                    <td><InfoDisplayTotalGameStats/></td>
                </tr>
                </tbody>
            </table>
        </div>
    );
};

const InputButtons = (props) => {

    const inputPlayer = () => {
        history.push('/CreatePlayer');
    };

    const inputGame = () => {
        history.push('/CreateGame');
    };

    return (
        <div className="topLevlInputButtons">
            <ToggleDisplay id="inputDisplayToggle"  >
                <div id="inputButtonDiv" className="header-row-input">
                    <div className="header-button-input-holder">
                        <button className="header-button-input" onClick={inputPlayer} id="playerInputButton" >Player</button>
                        <button className="header-button-input" onClick={inputGame} id="gameInputButton" >Game</button>
                    </div>
                </div>
            </ToggleDisplay>
        </div>

    );
};

export class HeaderButtons extends React.Component {
    state = {
        buttonDisplayNames:["Input","Scores","Players"],
        showInputButtons:false
    };

    goToPlayers = () => {
    history.push('/Players');

    };

    goToGames = () => {
    history.push('/Games');
    };

    onInputClick = () => {
        this.setState({
            showInputButtons:!this.state.showInputButtons
        });

        if(jQuery('#playerInputButton').css('visibility')==="hidden"){
            jQuery('#playerInputButton').css('visibility','visible');
            jQuery('#gameInputButton').css('visibility','visible');
        } else {
            jQuery('#playerInputButton').css('visibility', 'hidden');
            jQuery('#gameInputButton').css('visibility', 'hidden');
        }
    };

    render(){
        return (
            <div id="headerContainer">
            <div className="header-row">
                <div  className="header-button-holder"> <button className="header-button" onClick={() => this.onInputClick()} id={this.state.buttonDisplayNames[0]} >{this.state.buttonDisplayNames[0]}</button> </div>
                <div  className="header-button-holder"> <button className="header-button" onClick={() =>this. goToGames()} id={this.state.buttonDisplayNames[1]} >{this.state.buttonDisplayNames[1]}</button> </div>
                <div  className="header-button-holder"> <button className="header-button" onClick={() => this.goToPlayers()} id={this.state.buttonDisplayNames[2]} >{this.state.buttonDisplayNames[2]}</button> </div>
                <div className="header-button-holder"><img src={require('../images/paddle.png')} /></div>
            </div>
                <div id="inputButtonContainer">
                    <InputButtons showInputButtons={this.state.showInputButtons}/>
                </div>
            </div>
        );
    }
}




export class Header extends React.Component {

    state = {
        showInputButtons:false,
    };

    onInputClick = () => {
        this.setState(prevState => ({
            showInputButtons:!prevState
        }));
    };

    render() {
        const {showInputButtons} = this.state;

        return (

        <div>
            <HeaderButtons />
        </div>
        )
    };
}








