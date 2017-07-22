import {Header,InfoDisplayTable} from '../ReactComponents/displayComponents.js';


const React = require('react');
const jQuery = require('jquery');
var _ = require('lodash');
const css = require("css-loader");
require("../stylesheet.css");
var FontAwesome = require('react-fontawesome');




export default class Game extends React.Component {
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

