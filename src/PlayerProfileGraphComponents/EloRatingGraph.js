
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Area, AreaChart, Tooltip,BarChart,Bar,Legend,ReferenceLine,ComposedChart
} from 'recharts';
const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");
const ReactFC = require('react-fusioncharts');
const fusioncharts = require('fusioncharts');
const charts = require('fusioncharts/fusioncharts.charts');
const {PropTypes} = React;


export class EloRatingPerGame extends React.Component {
    state = {
        dataset:[],
        result:'',
        showOppEloRating:true,
        showEloRating:true,
        barGraphKeys:["eloRating","opponentEloRating"],
        opacity: 1,
        eloRatingButton:"Hide Elo Rating",
        oppEloRatingButton:"Hide Opp Elo Rating",
        oppDataSet:[],
        negativeSet:[],
        positiveSet: [],

        gameDisplayStats: {
            number:0,
            player1Username:'',
            player2Username:'',
            score1:0,
            score2:0
        }
    };


    // componentDidMount = () => {
    //     jQuery.ajax({
    //
    //         url: "http://localhost:8080/GetEloRatings?id=2",
    //         type:"GET",
    //         dataType:"json",
    //         async:false,
    //         success: function(data){
    //             this.setState({
    //                 dataset:JSON.parse(data.message),
    //                 result:data.success,
    //             });
    //
    //         }.bind(this)
    //     });
    // };
    componentWillReceiveProps = (nextProps) => {

        const playerID = nextProps.playerID;
        if (this.props !== nextProps) {
            this.setState({
                playerID: playerID
            });
            jQuery.ajax({

                url: "http://localhost:8080/GetEloRatings?id="+playerID,
                type:"GET",
                dataType:"json",
                async:false,
                success: function(data){
                    let oppDataSet = [];
                    let dataSet = JSON.parse(data.message);
                    for (let i = 0; i < dataSet.length; i++) {
                        let newData = dataSet[i];
                        newData.opponentEloRating = newData.opponentEloRating*-1;
                        oppDataSet.push(newData);
                    }
                    this.setState({
                        dataset:JSON.parse(data.message),
                        result:data.success,
                        oppDataSet:oppDataSet,
                        negativeSet:oppDataSet,
                        positiveSet:JSON.parse(data.message)
                    });

                }.bind(this)
            });
        }
    };

    returnYLabel = (x,y) => {
        return (
            <text x={x} y={y}  textAnchor="middle" fontSize={'16pt'} className="XAxisLabel">Rating</text>
        )
    };

    returnXLabel = (x,y) => {
        return (
            <text x={x} y={y} textAnchor="middle" fontSize={'16pt'} className="XAxisLabel" >Game</text>
        )
    };

    toggleEloRating = () => {
        let toggle = !this.state.showEloRating;
        let text = "";
        if(toggle===true){
            text = "Hide Elo Rating";
        } else {
            text = "Show Elo Rating";
        }
        this.setState({
            showEloRating:toggle,
            eloRatingButton:text
        });
        if (toggle === false) {
            this.checkOppScoreIsOnlyOneOut(toggle,this.state.showOppEloRating);
        }

    };

    toggleOppEloRating = () => {
        let toggle = !this.state.showOppEloRating;
        let text = "";
        if(toggle===true){
            text = "Hide Opp Elo Rating";
        } else {
            text = "Show Opp Elo Rating";
        }
        this.setState({
            showOppEloRating:toggle,
            oppEloRatingButton:text
        });

    };

    checkOppScoreIsOnlyOneOut = (elo,oppElo) => {
      // if (elo ===false &&
      // oppElo === true) {
      //     if (this.state.dataset === this.state.negativeSet) {
      //         let positive = this.state.positiveSet;
      //         this.setState({
      //             dataset: positive
      //         });
      //     } else {
      //
      //         let negative = this.state.negativeSet;
      //         this.setState({
      //             dataset: negative
      //         });
      //     }
      // } else {
      //     let positive = this.state.positiveSet;
      //     this.setState({
      //         dataSet:positive
      //     });
      // }


    };

    renderEloRating = (state) => {
        if(state === true) {
            return (<Line dataKey="eloRating" fill='#4286f4' />)
        }
    };

    renderOppEloRating = (state) => {
        if(state === true) {
            return (<Line dataKey ="opponentEloRating"  fill='#4d004d' stroke='#4d004d' />
            )
        }
    };


    setGameDisplayState = (number,player1Username,player2Username,score1,score2) => {
        this.setState({
            gameDisplayStats: {
                number:number,
                player1Username:player1Username,
                player2Username:player2Username,
                score1:score1,
                score2:score2
            }
        })
    };



    render() {
        return (
            <div className="PlayerChartContainer">
                <div className="PlayerGraph">
                    <span ><text >Elo Rating Per Game</text></span>
                    <LineChart width={1000} height={400} data={this.state.dataset}
                                   margins={{top: 5, right: 30,  bottom: 5}} >
                        <XAxis allowDecimals={false} type="number" dataKey="label" domain={[0,'auto']} label={this.returnXLabel(475,375)} padding={{bottom: 50,right:10}} labelStyle = {{paddingTop:20,color : '#32CD32'}}/>
                        <YAxis domain={['auto','auto']} label={this.returnYLabel(30,150)} />
                        <Tooltip position={{ x: 1000, y: 0 }} content={<CustomToolTipDisplayGameElo setGameDisplay = {this.setGameDisplayState}/>}/>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <ReferenceLine y={0} className="referenceLine"  stroke='#000'/>
                        {this.renderEloRating(this.state.showEloRating)}
                        {this.renderOppEloRating(this.state.showOppEloRating)}
                        <Legend margins={{top: 15, right: 15,  bottom: 5}} />
                    </LineChart>
                </div>
                <div className="averageScoreToggleButtons">
                    <button className="graphButton" onClick={() => this.toggleEloRating()}>{this.state.eloRatingButton}</button>
                    <button className="graphButton" onClick={() => this.toggleOppEloRating()}>{this.state.oppEloRatingButton}</button>
                </div>
            </div>
        )
    }
}

export const CustomToolTipDisplayGameElo  = React.createClass({
    propTypes: {
        type: PropTypes.string,
        payload: PropTypes.array,
        label: PropTypes.number,
    },


    getIntroOfPage(label,timeString) {
        return "Game "+label + " -" + timeString;
    },

    render() {
        const { active } = this.props;


        if (active) {
            const { payload, label } = this.props;
            if(label>0) {
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
                                    {payload[0].payload.eloRating}
                                </td>
                                <td colSpan={2}>
                                    {payload[0].payload.opponentEloRating}
                                </td>

                            </tr>
                            <tr>
                                <td colSpan={5} className="GameDisplayHeader">
                                    <span className="HeaderText">{this.getIntroOfPage(label,game.timeString)}</span>
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