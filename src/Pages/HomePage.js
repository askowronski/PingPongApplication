import {
    Header,
    InfoDisplayTable
} from '../ReactComponents/displayComponents.js';
import ToggleDisplay from 'react-toggle-display';
// import '../index.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../main/css/index.css");

export default class TotalStats extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            buttonNames: ["InputPlayer", "Scores", "Players"],
            loading: true,
            areThereGames:true,
            data:''
        };
    }

    componentDidMount = () => {

        jQuery.ajax({

            url: "http://localhost:8080/AreThereGames",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                this.setState({
                    areThereGames: data.success
                });
            }.bind(this)
        });
    };

    changeLoadingState = () => {
        this.setState(
            {
                loading: false
            }
        )
    };

    changeAreThereGamesState = () => {
        let boolean = !this.state.areThereGames;
        this.setState(
            {
                areThereGames: boolean
            }
        )
    };

    render() {

        return (
            <div>
                <Header selectedButton="totalStatsButton"/>
                <br/>
            {
                !this.state.areThereGames ? <div className="noDataContainer"> No Games </div> :

                    <div>


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
            }


            </div>
        );

    }
}

