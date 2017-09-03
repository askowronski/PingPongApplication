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
            result:'',
            data:''
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }



    handleChange(event) {
        this.setState({
            value: event.target.value
        });
    }

    handleSubmit(event) {
        console.log(this.state.value);

        jQuery.ajax({

            url: "http://localhost:8080/CreatePlayer?username="+this.state.value,
            type:"POST",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    data:data.message,
                    result:data.success,
                });
                alert(data.message);
            }.bind(this)
        });

    };


    render() {
        return (
            <div>
                <form onSubmit={this.handleSubmit}>
                    <label className="UsernameLabel">
                        Username: &nbsp;
                        <input type="text" value={this.state.value} onChange={this.handleChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
            </div>
        );
    }
}

export default class CreatePlayer extends React.Component {
    state = {
        buttonNames:["InputPlayer","Scores","Players"]
    };



    render(){

        return(
            <div>
                <div>
                    <Header id = "header" className="header"/>
                </div>
                <br/>
                <div id="PlayerForm" className="CreatePlayerForm">
                    <CreatePlayerForm/>
                </div>
            </div>
        );

    }
}


