import ToggleDisplay from 'react-toggle-display';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");



export default class GamesList extends React.Component {
    state = {
        games:[], result:'',
        showUsername1:[],
        showUsername2:[],
        showScore1:[],
        showScore2:[]
    };

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetGames",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    games:JSON.parse(data.message),
                    result:data.success,
                });
                let showUsername1Array = [];
                let showUsername2Array = [];
                let showScore1Array = [];
                let showScore2Array = [];
                for(let i = 0; i< JSON.parse(data.message).length; i++){
                    showUsername1Array.push(true);
                    showUsername2Array.push(true);
                    showScore1Array.push(true);
                    showScore2Array.push(true);
                }
                this.setState({
                    showUsername1:  showUsername1Array,
                    showUsername2:  showUsername2Array,
                    showScore1: showScore1Array,
                    showScore2: showScore2Array,
                })
            }.bind(this)
        });
    };

    render() {
        let Table = Reactable.Table;
        let Tr = Reactable.Tr;
        let Td = Reactable.Td;

        let editLinks = <div>
            <a href="">Edit</a>
            &nbsp;
            <a href="">Delete</a>
        </div>;
        return (
            <div className="tableHolder">
                <form>
                <Table className="table" border="true" itemsPerPage={4} pageButtonLimit={5}>

                    {this.state.games.map((game,i) =>
                        <Tr>
                            <Td column="ID" >{game.id}</Td>
                            <Td column="Player 1 Username" ><ToggleDisplay show={this.state.showUsername1[i]}>{game.player1.username}</ToggleDisplay></Td>
                            <Td column="Player 2 Username"><ToggleDisplay show={this.state.showUsername2[i]}>{game.player2.username}</ToggleDisplay></Td>
                            <Td column="Score 1"><ToggleDisplay show={this.state.showScore1[i]}>{game.score1}</ToggleDisplay></Td>
                            <Td column="Score 2"><ToggleDisplay show={this.state.showScore2[i]}>{game.score2}</ToggleDisplay></Td>
                            <Td column="Actions" data={editLinks} ><a href="">Edit</a></Td>
                        </Tr>
                    )};
                </Table>
                </form>
            </div>
        );
    }
}

class EditGames extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            score1: props.score1,
            score2:props.score2,
            player1Username:props.player1.username,
            player2Username:props.player2.username,
            players:[],
            id:props.id,

        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleChange(event) {
        if(event.target === 'Score 1') {
            this.setState({
                score1: event.target.value
            });
        }
        if(event.target === 'Score 2') {
            this.setState({
                score2: event.target.value
            });
        }
        if(event.target === 'Player 1 Username') {
            this.setState({
                player1Username: event.target.value
            });
        }
        if(event.target === 'Player 2 Username') {
            this.setState({
                player2Username: event.target.value
            });
        }
    };


    handleSubmit(event) {




        jQuery.ajax({

            url: "http://localhost:8080/EditGame?id="+this.state.id+"&player1Username="+this.state.player1Username+
            "&player2Username="+this.state.player2Username+"&score1="+this.state.score1+"&score2="+this.state.score2,
            type:"POST",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    message:data.message,
                    result:data.success,
                });
                alert(data.message);
            }.bind(this)
        });

    };


    render() {
        return (
            <div id="editUsernameContainer" >
                <form id="editUsernameForm" onSubmit={this.handleSubmit}>
                    <input id="editUsernameInput" type="text" value={this.state.newUsername} onChange={this.handleChange} />
                    <input type="submit" value="Submit" />
                </form>
            </div>
        );
    }
}


