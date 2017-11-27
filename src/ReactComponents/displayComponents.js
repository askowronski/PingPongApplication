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
    Text, Flex, Container, Border, NavLink, Toolbar, Button, Image
} from 'rebass'
import moment from "moment";
const React = require('react');
const jQuery = require('jquery');
var _ = require('lodash');
const css = require("css-loader");
require("../stylesheet.css");
var Router = require('react-router').Router;

class InfoDisplayPlayer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: "",
            username: "",
            id: "",
            streak: "",
            firstName: '',
            lastName: '',
            loading:props.loading
        };
    }

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetLongestStreak",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                this.setState({
                    data: data.message,
                    username: JSON.parse(data.message).player.username,
                    firstName: JSON.parse(data.message).player.firstName,
                    lastName: JSON.parse(data.message).player.lastName,
                    id: JSON.parse(data.message).player.id,
                    streak: JSON.parse(data.message).streak,
                    loading:false
                });
                this.props.changeLoading();
            }.bind(this)
        });

    };

    render() {
        if (this.state.loading) {
            return <div><span>im loading</span></div>
        } else
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
    constructor(props) {
        super(props);
        this.state = {
            data: "",
            player1Username: "",
            id: "",
            player2Username: "",
            score1: "",
            score2: "",
            time: '',
            loading:props.loading
        };

    }

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetLastGame?",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                if (data.success) {
                    this.setState({
                        data: data.message,
                        player1Username: JSON.parse(
                            data.message).player1.username,
                        player2Username: JSON.parse(
                            data.message).player2.username,
                        id: JSON.parse(data.message).iD,
                        score1: JSON.parse(data.message).score1,
                        score2: JSON.parse(data.message).score2,
                        time: moment(
                            JSON.parse(data.message).timeString).format(
                            'MMMM Do YYYY, h:mm:ss a'),
                        loading: false
                    });
                }
                else {
                    this.setState({
                        loading: false,
                        data:data.message
                    })
                }
                this.props.changeLoading();
            }.bind(this)
        });

    };

    render() {
        if (this.state.loading) {
            return <div><span>im loading</span></div>
        } else
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
    constructor(props) {
        super(props);
        this.state = {
            data: "",
            id: "",
            username: "",
            eloRating: "",
            firstName: '',
            lastName: '',
            loading:props.loading
        };


    }

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayerWithHighestRating",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                if (data.success) {
                    this.setState({
                        data: data.message,
                        id: JSON.parse(data.message).id,
                        username: JSON.parse(data.message).username,
                        firstName: JSON.parse(data.message).firstName,
                        lastName: JSON.parse(data.message).lastName,
                        eloRating: JSON.parse(data.message).eloRating.rating,
                        loading: false
                    });
                } else {
                    this.setState({
                        data:data.message
                    })
                }
                this.props.changeLoading();
            }.bind(this)
        });

    };

    render() {
        if (this.state.loading) {
            return <div><span>im loading</span></div>
        } else
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
    constructor(props) {
        super(props);
        this.state = {
            data: "",
            totalGamesPlayed: "",
            stdDevForLosses: "",
            stdDevForGames: "",
            averageEloRating: "",
            loading:props.loading

        };


    }

    componentDidMount = () => {
        this.setState({ fetchInProgress: true });

        jQuery.ajax({

            url: "http://localhost:8080/TotalGameStats",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                if(data.success) {
                    this.setState({
                        data: data.message,
                        totalGamesPlayed: JSON.parse(data.message).totalGames,
                        stdDevForGames: JSON.parse(data.message).stdDevForGames,
                        stdDevForLosses: JSON.parse(
                            data.message).stdDevForLosses,
                        averageEloRating: JSON.parse(
                            data.message).averageEloRating,
                        fetchInProgress: false,
                        loading: false
                    });
                } else {
                    this.setState({
                      data:data.message
                    })
                }
                this.props.changeLoading();
            }.bind(this)
        });

    };

    render() {
        if (this.state.loading) {
            return <div><span>im loading</span></div>
        } else
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

export class InfoDisplayTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loadingPlayer:true,
            loadingGame:true,
            loadingHighestRating:true,
            loadingTotalStats:true,
        };
    }

    changeLoadingPlayer = () => {
       this.setState({
           loadingPlayer:false
       });

        if (!this.state.loadingGame && !this.state.loadingHighestRating
        && !this.state.loadingTotalStats) {
            this.props.changeLoadingState();
        }
    };

    changeLoadingGame = () => {
        this.setState({
            loadingPlayer:false
        });


        if (!this.state.loadingPlayer && !this.state.loadingHighestRating
            && !this.state.loadingTotalStats) {
            this.props.changeLoadingState();
        }
    };

    changeLoadingHighestRating = () => {
        this.setState({
            loadingPlayer:false
        });

        if (!this.state.loadingPlayer && !this.state.loadingGame
            && !this.state.loadingTotalStats) {
            this.props.changeLoadingState();
        }
    };

    changeLoadingTotalStats = () => {
        this.setState({
            loadingPlayer:false
        });

            this.props.changeLoadingState();
    };

    render() {
        return (
            <div id="homePageTableContainer" className="displayTable">
                <table className="homePageTable">
                    <tbody className="homePageBody">
                    <tr id="infoDisplay" className="infoDisplay">
                        <td className="homePageTd"><InfoDisplayPlayer changeLoading={this.changeLoadingPlayer}/></td>
                        <td className="homePageTd"><InfoDisplayGame changeLoading={this.changeLoadingGame}/></td>
                    </tr>
                    <tr id="infoDisplay" className="infoDisplay">
                        <td className="homePageTd"><InfoDisplayHighestRating changeLoading={this.changeLoadingHighestRating}/>
                        </td>
                        <td className="homePageTd"><InfoDisplayTotalGameStats changeLoading={this.changeLoadingTotalStats}/>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        );
        }

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
            selectedButton:this.props.selectedButton,
            secondarySelected: this.props.secondarySelected
        };
    }


    componentDidMount = () => {
        toggleHeaderButton(this.state.selectedButton, this.state.secondarySelected);
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

    goToFeedback = () => {
        history.push('/Feedback');
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
                        <NavLink ml='auto' f={24}
                                 onClick={this.goToFeedback}
                                 id = "feedbackButtonTab">
                            Feedback
                        </NavLink>
                        <NavLink
                                 onClick={this.goToFeedback}
                                 id = "feedbackButtonTab">
                                  <img src={require('../images/backstopPingPonglogo3.png')} width="60px" />

                        </NavLink>

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
        let secondarySelected = '';
        if (props.selectedButton === "inputButton") {
            secondarySelected = props.secondarySelected;
        }
        super(props);
        this.state = {
            showInputButtons: false,
            selectedButton:this.props.selectedButton,
            secondarySelected:secondarySelected
        };
    }


    onInputClick = () => {
        this.setState(prevState => ({
            showInputButtons: !prevState
        }));
    };

    render() {
        return (

            <div>
                <HeaderButtons2 selectedButton={this.props.selectedButton} secondarySelected = {this.state.secondarySelected}/>
            </div>
        )
    };
}

export const toggleHeaderButton = (buttonName,secondarySelected) => {
    let id = buttonName+'Tab';
    jQuery('#'+id).css('background-color','#1007a5');

    if(buttonName === "inputButton") {
        let id2 = secondarySelected+'Button';
        jQuery('#'+id2).css('background-color','#1007a5')
    }
};








