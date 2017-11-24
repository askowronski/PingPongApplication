import {Header} from '../ReactComponents/displayComponents.js';
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");

class CreatePlayerForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '',
            result: '',
            data: '',
            firstName: '',
            lastName: ''
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

    handleSubmit(event) {
        console.log(this.state.value);

        jQuery.ajax({

            url: "http://localhost:8080/CreatePlayer?username="
            + this.state.value
            + "&firstName=" + this.state.firstName + "&lastName="
            + this.state.lastName,
            type: "POST",
            dataType: "json",
            async: false,
            success: function(data) {
                this.setState({
                    data: data.message,
                    result: data.success,
                });
                alert(data.message);
            }.bind(this)
        });

    };

    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    <table className="inputPlayerTable">
                        <tbody>
                        <tr>

                            <td className="inputCell">
                                <label className="UsernameLabel">
                                    Username: &nbsp;
                                </label>
                            </td>
                            <td className="inputCell">
                                <div>
                                    <input type="text" value={this.state.value}
                                           onChange={this.handleChangeUsername}
                                           className="createPlayerInput"/>
                                </div>
                            </td>

                        </tr>
                        <tr>
                            <td className="inputCell">
                                <label className="UsernameLabel">
                                    First Name: &nbsp;
                                </label>
                            </td>
                            <td className="inputCell">
                                <div>
                                    <input type="text" value={this.state.firstName}
                                           onChange={this.handleChangeFirstName}
                                           className="createPlayerInput"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td className="inputCell">
                                <label className="UsernameLabel">
                                    Last Name: &nbsp;
                                </label>
                            </td>
                            <td className="inputCell">
                                <div>
                                    <input type="text" value={this.state.lastName}
                                           onChange={this.handleChangeLastName}
                                           className="createPlayerInput"/>
                                </div>
                            </td>
                        </tr>
                        <tr >
                            <td className="inputCell" colSpan={2}>
                                <input type="submit" value="Submit"
                                       className="createPlayerButton"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
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
                    <Header id="header" className="header" selectedButton="inputButton"/>
                </div>
                <br/>
                <div id="PlayerForm" className="CreatePlayerForm">
                    <CreatePlayerForm/>
                </div>
            </div>
        );

    }
}


