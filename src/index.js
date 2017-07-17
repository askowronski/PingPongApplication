import history from './history';

const React = require('react');
const ReactDOM = require('react-dom');
const jQuery = require('jquery');
var _ = require('lodash');
const css = require("css-loader");
require("./stylesheet.css");
var FontAwesome = require('react-fontawesome');
var ReactRouter = require('react-router');
var Router = require('react-router').Router;
var Route = require('react-router').Route;





const HeaderButtons = (props) => {
    return (
        <div className="header-row">
        {props.buttonNames.map((buttonName,i) =>
            <div className="header-button-holder"> <button className="header-button" id="button-header" >{buttonName}</button> </div>
        )}
        </div>
    );
}

const Display = (props) => {

    return (
        <div>
      <span>Games Played</span>
            <span> equals + {props.gamesPlayed(4)} </span>
        </div>

    );
}

const input = (props) => {
    return (
        <input id="idInput" type='submit' >input</input>
    );
}


class IDForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '',
            userName:'huh'
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    getGamesPlayed = (id) => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayer?id="+id,
            dataType: 'json',
            cache: false,

        }).then(function (data) {
            window.alert(data.message);
            this.setState({

                userName:data.message
            })
            if(this.userName===null){
                this.setState({
                    userName: "User Not Found"
                })

            }
        }.bind(this));
    };

    handleChange(event) {
        this.setState({
            value: event.target.value
        });
    }

    handleSubmit(event) {
        console.log(this.state.value);
        {this.getGamesPlayed(this.state.value)};
    }

    render() {
        return (
            <div>
            <form onSubmit={this.handleSubmit}>
                <label>
                    Name:
                    <input type="text" value={this.state.value} onChange={this.handleChange} />
                </label>
                <input type="submit" value="Submit" />
            </form>

            <Display gamesPlayed={this.getGamesPlayed} />
            </div>
        );
    }
}

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


const InfoDisplayTable = (props) => {
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
}






class Game extends React.Component {
    state = {
        buttonNames:["Input","Scores","Players"]
    };



    render(){

        return(
            <div>
                <div>
                    <HeaderButtons buttonNames={this.state.buttonNames} id = "header-button-1" className="header-button"/>
                </div>
                    <br/>
                <div>
                    <InfoDisplayTable />
                </div>
            </div>
        );

    }
}


class App extends React.Component {
    render(){
        return(
            <div>
                <Game />
            </div>
        );

    }
}




ReactDOM.render(
    <Router history={history}>
        <Route path="/home" component={App}>

        </Route>
    </Router>,
    document.getElementById('root')
);


