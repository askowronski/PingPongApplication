import ToggleDisplay from 'react-toggle-display';
import history from '../history.js';
import {
    Card,
    Box,
    BackgroundImage,
    Subhead,
    Small,
    Row,
    Column,
    Provider,
    Text, Flex, Container, Border, NavLink, Toolbar, Button
} from 'rebass'
import moment from "moment";
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
        streak: "",
        firstName: '',
        lastName: ''
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetLongestStreak",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    data: data.message,
                    username: JSON.parse(data.message).player.username,
                    firstName: JSON.parse(data.message).player.firstName,
                    lastName: JSON.parse(data.message).player.lastName,
                    id: JSON.parse(data.message).player.id,
                    streak: JSON.parse(data.message).streak
                });
            }.bind(this)
        });

    };

    render() {
        return (

            <div className="infoDisplay">
                <Provider
                    theme={{
                        font: '"Serif"',
                    }}
                >
                    <Flex wrap mx={-2}>

                        <Border borderWidth={4} width={1} color="black">
                            <Box px={2} py={2} width={1}>
                                <Text p={1} color='white' bg='blue'
                                      f={35}
                                      font-family="Serif"

                                >
                                    <em > Longest Streak </em>
                                </Text>

                            </Box>
                        </Border>


                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}
                                  underline>
                                <em ><u> Username: </u> </em>
                            </Text>

                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> {this.state.username}</em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em ><u> Name: </u> </em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.firstName + " "
                                + this.state.lastName}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em ><u> Streak: </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em>{this.state.streak}</em>
                            </Text>
                        </Box>
                    </Flex>
                </Provider>

            </div>

        );
    }
}

class InfoDisplayGame extends React.Component {

    state = {
        data: "",
        player1Username: "",
        id: "",
        player2Username: "",
        score1: "",
        score2: "",
        time: ''
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetLastGame?",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    data: data.message,
                    player1Username: JSON.parse(data.message).player1.username,
                    player2Username: JSON.parse(data.message).player2.username,
                    id: JSON.parse(data.message).iD,
                    score1: JSON.parse(data.message).score1,
                    score2: JSON.parse(data.message).score2,
                    time: moment(JSON.parse(data.message).timeString).format(
                        'MMMM Do YYYY, h:mm:ss a')
                });
            }.bind(this)
        });

    };

    render() {
        return (

            <div className="infoDisplay">

                <Provider
                    theme={{
                        font: '"Serif"',
                    }}
                >
                    <Flex wrap mx={-2}>
                        <Border borderWidth={4} width={1} color="black">

                            <Box px={2} py={2} width={1}>
                                <Text p={1} color='white' bg='blue'
                                      f={35}
                                      font-family="Serif"
                                >
                                    <em > Last Game Played </em>
                                </Text>
                            </Box>
                        </Border>


                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u>  {this.state.player1Username} </u>
                                </em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u> {this.state.player2Username} </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={0} width={1}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em>vs.</em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.score1}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.score2}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em>{this.state.time}</em>
                            </Text>
                        </Box>
                    </Flex>
                </Provider>

            </div>



        );
    }
}

class InfoDisplayHighestRating extends React.Component {

    state = {
        data: "",
        id: "",
        username: "",
        eloRating: "",
        firstName: '',
        lastName: ''
    };

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayerWithHighestRating",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    data: data.message,
                    id: JSON.parse(data.message).id,
                    username: JSON.parse(data.message).username,
                    firstName: JSON.parse(data.message).firstName,
                    lastName: JSON.parse(data.message).lastName,
                    eloRating: JSON.parse(data.message).eloRating.rating,
                });
            }.bind(this)
        });

    };

    render() {
        return (

            <div className="infoDisplay">
                <Provider
                    theme={{
                        font: '"Serif"',
                    }}
                >
                    <Flex wrap mx={-2}>
                        <Border borderWidth={4} width={1} color="black">

                            <Box px={2} py={2} width={1}>
                                <Text p={1} color='white' bg='blue'
                                      f={35}
                                      font-family="Serif"
                                >
                                    <em > Player With Highest Elo Rating </em>
                                </Text>
                            </Box>
                        </Border>


                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em ><u> Username: </u> </em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> {this.state.username}</em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em ><u> Name: </u> </em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.firstName + " "
                                + this.state.lastName}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em ><u> Rating: </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {parseFloat(
                                    this.state.eloRating).toFixed(2)}
                            </Text>
                        </Box>
                    </Flex>
                </Provider>
            </div>

        );
    }
}

class InfoDisplayTotalGameStats extends React.Component {

    state = {
        data: "",
        totalGamesPlayed: "",
        stdDevForLosses: "",
        stdDevForGames: "",
        averageEloRating: "",

    };

    componentDidMount = () => {
        this.setState({ fetchInProgress: true });

        jQuery.ajax({

            url: "http://localhost:8080/TotalGameStats",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    data: data.message,
                    totalGamesPlayed: JSON.parse(data.message).totalGames,
                    stdDevForGames: JSON.parse(data.message).stdDevForGames,
                    stdDevForLosses: JSON.parse(data.message).stdDevForLosses,
                    averageEloRating: JSON.parse(data.message).averageEloRating,
                    fetchInProgress: false
                });
            }.bind(this)
        });

    };

    render() {
        return (

            <div className="infoDisplay">
                <Provider

                    theme={{
                        font: '"Serif"',
                    }}
                >
                    <Flex wrap mx={-2}>
                        <Border borderWidth={4} width={1} color="black">
                            <Box px={2} py={2} width={1}
                            >
                                <Text p={1} color='white' bg='blue'
                                      f={35}
                                      font-family="Serif"
                                >
                                    <em > Total Game Stats </em>
                                </Text>
                            </Box>
                        </Border>
                        <Box pl={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u> Total Games Played: </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.totalGamesPlayed}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u> Std Dev For Games: </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.stdDevForGames}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u> Std Dev For Losses: </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.stdDevForLosses}
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                <em> <u> Average Elo Rating: </u></em>
                            </Text>
                        </Box>
                        <Box px={2} py={2} width={1 / 2}>
                            <Text p={1} color='black' bg='white'
                                  f={30}>
                                {this.state.averageEloRating}
                            </Text>
                        </Box>
                    </Flex>
                </Provider>

            </div>

        );
    }
}

export const InfoDisplayTable = (props) => {
    return (
        <div id="homePageTableContainer" className="displayTable">
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
        <div className="header-button-input-holder">
            <Provider>
                <Flex>
                    <Box>
                        <Button className="header-button-input"
                                onClick={inputPlayer} id="playerInputButton"
                                bg="blue">
                            Player
                        </Button>
                    </Box>
                    <Box>
                        <Button className="header-button-input"
                                onClick={inputGame} id="gameInputButton">Game
                        </Button>
                    </Box>
                </Flex>
            </Provider>
        </div>

    );
};

export class HeaderButtons extends React.Component {
    state = {
        buttonDisplayNames: ["Input", "Games", "Players"],
        showInputButtons: false
    };

    goToPlayers = () => {
        history.push('/Players');

    };

    goToGames = () => {
        history.push('/Games');
    };

    onInputClick = () => {
        this.setState({
            showInputButtons: !this.state.showInputButtons
        });

        if (jQuery('#playerInputButton').css('visibility') === "hidden") {
            jQuery('#playerInputButton').css('visibility', 'visible');
            jQuery('#gameInputButton').css('visibility', 'visible');
        } else {
            jQuery('#playerInputButton').css('visibility', 'hidden');
            jQuery('#gameInputButton').css('visibility', 'hidden');
        }
    };

    goToHomePage = () => {
        history.push('/TotalStats');
    };

    render() {
        return (
            <div id="headerContainer">
                <div className="header-row">
                    <div className="header-button-holder">
                        <button className="header-button"
                                onClick={() => this.onInputClick()}
                                id={this.state.buttonDisplayNames[0]}>{this.state.buttonDisplayNames[0]}</button>
                    </div>
                    <div className="header-button-holder">
                        <button className="header-button"
                                onClick={() => this.goToGames()}
                                id={this.state.buttonDisplayNames[1]}>{this.state.buttonDisplayNames[1]}</button>
                    </div>
                    <div className="header-button-holder">
                        <button className="header-button"
                                onClick={() => this.goToPlayers()}
                                id={this.state.buttonDisplayNames[2]}>{this.state.buttonDisplayNames[2]}</button>
                    </div>
                    <div className="header-button-holder"><img
                        src={require('../images/paddle.png')}
                        onClick={() => this.goToHomePage()}/></div>
                </div>
                <div id="inputButtonContainer">
                    <InputButtons
                        showInputButtons={this.state.showInputButtons}/>
                </div>
            </div>
        );
    }
}

export class HeaderButtons2 extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            buttonDisplayNames: ["Input", "Games", "Players", "Total Stats"],
            showInputButtons: false,
            selected:props.selected,
            selectedButton:this.props.selectedButton
        };
    }


    componentDidMount = () => {
        toggleHeaderButton(this.state.selectedButton);
        debugger;
    };


    showInputButtons = () => {
        this.setState({
            showInputButtons: !this.state.showInputButtons
        });

        if (jQuery('#playerInputButton').css('visibility') === "hidden") {

            let position = jQuery('.inputButton').position().left;
            let width = jQuery('.inputButton').width();
            let positionPlayer = position + width / 2.0;
            let positionGame = positionPlayer - jQuery(
                    '#gameInputButton').width();
            jQuery('#playerInputButton').css('left', positionPlayer);
            jQuery('#gameInputButton').css('left', positionGame);
            jQuery('#playerInputButton').css('visibility', 'visible');
            jQuery('#gameInputButton').css('visibility', 'visible');

        } else {
            jQuery('#playerInputButton').css('visibility', 'hidden');
            jQuery('#gameInputButton').css('visibility', 'hidden');
        }
    };

    goToPlayers = () => {
        history.push('/Players');

    };

    goToGames = () => {
        history.push('/Games');
    };

    goToTotalStats = () => {
        history.push('/TotalStats');
    };

    inputPlayer = () => {
        history.push('/CreatPlayer');
    };

    goToIndividualStats = () => {
        const location = {
            pathname: '/PlayerProfile',
            state: {
                player: {
                    id: 0
                }
            }
        };
        history.push(location);

    };

    inputPlayer = () => {
        history.push('/CreatePlayer');
    };

    inputGame = () => {
        history.push('/CreateGame');
    };

    render() {
        return (
            <div>
                <Provider
                    theme={{
                        font: '"Serif"',

                    }}
                >
                    <Toolbar bg="black">
                        <Button ml={40} mr={20} f={23}
                                onClick={this.showInputButtons}
                                id = "inputButtonTab"
                        >
                            <span className="inputButton" > Input </span>
                        </Button>
                        <Button mx={20} f={24}
                                onClick={this.goToGames}
                                id = "gamesButtonTab">
                            Games
                        </Button >
                        <Button mx={20} f={24} onClick={this.goToPlayers}
                                id = "playersButtonTab">
                            Players
                        </Button>
                        <Button mx={20} f={24} onClick={this.goToTotalStats}
                                id = "totalStatsButtonTab">
                            Total Stats
                        </Button>
                        <Button mx={20} f={24}
                                onClick={this.goToIndividualStats}
                                id = "individualStatsButtonTab">
                            Individual Stats
                        </Button>
                    </Toolbar>
                </Provider>
                <InputButtons
                    showInputButtons={this.state.showInputButtons}/>
            </div>
    );
    }
}

export class Header extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            showInputButtons: false,
            selectedButton:this.props.selectedButton
        };
    }


    onInputClick = () => {
        this.setState(prevState => ({
            showInputButtons: !prevState
        }));
    };

    render() {
        const {showInputButtons} = this.state;

        return (

            <div>
                <HeaderButtons2 selectedButton={this.props.selectedButton}/>
            </div>
        )
    };
}

export const toggleHeaderButton = (buttonName) => {
    debugger;
    let id = buttonName+'Tab';
    jQuery('#'+id).css('background-color','#1007a5')
};








