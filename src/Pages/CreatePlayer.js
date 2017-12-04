import {Header} from '../ReactComponents/displayComponents.js';
import {Border, Box, Button, Flex, Provider, Text} from "rebass";
import {ResultText} from "./CreateGame";
// import '../index.css';
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../main/css/index.css");

class CreatePlayerForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '',
            result: '',
            data: '',
            firstName: '',
            lastName: '',
            player: {
                username: '',
                firstName: '',
                lastName: '',
                eloRating: {
                    rating: ''
                }

            },
            ratingText:''
        };

        this.handleChangeUsername = this.handleChangeUsername.bind(this);
        this.handleChangeFirstName = this.handleChangeFirstName.bind(this);
        this.handleChangeLastName = this.handleChangeLastName.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChangeUsername(event) {
        this.setState({
            value: event.target.value
        });
    }

    handleChangeFirstName(event) {
        this.setState({
            firstName: event.target.value
        });
    }

    handleChangeLastName(event) {
        this.setState({
            lastName: event.target.value
        });
    }

    clearInputs = () => {

    };

    handleSubmit(event) {
        console.log(this.state.value);

        jQuery.ajax({

            url: "/CreatePlayer?username="
            + this.state.value
            + "&firstName=" + this.state.firstName + "&lastName="
            + this.state.lastName,
            type: "POST",
            dataType: "json",
            async: true,
            success: function(data) {
                if (data.success) {
                    this.setState({
                        data: data.message,
                        result: data.success,
                        player: {
                            username: this.state.value,
                            firstName: this.state.firstName,
                            lastName: this.state.lastName,
                            eloRating: {
                                rating: 1500.00
                            }
                        },
                        value: '',
                        firstName: '',
                        lastName: '',
                        ratingText: 'Rating:'
                    });
                } else {
                    this.setState({
                        data: data.message,
                        result: data.success,
                        player: {
                            username: '',
                            firstName: '',
                            lastName: '',
                            eloRating: {
                                rating: ''
                            }
                        },
                        ratingText: ''
                    });
                }
            }.bind(this)
        });

    };

    render() {
        return (
            <div>
                <div className="createPlayerFormCont">
                    <Provider
                        theme={{
                            font: '"Serif"',
                        }}
                    > <Border borderWidth={4}>

                        <Flex wrap mx={50}>
                            <Box px={2} py={2} width={1 / 2}
                                 className="createGameFormText">
                                <Text p={1} color='black' bg='white'
                                      f={35}
                                      font-family="Serif"
                                >
                                    <em > Username:</em>
                                </Text>
                            </Box>


                            <Box px={2} pb={3} pt={2} width={1 / 2}>
                                <div className="Select-control-wrapper-player">
                                    <input type="text" value={this.state.value}
                                           onChange={this.handleChangeUsername}
                                           className="createPlayerInput"/>
                                </div>
                            </Box>

                            <Box px={2} py={2} width={1 / 2}
                                 className="createGameFormText">
                                <Text p={1} color='black' bg='white'
                                      f={35}
                                      font-family="Serif"
                                >
                                    <em > First Name:</em>
                                </Text>
                            </Box>


                            <Box px={2} py={2} width={1 / 2}>
                                <div className="Select-control-wrapper-player">
                                    <input type="text"
                                           value={this.state.firstName}
                                           onChange={this.handleChangeFirstName}
                                           className="createPlayerInput"/>
                                </div>
                            </Box>
                            <Box px={2} py={2} width={1 / 2}
                                 className="createGameFormText">
                                <Text p={1} color='black' bg='white'
                                      f={35}
                                      font-family="Serif"
                                >
                                    <em > Last Name:</em>
                                </Text>
                            </Box>


                            <Box px={2} py={2} width={1 / 2}>
                                <div className="Select-control-wrapper-player">
                                    <input type="text"
                                           value={this.state.lastName}
                                           onChange={this.handleChangeLastName}
                                           className="createPlayerInput"/>
                                </div>
                            </Box>

                            <Box width={1} py={2}
                                 className="createGameFormText">
                                <Button onClick={this.handleSubmit}>
                                    Submit
                                </Button>

                            </Box>
                        </Flex>
                    </Border>
                    </Provider>

                </div>
                <div className="createGameDisplay">
                    <PlayerView player={this.state.player}
                                resultText={this.state.data}
                    ratingText={this.state.ratingText}/>
                </div>
            </div>

        );
    }
}

export default class CreatePlayer extends React.Component {
    state = {
        buttonNames: ["InputPlayer", "Scores", "Players"]
    };

    render() {

        return (
            <div>
                <div>
                    <Header id="header" className="header"
                            selectedButton="inputButton"
                            secondarySelected="playerInput"/>
                </div>
                <br/>
                <div id="PlayerForm" className="CreatePlayerForm">
                    <CreatePlayerForm/>
                </div>
            </div>
        );

    }
}

class PlayerView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            player: props.player,
            ratingText: ''
        };
    }

    componentWillReceiveProps = (nextProps) => {
        if (nextProps.player.eloRating.rating > 0) {
            this.setState({
                ratingText: "Rating :"
            });
            this.setState({
                player: nextProps.player
            });

        }

    };

    render() {
        return (
            <Provider
                theme={{
                    font: '"Serif"',
                }}
            >
                <Flex wrap mx={-2}>
                    <Box px={2} py={2} width={1}>
                        <ResultText text={this.props.resultText}/>
                    </Box>


                    <Box px={2} py={2} width={1}>
                        <Text p={1} color='black' bg='white'
                              f={30}>
                            <em> <u>  {this.state.player.username} </u>
                            </em>
                        </Text>
                    </Box>
                    <Box px={2} py={2} width={1}>
                        <Text p={1} color='black' bg='white'
                              f={30}>
                            {this.state.player.firstName} {this.state.player.lastName}
                        </Text>
                    </Box>
                    <Box px={2} py={2} width={1}>
                        <Text p={1} color='black' bg='white'
                              f={30}>
                            {this.props.ratingText} {this.state.player.eloRating.rating}
                        </Text>
                    </Box>

                </Flex>
            </Provider>

        );
    }
}


