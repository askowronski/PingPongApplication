import {Header,InfoDisplayTable} from '../ReactComponents/displayComponents.js';


const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");




export default class Home extends React.Component {
    state = {
        buttonNames:["InputPlayer","Scores","Players"]
    };



    render(){

        return(
            <div>
                <Header/>
                <br/>
                <div>
                    <InfoDisplayTable/>
                </div>
            </div>
        );

    }
}

