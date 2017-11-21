import history from './history';
import Home from './Pages/HomePage.js';
import CreatePlayer from './Pages/CreatePlayer.js';
import CreateGame from './Pages/CreateGame.js';
import PlayersList from './Pages/Players.js';
import Games from './Pages/Games.js';
import {Header} from './ReactComponents/displayComponents';
import {PlayerGraphTable} from './Pages/PlayerProfilePage.js';
import TotalStats from "./Pages/HomePage";

const React = require('react');
const ReactDOM = require('react-dom');
require("./stylesheet.css");
var Router = require('react-router').Router;
var Route = require('react-router').Route;


class TotalStatsPage extends React.Component {
    render(){
        return(
            <div>
                <TotalStats />
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

class PlayerProfilePage extends React.Component {
    render() {
        return(
         <div>
             <div>
                 <Header/>
             </div>
             <div>
                 <PlayerGraphTable history={history}/>
             </div>
         </div>
        )
    }
}




ReactDOM.render(
    <Router history={history}>
        <div>
            <Route path="/TotalStats" component={TotalStats} />
            <Route path="/CreatePlayer" component={InputPlayer} />
            <Route path="/CreateGame" component={InputGame} />
            <Route path="/Players" component={PlayersPage} />
            <Route path="/Games" component={GamesPage} />
            <Route path="/PlayerProfile" component={PlayerProfilePage}/>
        </div>

    </Router>,
    document.getElementById('root')
);


