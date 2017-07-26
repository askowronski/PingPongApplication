import ToggleDisplay from 'react-toggle-display';

const React = require('react');
const ReactDOM = require('react-dom');
const jQuery = require('jquery');
var _ = require('lodash');
const css = require("css-loader");
require("../stylesheet.css");
var FontAwesome = require('react-fontawesome');
var ReactRouter = require('react-router');
var Router = require('react-router').Router;
var Route = require('react-router').Route;

class InfoDisplayPlayer extends React.Component {

    state = {
        data: "",
        username: "",
        id: "",
        eloRating: ""
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayer?id=4",
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
                    newUsername:JSON.parse(data.message).newUsername,
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
                <span id="highestEloRatingUsername">Username: {this.state.newUsername} </span>
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

export const HeaderButtons = (props) => {

    let displayNames = ["Input","Scores","Players"];

    const showButtons = () => {

        jQuery('#inputDisplayToggle').show();
    };

    const goToPlayers = () => {
        window.location = ('/Players');
    };

    const goToGames = () => {
        window.location = ('/Games');
    };

    return (
        <div className="header-row">
            <div  className="header-button-holder"> <button className="header-button" onClick={() => showButtons()} id={displayNames[0]} >{displayNames[0]}</button> </div>
            <div  className="header-button-holder"> <button className="header-button" onClick={() => goToGames()} id={displayNames[1]} >{displayNames[1]}</button> </div>
            <div  className="header-button-holder"> <button className="header-button" onClick={() => goToPlayers()} id={displayNames[2]} >{displayNames[2]}</button> </div>
        </div>
    );
};

const InputButtons = (props) => {

    const inputPlayer = () => {
        window.location = '/CreatePlayer';
    };

    const inputGame = () => {
        window.location = '/CreateGame';
    };

        return (
            <ToggleDisplay id="inputDisplayToggle" show={props.showInputButtons}>
                <div className="header-row-input">
                    <div className="header-button-input-holder">
                        <button className="header-button-input" onClick={inputPlayer} id="playerInputButton">Player</button>
                        <button className="header-button-input" onClick={inputGame} id="gameInputButton">Game</button>
                    </div>
                </div>
            </ToggleDisplay>
        );
}


export class Header extends React.Component {

    state = {
        showInputButtons:false,
    };

    onInputClick = () => {
        jQuery('#inputDisplayToggle').show();
    };

    render() {
        const {showInputButtons} = this.state;

        return (

        <div>
            <HeaderButtons onClick={this.onInputClick}/>
            <InputButtons  showInputButtons = {showInputButtons}/>
        </div>
        )
    };
}








