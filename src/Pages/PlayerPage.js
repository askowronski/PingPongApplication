import ToggleDisplay from 'react-toggle-display';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Area, AreaChart, Tooltip,
    ResponsiveContainer, Legend, BarChart, Bar, ReferenceLine
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



export default class NetWinsGraph extends React.Component {
    state = {
        dataset:[],
        result:'',
        attr : {
            "caption": "Net Wins By Game",
            "subCaption": "Player 1",
            "plotgradientcolor": "",
            "bgcolor": "FFFFFF",
            "showalternatehgridcolor": "0",
            "divlinecolor": "CCCCCC",
            "showvalues": "0",
            "showcanvasborder": "0",
            "canvasborderalpha": "0",
            "canvasbordercolor": "CCCCCC",
            "canvasborderthickness": "1",
            "yaxismaxvalue": "10",
            "captionpadding": "30",
            "linethickness": "3",
            "yaxisvaluespadding": "15",
            "legendshadow": "0",
            "legendborderalpha": "0",
            "palettecolors": "#f8bd19,#008ee4,#33bdda,#e44a00,#6baa01,#583e78",
            "showborder": "0"
        },
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

    renderContent = () => {
        return(
            <div>
                <span>sick game</span>
            </div>
        )
    };

    render() {
        return (
         <div id="PlayerChartContainer">
            {/*<LineChart width={400} height={400} data={this.returnData()} margin={{top: 5, right: 30, left: 20, bottom: 5}}>*/}
                {/*<XAxis type="number" dataKey="label" axisLine={5} />*/}
                {/*<YAxis domain={[-5,5]} />*/}
                {/*<Tooltip/>*/}
                {/*<Legend />*/}
                {/*<Line dataKey ="value" />*/}
            {/*</LineChart>*/}
             <BarChart width={600} height={300} data={this.state.dataset}
                       margin={{top: 5, right: 30, left: 20, bottom: 5}}>
                 <XAxis dataKey="label" />
                 <YAxis domain={[-5,5]} name={"Win/Losses"} ticks={[-5,0,5]} />
                 <CartesianGrid strokeDasharray="3 3"/>
                 <ReferenceLine y={0} stroke='#000'/>
                 <Legend />
                 <Bar dataKey="wins"  fill="#8884d8"  />
                 <Tooltip label="jah"  payload={this.state.games} content={this.renderContent()} />
             </BarChart>
         </div>
        )
    }
}