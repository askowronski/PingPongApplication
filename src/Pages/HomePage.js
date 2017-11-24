import {Header,InfoDisplayTable} from '../ReactComponents/displayComponents.js';


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

        this.setState({
            loading:false
        });

    };



    render(){

        return(
            <div>
                <Header selectedButton="totalStatsButton"/>
                <br/>

                {this.state.loading ? <div><span>loading</span> </div> :<div>
                    <InfoDisplayTable loading={this.state.loading}/>
                </div> }
            </div>
        );

    }
}

