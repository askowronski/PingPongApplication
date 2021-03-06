import history from './history';
import Home from './Pages/HomePage.js';
import CreatePlayer from './Pages/CreatePlayer.js';
import CreateGame from './Pages/CreateGame.js';
import PlayersList from './Pages/Players.js';
import Games from './Pages/Games.js';
import {Header, toggleHeaderButton} from './ReactComponents/displayComponents';
import {PlayerGraphTable} from './Pages/PlayerProfilePage.js';
import TotalStats from "./Pages/HomePage";
import FeedbackPage from "./Pages/Feedback";
import LogoPage from "./Pages/LogoPage";


const React = require('react');
const ReactDOM = require('react-dom');
const css = require("css-loader");
var Router = require('react-router').Router;
var Route = require('react-router').Route;
require("./main/css/index.css");


class TotalStatsPage extends React.Component {
    render() {
        return (
            <div>
                <TotalStats />
            </div>
        );

    }
}
class InputPlayer extends React.Component {
    render() {
        return (
            <div>
                <CreatePlayer />
            </div>
        );

    }
}

class InputGame extends React.Component {
    render() {
        {
            toggleHeaderButton('inputButton')
        }

        return (
            <div>
                <CreateGame />
            </div>
        );
    }
}

class PlayersPage extends React.Component {
    state = {
        showEdit: false,
        showTable: true
    };

    render() {
        return (
            <div>
                <div>
                    <Header selectedButton="playersButton"/>
                </div>
                <div>
                    <PlayersList showEdit={this.state.showEdit}
                                 showTable={this.state.showTable}/>

                </div>
            </div>
        )
    }
}

class GamesPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: true
        }
    }

    render() {

        {
            toggleHeaderButton('gamesButton')
        }
        return (
            <div>
                <div>
                    <Header selectedButton="gamesButton"/>
                </div>
                <div>
                    <Games loading={this.state.loading}/>
                </div>
            </div>
        )
    }
}

class PlayerProfilePage extends React.Component {
    render() {

        {
            toggleHeaderButton('individualStatsButton')
        }

        return (
            <div>
                <div>
                    <Header selectedButton="individualStatsButton"/>
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
            <Route path="/TotalStats" component={TotalStats}/>
            <Route path="/CreatePlayer" component={InputPlayer}/>
            <Route path="/CreateGame" component={InputGame}/>
            <Route path="/Players" component={PlayersPage}/>
            <Route path="/Games" component={GamesPage}/>
            <Route path="/PlayerProfile" component={PlayerProfilePage}/>
            <Route path="/Feedback" component={FeedbackPage}/>
            <Route path="/Home" component={LogoPage}/>

        </div>

    </Router>,
    document.getElementById('root')
);


