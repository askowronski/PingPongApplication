import {Header} from '../ReactComponents/displayComponents.js';
import {FeedbackForm} from "../ReactComponents/FeedbackForm.js";
// import '../index.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../main/css/index.css");


export default class FeedbackPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            feedBackText:'',
            players:[],
            feedBackPlayer:{
                username:'',
                firstName:'',
                lastName:''
            }
        }
    }



    onInputChange = (input) => {
        this.setState({
            feedBackText:input
        })
    };

    onPlayerChange = (player) => {
        this.setState({
            feedBackPlayer:player
        })
    };

    render() {
        return (
          <div>
              <Header selectedButton="feedbackButton"/>

              <FeedbackForm onInputChange={this.onInputChange}/>

          </div>

        );

    }


}
