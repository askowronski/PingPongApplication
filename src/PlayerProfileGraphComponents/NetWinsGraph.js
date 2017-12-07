import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Area,
    AreaChart,
    Tooltip,
    BarChart,
    Bar,
    Legend,
    ReferenceLine,
    ComposedChart
} from 'recharts';
import ToggleDisplay from 'react-toggle-display';
import {CustomToolTipDisplayNet} from "../Pages/PlayerProfilePage";
import moment from "moment";
import {ParseApiMessage} from "./EloRatingGraph";
// import '../index.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../main/css/index.css");
const fusioncharts = require('fusioncharts');
const charts = require('fusioncharts/fusioncharts.charts');

export class NetWinsGraph extends React.Component {
    state = {
        dataset: [],
        result: '',
        games: [],
        showBar: true,
        buttonValue: "Show Line Chart",
        playerID: 0,
        startDate: '',
        endDate: '',
        errorMessage: '',
        displayError: false,
        showGraph:false,
        showGif:true
    };

    returnData = () => {
        return this.state.dataset
    };

    componentWillReceiveProps = (nextProps) => {
        const playerID = nextProps.playerID;
        if (playerID !== this.props) {
            this.setState({
                playerID: playerID,
                startDate: nextProps.startDate,
                endDate: nextProps.endDate,
                showGif:true
            });
            let timeString = "";
            if (nextProps.startDate instanceof moment && nextProps.endDate
                instanceof moment) {
                timeString = "&beginningTime=" + moment(
                        nextProps.startDate).format("YYYYMMMDD") +
                    "&endingTime=" + moment(nextProps.endDate).format(
                        "YYYYMMMDD");
            }
            jQuery.ajax({

                url: "/GetPlayerOutcomes?id=" + playerID
                + timeString,
                type: "GET",
                dataType: "json",
                async: true,
                success: function(data) {
                    if (data.success === false) {
                        this.handleFailure(data.message)
                        this.setState({
                            showGraph:false
                        });
                    } else {
                        this.setupGraphDataSoPlayerIsAlwaysFirst(data.message);
                        this.setState({
                            // dataset: ParseApiMessage(data),
                            result: data.success,
                            displayError: false,
                            errorMessage: '',
                            showGraph:true,
                        });
                    }
                }.bind(this)
            });
        }
    };

    handleFailure = (message) => {
        this.setState({
            errorMessage: message,
            displayError: true,
            dataset: [],
            showGif:false
        });
    };

    returnYLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="left" className="XAxisLabel">
                Net</text>
        )
    };

    returnXLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="center" className="XAxisLabel">
                Game</text>
        )
    };

    returnBar = (state) => {
        if (state === true) {
            return <Bar dataKey="wins" barSize={60} fill="#8884d8"/>
        }
        return <Line dataKey="wins" fill="#8884d8"/>

    };

    toggleBarLine = () => {
        let check = !this.state.showBar;
        let buttonVal = "Show Bar Chart";
        if (check) {
            buttonVal = "Show Line Chart";
        }
        this.setState({
            showBar: check,
            buttonValue: buttonVal
        })
    };

    returnWidth = () => {
        return jQuery('#infoDisplay').width() * .72;
    };

    returnStartTooltip = () => {
        return jQuery('#infoDisplay').width() * .76;
    };

    setupGraphDataSoPlayerIsAlwaysFirst = (graphData) => {
        let theFirstPlayerId = this.state.playerID;
        let player1Username = '';
        let player2Username = '';
        let player1Score = '';
        let player2Score = '';
        let player2Id = 0;
        let parsedData = JSON.parse(graphData);
        debugger;
        for (let i = 0; i < parsedData.length; i++) {

            if (parsedData[i].game.player1ID !== theFirstPlayerId) {
                player2Id = parsedData[i].game.player1ID;
                player2Score = parsedData[i].game.score1;
                player2Username = parsedData[i].player1Username;
                player1Username = parsedData[i].player2Username;
                player1Score = parsedData[i].game.score2;
                parsedData[i].game.player1ID = theFirstPlayerId;
                parsedData[i].game.player2ID = player2Id;
                parsedData[i].game.score1 = player1Score;
                parsedData[i].game.score2 = player2Score;
                parsedData[i].player1Username = player1Username;
                parsedData[i].player2Username = player2Username;
            }
        }
        this.setState({
            dataset:parsedData
        });

    };

    render() {
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                    <span ><ToggleDisplay show={this.state.displayError}><text
                        className="errorMessageGraph">{this.state.errorMessage}</text></ToggleDisplay><text
                        className="graphHeaderText">Net Wins</text></span>

                    <ToggleDisplay show={!this.state.showGraph}>
                        {
                            this.state.showGif ? <p id="loadingSpinner" style={{'text-align': 'center'}}>
                                <img src={require('../images/Spinner.gif')}
                                     width='45%' height='45%'/></p> :
                                <p id="loadingSpinner" style={{'text-align': 'center'}}>
                                    <img src={require('../images/paddle.png')}
                                         width='45%' height='45%'/></p>
                        }
                    </ToggleDisplay>
                    <ToggleDisplay show={this.state.showGraph}>
                    <ComposedChart width={this.returnWidth()} height={400}
                                   data={this.state.dataset}
                                   margin={{
                                       top: 5,
                                       right: 30,
                                       left: 20,
                                       bottom: 5
                                   }}
                                   label={"Net Wins/Losses"}>
                        <ReferenceLine y={0} stroke='#000'/>
                        <XAxis allowDecimals={false} dataKey="label"
                               label={this.returnXLabel(500, 400)}
                               domain={[0, 'auto']}
                               padding={{bottom: 50, right: 10}}
                               labelStyle={{paddingTop: 20, color: '#32CD32'}}
                        />
                        <YAxis allowDecimals={false} domain={['auto', 'auto']}
                               label={this.returnYLabel(20, 150)}/>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <div className="legendWinsLosses">
                            <Legend />
                        </div>
                        {this.returnBar(this.state.showBar)}
                        <Tooltip position={{x: this.returnStartTooltip(), y: 0}}
                                 content={<CustomToolTipDisplayNet/>}/>
                    </ComposedChart>
                    <div className="netWinsToggleButtons">
                        <button className="graphButton"
                                onClick={() => this.toggleBarLine()}>{this.state.buttonValue}</button>
                    </div>
                    </ToggleDisplay>
                </div>


            </div>
        )
    }
}