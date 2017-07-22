import history from './history';
import Game from './Pages/HomePage.js';
import CreatePlayer from './Pages/CreatePlayer.js';
import CreateGame from './Pages/CreateGame.js';
import PlayersList from './Pages/Players.js';
import Games from './Pages/Games.js';
import {Header} from './ReactComponents/displayComponents';

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


class App extends React.Component {
    render(){
        return(
            <div>
                <Game />
            </div>
        );

    }
}

class InputPlayer extends React.Component {
    render(){
        return(
            <div>
                <CreatePlayer />
            </div>
        );

    }
}

class InputGame extends React.Component {
    render(){
        return(
            <div>
                <CreateGame />
            </div>
        );

    }
}

class PlayersPage extends React.Component {
    state = {
      showEdit:false,
        showTable:true
    };

    render() {
        return(
            <div>
                <div>
                    <Header />
                </div>
                <div>
                    <PlayersList showEdit = {this.state.showEdit} showTable = {this.state.showTable} />
                </div>
            </div>
        )
    }
}

class GamesPage extends React.Component {

    render() {
        return(
            <div>
                <div>
                    <Header/>
                </div>
                <div>
                    <Games />
                </div>
            </div>
        )
    }
}




ReactDOM.render(
    <Router history={history}>
        <div>
            <Route path="/Home" component={App} />
            <Route path="/CreatePlayer" component={InputPlayer} />
            <Route path="/CreateGame" component={InputGame} />
            <Route path="/Players" component={PlayersPage} />
            <Route path="/Games" component={GamesPage} />
        </div>

    </Router>,
    document.getElementById('root')
);

