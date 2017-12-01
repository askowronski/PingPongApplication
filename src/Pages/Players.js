import ToggleDisplay from 'react-toggle-display';
import {Link} from "react-router-link";
import history from '../history.js';
import '../main/css/index.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');

export default class PlayersList extends React.Component {
    state = {
        players: [],
        result: '',
        showEditPlayer: [],
        deletePlayerResponse: '',
        editUsername: '',
        editFirstName: '',
        editLastName: '',
        editId: 0,
        originalUsername:'',
        areTherePlayers:true,
    };

    componentDidMount = () => {
        jQuery('tbody.reactable-pagination tr td').addClass(
            'custom-pagination');
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type: "GET",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    players: JSON.parse(data.message),
                    result: data.success,
                });
                let showEditArray = [];
                if (JSON.parse(data.message).length == 0) {
                    this.setState({
                        areTherePlayers:false
                    })
                } else {
                    this.setState({
                        areTherePlayers:true
                    })
                }

                for (let i = 0; i < JSON.parse(data.message).length; i++) {
                    showEditArray.push(false);
                }
                this.setState({
                    showEditPlayer: showEditArray,
                });
            }.bind(this)
        });

    };

    showEditPlayer = (index, player) => {

        let showEditArray = this.state.showEditPlayer.concat();
        let indexOfTrue = showEditArray.indexOf(true);
        if (indexOfTrue >= 0) {
            showEditArray[indexOfTrue] = false;
        }
        showEditArray[index] = true;

        this.setState({
            showEditPlayer: showEditArray,
            editId: player.id,
            originalUsername:player.username,
            editUsername: player.username,
            editFirstName:player.firstName,
            editLastName:player.lastName
        });
    };

    cancelEditPlayer = (index) => {
        let showEditArray = this.state.showEditPlayer.concat();
        showEditArray[index] = false;

        this.setState({
            showEditPlayer: showEditArray,
            editUsername: null,
            editFirstName: null,
            editLastName: null,
            originalUsername:'',
            editId:0
        });
    };

    processEditPlayer = () => {

        let urlString = "http://localhost:8080/EditPlayer?id="+this.state.editId+ "&newFirstName="
            + this.state.editFirstName + "&newLastName=" + this.state.editLastName;
        if (this.state.editUsername !== this.state.originalUsername) {
            urlString = urlString + "&newUsername="+this.state.editUsername
        }
        jQuery.ajax({

            url: urlString,
            type: "POST",
            dataType: "json",
            async: true,
            success: function(data) {
                alert(data.message);
                this.componentDidMount();
            }.bind(this)
        });
    };

    processDeletePlayer = (id, username) => {
        jQuery.ajax({

            url: "http://localhost:8080/DeletePlayer?id=" + id + "&username="
            + username,
            type: "DELETE",
            dataType: "json",
            async: true,
            success: function(data) {
                alert(data.message);
                this.componentDidMount();
            }.bind(this)
        });
    };

    onChangeUsername = (event) => {
        this.setState({
            editUsername: event.target.value
        })
    };

    onChangeFirstName = (event) => {
        this.setState({
            editFirstName: event.target.value
        })
    };

    onChangeLastName = (event) => {
        this.setState({
            editLastName: event.target.value
        })
    };

    playerProfile = (player) => {
        const location = {
            pathname: '/PlayerProfile',
            state: {
                player: player
            }
        };
        history.push(location);
    };

    render() {
        let Table = Reactable.Table;
        let Tr = Reactable.Tr;
        let Td = Reactable.Td;

        let playersLength = this.state.players.length;

        return (



            <div>
                {
                    !this.state.areTherePlayers ? <div className="noDataContainer">No Players</div> :
                        <div>
                            <div>
                                <p id="loadingSpinner" style={{'text-align': 'center'}}>
                                    <img src={require('../images/Spinner.gif')}
                                         width='45%' height='45%'/></p>
                            </div>

                            <div className="tableHolder">


                                <Table className="PlayersTable" sortable={true} itemsPerPage={8}
                                       pageButtonLimit={5}>

                                    {this.state.players.map((player, i) => {
                                        if (i === playersLength - 1) {
                                            jQuery('.tableHolder').css('visibility', 'visible');
                                            jQuery('#loadingSpinner').remove();
                                        }

                                        return <Tr className="PlayersRow">

                                            <Td column="Username" className="playersTableColumn"
                                                value={player.username}>
                                                <div>
                                                    <ToggleDisplay id="usernameToggleDisplay"
                                                                   show={!this.state.showEditPlayer[i]}>
                                                        {player.username}
                                                    </ToggleDisplay>
                                                    <ToggleDisplay
                                                        id="editUsernameToggleDisplay"
                                                        show={this.state.showEditPlayer[i]}>
                                                        <input className="editUsernameInput"
                                                               type="text"
                                                               value={this.state.editUsername}
                                                               onChange={(event) => this.onChangeUsername(
                                                                   event)}
                                                        />
                                                    </ToggleDisplay>
                                                </div>
                                            </Td>
                                            <Td column="First Name"
                                                className="playersTableColumn"
                                                value={player.firstName}>
                                                <div>
                                                    <ToggleDisplay id="usernameToggleDisplay"
                                                                   show={!this.state.showEditPlayer[i]}>
                                                        {player.firstName}
                                                    </ToggleDisplay>
                                                    <ToggleDisplay
                                                        id="editUsernameToggleDisplay"
                                                        show={this.state.showEditPlayer[i]}>
                                                        <input className="editUsernameInput"
                                                               type="text"
                                                               value={this.state.editFirstName}
                                                               onChange={(event) => this.onChangeFirstName(
                                                                   event)}
                                                        />
                                                    </ToggleDisplay>
                                                </div>
                                            </Td>
                                            <Td column="Last Name"
                                                className="playersTableColumn"
                                                value={player.lastName}>
                                                <div>
                                                    <ToggleDisplay id="usernameToggleDisplay"
                                                                   show={!this.state.showEditPlayer[i]}>
                                                        {player.lastName}
                                                    </ToggleDisplay>
                                                    <ToggleDisplay
                                                        id="editUsernameToggleDisplay"
                                                        show={this.state.showEditPlayer[i]}>
                                                        <input className="editUsernameInput"
                                                               type="text"
                                                               value={this.state.editLastName}
                                                               onChange={(event) => this.onChangeLastName(
                                                                   event)}
                                                        />
                                                    </ToggleDisplay>
                                                </div>
                                            </Td>
                                            <Td column="Elo Rating"
                                                className="playersTableColumn"
                                                value={parseFloat(
                                                    player.eloRating.rating).toFixed(2)}>
                                                {parseFloat(player.eloRating.rating).toFixed(2)}
                                            </Td>
                                            <Td column="Actions" id={player.id}>
                                                <div>
                                                    <ToggleDisplay
                                                        show={this.state.showEditPlayer[i]}>
                                                        <div>
                                                            <input type="button" value="Submit"
                                                                   className="editButton"
                                                                   onClick={this.processEditPlayer}
                                                            />
                                                            &nbsp;
                                                        </div>
                                                    </ToggleDisplay>
                                                    <div className="editContainer">
                                                        <a className="editPlayer"
                                                           style={{cursor: 'pointer'}}
                                                           onClick={() => this.showEditPlayer(
                                                               i, player)}>Edit</a>
                                                    </div>
                                                    &nbsp;
                                                    <ToggleDisplay
                                                        show={this.state.showEditPlayer[i]}>
                                                        <div className="cancelContainer">
                                                            <a style={{cursor: 'pointer'}}
                                                               onClick={() => this.cancelEditPlayer(
                                                                   i)}>Cancel</a>
                                                        </div>
                                                    </ToggleDisplay>
                                                    &nbsp;
                                                    <div className="profileContainer">
                                                        <a style={{cursor: 'pointer'}}
                                                           onClick={() => this.playerProfile(
                                                               player)}>Profile</a>
                                                    </div>
                                                    &nbsp;
                                                    &nbsp;
                                                    &nbsp;
                                                    <div className="deleteContainer">
                                                        <a style={{cursor: 'pointer'}}
                                                           onClick={() => this.processDeletePlayer(
                                                               player.id, player.username)}>Delete</a>
                                                    </div>
                                                </div>
                                            </Td>
                                        </Tr>
                                    })};
                                </Table>

                            </div>
                        </div>

                }
            </div>
        );
    }
}

class EditPlayer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            newUsername: props.username,
            id: props.id,
            result: '',
            messsage: ''
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({
            newUsername: event.target.value
        });
    };

    handleSubmit(event) {
        alert(this.state.newUsername);

        jQuery.ajax({

            url: "http://localhost:8080/EditPlayer?id=" + this.state.id
            + "&newUsername=" + this.state.newUsername,
            type: "POST",
            dataType: "json",
            async: true,
            success: function(data) {
                this.setState({
                    message: data.message,
                    result: data.success,
                });
                alert(data.message);
            }.bind(this)
        });

    };

    render() {
        return (
            <div className="editUsernameContainer">
                <form className="editUsernameForm" onSubmit={this.handleSubmit}>
                    <input className="editUsernameInput" type="text"
                           value={this.state.newUsername}
                           onChange={this.handleChange}/>
                    <input type="submit" value="Submit"/>
                </form>
            </div>
        );
    }
}






