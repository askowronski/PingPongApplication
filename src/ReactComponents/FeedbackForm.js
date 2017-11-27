import {EditUsernameSelect} from "../Pages/Games";
import {Border, Box, Button, Flex, Provider, Text} from "rebass";

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");
var TextArea = require('react-text-input').TextArea;
var Input = require('react-text-input').Input;

export class FeedbackForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            players: [],
            feedBackPlayer: null,
            feedBackText:'',
            showThanks:false

        };

        this.handleChangePlayer = this.handleChangePlayer.bind(this);
        this.handleChangeText = this.handleChangeText.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                if (data.success) {
                    this.setState({
                        players: JSON.parse(data.message),
                        result: data.success,
                    });
                } else {
                    alert(data.message);
                }
            }.bind(this)
        });

    };

    handleChangePlayer(event) {
        this.setState({feedBackPlayer: event});
    }

    handleChangeText(event) {
        this.setState({feedBackText: event.target.value});
    }

    handleSubmit(event) {
        if (this.state.feedBackPlayer === null) {
            alert("Who are ya?!");
        } else
        jQuery.ajax({
            url: "http://localhost:8080/CreateFeedback",
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify({player_Id:this.state.feedBackPlayer.id, feedback_text:this.state.feedBackText}),
            async: true,
            success: function(data) {

                    this.setState({
                        showThanks:true
                    });
                    jQuery('.feedBackTextArea').val('');

            }.bind(this),
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }.bind(this)
        });


    }

    render() {
        return (
            <div className="feedBackContainer">
            <div className="feedBackForm">
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
                                <em >Who are ya?</em>
                            </Text>
                        </Box>

                        <Box px={2} py={2} width={1 / 2}
                             className="createGameFormText">
                                <EditUsernameSelect
                                    id="player1IDInput"
                                    players={this.state.players}
                                    onOptionSelected={(event) => this.handleChangePlayer(event)}
                                    currentPlayer={this.state.feedBackPlayer}
                                    className="feedBackSelect"
                                />
                        </Box>

                        <Box px={2} py={2} width={1 / 2}
                             className="createGameFormText">
                            <Text p={1} color='black' bg='white'
                                  f={35}
                                  font-family="Serif"
                            >
                                <em >Your thoughts?</em>
                            </Text>
                        </Box>

                        <Box px={2} py={2} width={1 / 2}
                             className="createGameFormText">
                            <div className="feedBackTextInput">
                            <textarea type="text" value={this.state.feedBackText}
                                   onChange={(event) => this.handleChangeText(event)}
                            className="feedBackTextArea"/>
                            </div>
                        </Box>

                        <Box px={2} py={2} width={1}
                             className="createGameFormText">
                            <Button onClick={(event) => this.handleSubmit(event)}>
                                Submit
                            </Button>
                        </Box>
                    </Flex>
                </Border>
                </Provider>
            </div>
            <div className="createGameDisplay">
                {
                    this.state.showThanks ? <span className="feedBackThanks">Thanks {this.state.feedBackPlayer.firstName}!</span> : ''
                }
                </div>
            </div>
        );
    }
}
