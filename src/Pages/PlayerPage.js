import ToggleDisplay from 'react-toggle-display';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Area, AreaChart, Tooltip,BarChart,Bar,Legend,ReferenceLine,ComposedChart
} from 'recharts';
import pureRender from 'react-pure-render';

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
        games:[]
    };


    returnData = () => {
        return this.state.dataset
    };

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayerOutcomes?id=2",
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
        return(
            <text x={x} y={y} textAnchor="left">Wins/Losses</text>
        )
    };

    returnXLabel = (x,y) => {
        return(
            <text x={x} y={y} textAnchor="center">Game</text>
        )
    };



    render() {
        return (
         <div className="PlayerChartContainer">
             <div className="PlayerGraph">
             <BarChart width={500} height={400} data={this.state.dataset}
                       margin={{top: 5, right: 30, left: 20, bottom: 5}}
             label={"Net Wins/Losses"}>
                 <XAxis dataKey="label" label={this.returnXLabel(140,385)} />
                 <YAxis domain={[-5,5]} label={this.returnYLabel(20,150)} ticks={[-5,0,5]} />
                 <CartesianGrid strokeDasharray="3 3"/>
                 <ReferenceLine y={0} stroke='#000'/>
                 <div className="legendWinsLosses">
                 <Legend />
                 </div>
                 <Tooltip label="jah"   content={<CustomTooltipWins/>} />
                 <Bar dataKey="wins"  barSize={60} fill="#8884d8" />
             </BarChart>
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

export class AverageScorePerGame extends React.Component {
    state = {
        dataset:[],
        result:'',
        showOppScore:true,
        barGraphKeys:["opponentScore","score"],
        opacity: 1,
        showScore:true,
        scoreButtonValue:"Hide Score",
        oppScoreButtonValue:"Hide Opp Score",
        showAverage:true,
        averageButtonValue:"Hide Average",
        showOppAverage:true,
        oppAverageButtonValue:"Hide Opp Average"
    };

    hideBarElement = () => {
        if(this.state.opacity == 0){
            this.setState({
                opacity :1
            });
        } else {
            this.setState({
                opacity: 0
            });
        }
    };

    returnData = () => {
        return this.state.dataset
    };

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetAverageScores?id=2",
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
    };

    returnYLabel = (x,y) => {
        return (
            <text x={x} y={y}  textAnchor="middle" fontSize={'16pt'} className="XAxisLabel">Score</text>
        )
    };

    returnXLabel = (x,y) => {
        return (
                <text x={x} y={y} textAnchor="middle" fontSize={'16pt'} className="XAxisLabel" >Game</text>
        )
    };

    toggleScore = () => {
        let toggle = !this.state.showScore;
        let text = "";
        if(toggle===true){
            text = "Hide Score";
        } else {
            text = "Show Score";
        }
        this.setState({
            showScore:toggle,
            scoreButtonValue:text
        })

    };

    toggleOppScore = () => {
        let toggle = !this.state.showOppScore;
        let text = "";
        if(toggle===true){
            text = "Hide Opp Score";
        } else {
            text = "Show Opp Score";
        }
        this.setState({
            showOppScore:toggle,
            oppScoreButtonValue:text
        })

    };

    toggleAverage = () => {
        let toggle = !this.state.showAverage;
        let text = "";
        if(toggle===true){
            text = "Hide Average";
        } else {
            text = "Show Average";
        }
        this.setState({
            showAverage:toggle,
            averageButtonValue:text
        })

    };

    toggleOppAverage = () => {
        let toggle = !this.state.showOppAverage;
        let text = "";
        if(toggle===true){
            text = "Hide Opp Average";
        } else {
            text = "Show Opp Average";
        }
        this.setState({
            showOppAverage:toggle,
            oppAverageButtonValue:text
        })

    };

    renderBarScore = (state) => {
        if(state === true) {
            return (<Bar dataKey="score" barSize={25} fill='#4286f4' />)
        }
    };

    renderBarOppScore = (state) => {
        if(state === true) {
            return (<Bar dataKey ="opponentScore" barSize={25} fill='#ad0505'/>
            )
        }
    };

    renderAverageScore = (state) => {
        if(state === true) {
            return (<Line dataKey ="averageScore" fill='#581887' stroke='#1029cc'/>
            )
        }
    };

    renderOppAverageScore = (state) => {
        if(state === true) {
            return (<Line dataKey ="opponentAverageScore" fill='#470303' stroke='#ad0505'/>
            )
        }
    };




    render() {
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                    <span ><text >Average Score Per Game</text></span>
                    <ComposedChart width={500} height={400} data={this.state.dataset}
                                   margins={{top: 5, right: 30,  bottom: 5}} >
                        <XAxis type="number" dataKey="label" label={this.returnXLabel(475,375)} padding={{bottom: 50,right:50}} labelStyle = {{paddingTop:20,color : '#32CD32'}}/>
                        <YAxis domain={[-30,30]} label={this.returnYLabel(30,150)} ticks={[-30,-20,-10,0,10,20,30]} />
                        <Tooltip/>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <ReferenceLine y={0} stroke='#000'/>
                        {this.renderBarScore(this.state.showScore)}
                        {this.renderBarOppScore(this.state.showOppScore)}
                        {this.renderAverageScore(this.state.showAverage)}
                        {this.renderOppAverageScore(this.state.showOppAverage)}
                        <Legend margins={{top: 15, right: 15,  bottom: 5}} />
                    </ComposedChart>
                </div>
                <div className="averageScoreToggleButtons">
                    <button onClick={() => this.toggleScore()}>{this.state.scoreButtonValue}</button>
                    <button onClick={() => this.toggleOppScore()}>{this.state.oppScoreButtonValue}</button>
                    <button onClick={() => this.toggleAverage()}>{this.state.averageButtonValue}</button>
                    <button onClick={() => this.toggleOppAverage()}>{this.state.oppAverageButtonValue}</button>
                </div>
            </div>
        )
    }
}






const renderBar2 = () => {
    return (
        <Bar dataKey ="opponentScore" barSize={25} fill='#ad0505'/>
    )
};



export const PlayerGraphTable = () => {
    return (
        <table >
            <thead>
            <tr>
                <th>Home Page</th>
            </tr>
            </thead>
            <tbody>
            <tr id="infoDisplay">
                <td><AverageScorePerGame/></td>
            </tr>
            <tr id="infoDisplay" >
                <td><NetWinsGraph/></td>
            </tr>
            <tr>
                <td><AverageScoreGraph/></td>
            </tr>
            </tbody>
        </table>
    )
};
