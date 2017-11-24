
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
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../stylesheet.css");
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
        errorMessage:'',
        displayError:false
    };

    returnData = () => {
        return this.state.dataset
    };

    componentWillReceiveProps = (nextProps) => {
        const playerID = nextProps.playerID;
        debugger;
        if (playerID !== this.props) {
            this.setState({
                playerID: playerID,
                startDate: nextProps.startDate,
                endDate: nextProps.endDate
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

                url: "http://localhost:8080/GetPlayerOutcomes?id=" + playerID+timeString,
                type: "GET",
                dataType: "json",
                async: false,
                success: function(data) {
                    if (data.success === false) {
                        this.handleFailure(data.message)
                    } else {
                        this.setState({
                            dataset: ParseApiMessage(data),
                            result: data.success,
                            displayError:false,
                            errorMessage:''
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
        });
    };

    returnYLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="left" className="XAxisLabel">Net</text>
        )
    };

    returnXLabel = (x, y) => {
        return (
            <text x={x} y={y} textAnchor="center" className="XAxisLabel">Game</text>
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
        debugger;
       return jQuery('#infoDisplay').width() * .72;
    };

    returnStartTooltip = () => {
        debugger;
        return jQuery('#infoDisplay').width() * .76;
    };

    render() {
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                    <span ><ToggleDisplay show={this.state.displayError}><text className = "errorMessageGraph">{this.state.errorMessage}</text></ToggleDisplay><text >Net Wins</text></span>
                    <ComposedChart width={this.returnWidth()} height={400}
                                   data={this.state.dataset}
                                   margin={{
                                       top: 5,
                                       right: 30,
                                       left: 20,
                                       bottom: 5
                                   }}
                                   label={"Net Wins/Losses"}>
                        <XAxis allowDecimals={false} dataKey="label"
                               label={this.returnXLabel(500, 400)}
                               domain={[0, 'auto']}
                               padding={{bottom: 50, right: 10}}
                               labelStyle={{paddingTop: 20, color: '#32CD32'}}
                        />
                        <YAxis allowDecimals={false} domain={[0, 'auto']}
                               label={this.returnYLabel(20, 150)}/>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <ReferenceLine y={0} stroke='#000'/>
                        <div className="legendWinsLosses">
                            <Legend />
                        </div>
                        {this.returnBar(this.state.showBar)}
                        <Tooltip position={{x:this.returnStartTooltip(), y: 0}}
                                 content={<CustomToolTipDisplayNet/>}/>
                    </ComposedChart>
                    <div className="netWinsToggleButtons">
                        <button className="graphButton"
                                onClick={() => this.toggleBarLine()}>{this.state.buttonValue}</button>
                    </div>
                </div>

            </div>
        )
    }
}