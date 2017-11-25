import {
    Header,
    InfoDisplayTable
} from '../ReactComponents/displayComponents.js';
import ToggleDisplay from 'react-toggle-display';
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");

export default class TotalStats extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            buttonNames: ["InputPlayer", "Scores", "Players"],
            loading: true
        };
    }

    componentDidMount = () => {

    };

    changeLoadingState = () => {
        debugger;
        this.setState(
            {
                loading: false
            }
        )
    };

    render() {

        return (
            <div>
                <Header selectedButton="totalStatsButton"/>
                <br/>

                <ToggleDisplay show={this.state.loading}>
                    <div><p id="loadingSpinner"
                            style={{'text-align': 'center'}}>
                        <img src={require('../images/Spinner.gif')}
                             width='45%' height='45%'/></p></div>
                </ToggleDisplay>
                <ToggleDisplay show={!this.state.loading}>
                    <InfoDisplayTable changeLoadingState={this.changeLoadingState}/>
                </ToggleDisplay>
            </div>
        );

    }
}

