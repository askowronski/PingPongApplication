import ToggleDisplay from 'react-toggle-display';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Area, AreaChart, Tooltip,BarChart,Bar,Legend,ReferenceLine,ComposedChart
} from 'recharts';
import pureRender from 'react-pure-render';
import {AverageScorePerGame} from "../PlayerProfileGraphComponents/ScorePerGame/ScorePerGameGraph";
import {EloRatingPerGame} from "../PlayerProfileGraphComponents/EloRatingGraph";
import {Typeahead} from "react-typeahead";
import history from '../history.js';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");
const ReactFC = require('react-fusioncharts');
const fusioncharts = require('fusioncharts');
const charts = require('fusioncharts/fusioncharts.charts');
const {PropTypes} = React;


export class NetWinsGraph extends React.Component {
    state = {
        dataset:[],
        result:'',
        games:[],
        showBar:true,
        buttonValue: "Show Line Chart",
        playerID:0
    };


    returnData = () => {
        return this.state.dataset
    };

    componentWillReceiveProps = (nextProps) => {
        const playerID = nextProps.playerID;
        if(playerID !== this.props) {
            this.setState({
                playerID: playerID
            });
            jQuery.ajax({

                url: "http://localhost:8080/GetPlayerOutcomes?id="+playerID,
                type: "GET",
                dataType: "json",
                async: false,
                success: function (data) {
                    this.setState({
                        dataset: JSON.parse(data.message),
                        result: data.success,
                    });

                }.bind(this)
            });
            // jQuery.ajax({
            //
            //     url: "http://localhost:8080/GetGamesForPlayerChart?id="+playerID,
            //     type:"GET",
            //     dataType:"json",
            //     async:false,
            //     success: function(data){
            //         this.setState({
            //             games:JSON.parse(data.message),
            //             result:data.success,
            //         });
            //
            //     }.bind(this)
            // });
        }

    };




    returnYLabel = (x,y) => {
        return(
            <text x={x} y={y} textAnchor="left">Net</text>
        )
    };

    returnXLabel = (x,y) => {
        return(
            <text x={x} y={y} textAnchor="center">Game</text>
        )
    };

    returnBar = (state) => {
        if(state===true) {
            return <Bar dataKey="wins" barSize={60} fill="#8884d8"/>
        }
        return  <Line dataKey="wins"   fill="#8884d8" />

    };

    toggleBarLine = () => {
        let check = !this.state.showBar;
        let buttonVal = "Show Bar Chart";
        if(check){
            buttonVal="Show Line Chart";
        }
        this.setState({
            showBar:check,
            buttonValue:buttonVal
        })
    };




    render() {
        return (
         <div className="PlayerChartContainer">
             <div className="PlayerGraph">
                     <span ><text >Net Wins/Losses</text></span>
             <ComposedChart width={1000} height={400} data={this.state.dataset}
                       margin={{top: 5, right: 30, left: 20, bottom: 5}}
             label={"Net Wins/Losses"}>
                 <XAxis dataKey="label" label={this.returnXLabel(500,400)} />
                 <YAxis domain={['auto', 'auto']} label={this.returnYLabel(20,150)}  tickCount={7} />
                 <CartesianGrid strokeDasharray="3 3"/>
                 <ReferenceLine y={0} stroke='#000'/>
                 <div className="legendWinsLosses">
                 <Legend />
                 </div>
                 {this.returnBar(this.state.showBar)}
                 <Tooltip position={{ x: 1000, y: 0 }}  content={<CustomToolTipDisplayNet/>} />
             </ComposedChart>
                 <div className="netWinsToggleButtons">
                     <button onClick={() => this.toggleBarLine()}>{this.state.buttonValue}</button>
                 </div>
             </div>

         </div>
        )
    }
}

const CustomTooltipWins  = React.createClass({
    propTypes: {
        type: PropTypes.string,
        payload: PropTypes.array,
        label: PropTypes.number,
    },

    getIntroOfPage(label) {
        return "Game "+label;
    },

    render() {
        const { active } = this.props;

        if (active) {
            const { payload, label } = this.props;
            return (
                <div className="custom-tooltip">
                    <p className="TooltipTitle">{this.getIntroOfPage(label)}</p>
                    <p className="TooltipText">Wins/Losses : {payload[0].value}</p>
                    <p className="TooltipText">{payload[0].payload.game.player1.username} :
                        {payload[0].payload.game.score1}</p>
                    <p className="TooltipText">{payload[0].payload.game.player2.username} :
                        {payload[0].payload.game.score2}</p>
                </div>
            );
        }

        return null;
    }
});

const CustomToolTipDisplayNet  = React.createClass({
    propTypes: {
        type: PropTypes.string,
        payload: PropTypes.array,
        label: PropTypes.number,
    },


    getIntroOfPage(label) {
        return "Game "+label;
    },

    render() {
        const { active } = this.props;


        if (active) {
            const {payload, label} = this.props;
            if (label > 0) {
                let game = payload[0].payload.game;

                return (
                    <div className="custom-tooltip-average">
                        <table className="GameDisplayTable">
                            <th className="GameDisplayHeader" colSpan={5}>
                                <span className="HeaderText">Net Wins/Losses</span>
                            </th>
                            <tr>
                                <td>
                                </td>
                                <td className="AverageUserHeader" colSpan={3}>
                                    <span >{payload[0].payload.wins}</span>
                                </td>
                                <td>
                                </td>
                                <td>
                                </td>
                            </tr>
                            <tr>
                                <td colSpan={5} className="GameDisplayHeader">
                                    <span className="HeaderText">{this.getIntroOfPage(label)}</span>
                                </td>
                            </tr>
                            <tr>
                                <td>

                                </td>
                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">You</span>
                                </td>

                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">{game.player2.username}</span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Score
                                </td>
                                <td colSpan={2}>
                                    {game.score1}
                                </td>
                                <td colSpan={2}>
                                    {game.score2}
                                </td>
                            </tr>
                        </table>
                    </div>
                );
            }
        }
        return null;
    }
});

export const CustomToolTipDisplayGame  = React.createClass({
    propTypes: {
        type: PropTypes.string,
        payload: PropTypes.array,
        label: PropTypes.number,
    },


    getIntroOfPage(label) {
        return "Game "+label;
    },

    render() {

        const { active } = this.props;


        if (active) {
            const {payload, label} = this.props;
            if (label > 0) {
                let game = payload[0].payload.game;

                return (
                    <div className="custom-tooltip-average">
                        <table className="GameDisplayTable">
                            <th className="GameDisplayHeader" colSpan={5}>
                                <span className="HeaderText">Average</span>
                            </th>
                            <tr>
                                <td>
                                </td>
                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">You</span>
                                </td>

                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">Opponent</span>
                                </td>
                            </tr>
                            <tr className="GameDisplayAverageRow">
                                <td>
                                    Score
                                </td>
                                <td colSpan={2}>
                                    {payload[0].payload.averageScore}
                                </td>
                                <td colSpan={2}>
                                    {-1 * payload[0].payload.opponentAverageScore}
                                </td>

                            </tr>
                            <tr>
                                <td colSpan={5} className="GameDisplayHeader">
                                    <span className="HeaderText">{this.getIntroOfPage(label)}</span>
                                </td>
                            </tr>
                            <tr>
                                <td>

                                </td>
                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">You</span>
                                </td>

                                <td className="AverageUserHeader" colSpan={2}>
                                    <span className="You">{game.player2.username}</span>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Score
                                </td>
                                <td colSpan={2}>
                                    {payload[0].payload.score}
                                </td>
                                <td colSpan={2}>
                                    {-1 * payload[0].payload.opponentScore}
                                </td>
                            </tr>
                        </table>
                    </div>
                );
            }
        }
        return null;
    }
});

const CustomizedLabel = React.createClass({

    render () {
        const {x, y, value,data} = this.props;
        return <text
            x={x}
            y={y}
            fontSize='16'
            fontFamily='sans-serif'
            textAnchor="middle">{value}</text>
    }
});

export class AverageScoreGraph extends React.Component {
    state = {
        dataset:[],
        result:'',
    };


    returnData = () => {
        return this.state.dataset
    };

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetAverageScore?id=2",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    dataset:JSON.parse(data.message),
                    result:data.success,
                });

            }.bind(this)
        });
        jQuery.ajax({

            url: "http://localhost:8080/GetGamesForPlayerChart?id=2",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    games:JSON.parse(data.message),
                    result:data.success,
                });

            }.bind(this)
        });

    };




    returnYLabel = (x,y) => {
        return (
            <text x={x} y={y} textAnchor="middle">Score</text>
        )
    };

    returnXLabel = (x,y) => {
        return (
            <text x={x} y={y} textAnchor="middle">Through Current Game</text>
        )
    };


    render() {
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                <BarChart width={500} height={400} data={this.state.dataset}
                margins={{top: 5, right: 30, left: 20, bottom: 5}}
                 >
                <XAxis dataKey="label" label={this.returnXLabel(325,262)} />
                <YAxis domain={[-10,10]} name={"Win/Losses"} ticks={[-10,0,10]} label={this.returnYLabel(0,175)} />
                <CartesianGrid strokeDasharray="3 3"/>
                <ReferenceLine y={0} stroke='#000'/>
                <Legend  />
                <Bar dataKey="averageScore"  fill="#8884d8" />}/>
                <Tooltip  />

                </BarChart>
                </div>
            </div>
        )
    }
}

const PlayerProfileSelect = (props) => {
    let options = props.players;

    let displayOption = (option) => {
        return option.username;
    };

    return (
        <Typeahead
            options = {options}
            displayOption = {displayOption}
            filterOption = 'username'
            value = {props.currentPlayer}
            id = {props.id}
            onOptionSelected = {props.onOptionSelected}
        />
    );
};

export class PlayerGraphTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            playerID:props.history.location.state.player.id,
            players:[],
            resultPlayers:'',
            playerUsername:props.history.location.state.player.username,
            history:history.state
        };
    }

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    players:JSON.parse(data.message),
                    resultPlayers:data.success,
                });
            }.bind(this)
        });
    };

    setPlayer = (event) => {
      let id = event.id;
      let username = event.username;
      this.setState({
          playerID:id,
          playerUsername:username
      })
    };

    render() {
        return (
            <table >
                <thead>
                <tr>
                    <th className="playerProfileHeader">Player Profile <spam className="UsernameHeader">{this.state.playerUsername}</spam></th>
                </tr>
                <tr>
                    <PlayerProfileSelect onOptionSelected = {(event) => this.setPlayer(event)}
                    players = {this.state.players}/>
                </tr>
                </thead>

                <tbody>
                <tr id="infoDisplay">
                    <td><AverageScorePerGame playerID = {this.state.playerID}/></td>
                </tr>
                <tr id="infoDisplay">
                    <td><NetWinsGraph playerID = {this.state.playerID}/></td>
                </tr>
                <tr>
                    <td><EloRatingPerGame playerID = {this.state.playerID}/></td>
                </tr>
                </tbody>
            </table>
        )
    }
};
