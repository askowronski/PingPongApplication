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
        streak: ""
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetLongestStreak",
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                this.setState({
                    data: data.message,
                    username: JSON.parse(data.message).player.username,
                    id: JSON.parse(data.message).player.id,
                    streak:JSON.parse(data.message).streak
                });
            }.bind(this)
        });

    };


    render() {
        return (

            <div className="infoDisplay">
                <span className="infoDisplayHeader">Longest Streak </span>
                <br/>
                <span className="longestStreakUsername">Username: {this.state.username}</span>

                <span className="longestStreakUsername"> ID: {this.state.id}</span>
                <br/>
                <span className="longestStreakUsername">Streak: {this.state.streak}</span>
            </div>

        );
    }
}

class InfoDisplayGame extends React.Component  {

    state = {
        data:"",
        player1Username:"",
        id:"" ,
        player2Username:"",
        score1:"",
        score2:"",
        time:''
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetLastGame?",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    data:data.message,
                    player1Username:JSON.parse(data.message).player1.username,
                    player2Username:JSON.parse(data.message).player2.username,
                    id: JSON.parse(data.message).iD,
                    score1:JSON.parse(data.message).score1,
                    score2:JSON.parse(data.message).score2,
                    time:JSON.parse(data.message).timeString
                });
            }.bind(this)
        });

    };



    render(){
        return (

            <div className="infoDisplay" >
                <span className="infoDisplayHeader"> Last Game Played</span>
                <br/>
                <span className="longestStreakUsername">Date : {this.state.time} </span>
                <br/>
                <span className="longestStreakUsername">{this.state.player1Username}</span>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span className="longestStreakUsername">{this.state.player2Username}</span>
                <br/>
                <span>score: {this.state.score1}</span>
                &nbsp;&nbsp;&nbsp;
                <span>score: {this.state.score2}</span>

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

            <div className="infoDisplay" >
                <span className="infoDisplayHeader">Player With Highest Elo Rating</span>
                <br/>
                <span className="longestStreakUsername">Username: {this.state.username} </span>
                <br/>
                <span className="eloRatingText">Rating: {parseFloat(this.state.eloRating).toFixed(2)}</span>
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

            <div className="infoDisplay">
                <span className="infoDisplayHeader">Total Game Stats</span>
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
        <div id = "homePageTableContainer" className="displayTable">
            <table className="homePageTable">
                <tbody className="homePageBody">
                <tr id="infoDisplay" className="infoDisplay">
                    <td className="homePageTd"><InfoDisplayPlayer/></td>
                    <td className="homePageTd"><InfoDisplayGame/></td>
                </tr>
                <tr id="infoDisplay" className="infoDisplay">
                    <td className="homePageTd"><InfoDisplayHighestRating/></td>
                    <td className="homePageTd"><InfoDisplayTotalGameStats/></td>
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

    goToHomePage = () => {
        history.push('/Home');
    };

    render(){
        return (
            <div id="headerContainer">
            <div className="header-row">
                <div  className="header-button-holder"> <button className="header-button" onClick={() => this.onInputClick()} id={this.state.buttonDisplayNames[0]} >{this.state.buttonDisplayNames[0]}</button> </div>
                <div  className="header-button-holder"> <button className="header-button" onClick={() =>this. goToGames()} id={this.state.buttonDisplayNames[1]} >{this.state.buttonDisplayNames[1]}</button> </div>
                <div  className="header-button-holder"> <button className="header-button" onClick={() => this.goToPlayers()} id={this.state.buttonDisplayNames[2]} >{this.state.buttonDisplayNames[2]}</button> </div>
                <div className="header-button-holder"><img src={require('../images/paddle.png')} onClick={() => this.goToHomePage()}/></div>
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








