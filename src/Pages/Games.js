import ToggleDisplay from 'react-toggle-display';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");
const Typeahead = require('react-typeahead').Typeahead;




export default class GamesList extends React.Component {
    state = {
        games:[],
        resultGames:'',
        players:[],
        showEdit:[],
        resultPlayers:''
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
                    resultGames:data.success,
                });
                let showEditArray = [];
                for(let i = 0; i< JSON.parse(data.message).length; i++){
                    showEditArray.push(false);
                }
                this.setState({
                    showEdit : showEditArray
                })
            }.bind(this)
        });
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

    showEditGame = (index) => {

        let showEditArray = this.state.showEdit.concat();
        showEditArray[index]=true;

        this.setState({
            showEdit:showEditArray,
        });
    };

    cancelEditGame = (index) => {
        let showEditArray = this.state.showEdit.concat();
        showEditArray[index]=false;

        this.setState({
            showEdit : showEditArray
        });
    };

    processDeleteGame = (id,username) => {

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
                <Table className="table" border="true" itemsPerPage={4}>

                    {this.state.games.map((game,i) =>
                        <Tr>
                            <Td column="ID" >{game.id}</Td>
                            <Td column="Player 1 Username" >
                                <div>
                                    <ToggleDisplay show={!this.state.showEdit[i]}>
                                        {game.player1.username}
                                        </ToggleDisplay>
                                    <ToggleDisplay show={this.state.showEdit[i]} >
                                        <EditUsernameTypeAhead id="swag" players={this.state.players} currentPlayer = {game.player1.username}/>
                                    </ToggleDisplay>
                                </div>
                            </Td>
                            <Td column="Player 2 Username" >
                                <div>
                                    <ToggleDisplay show={!this.state.showEdit[i]}>
                                        {game.player2.username}
                                    </ToggleDisplay>
                                    <ToggleDisplay show={this.state.showEdit[i]} >
                                        <EditUsernameTypeAhead players={this.state.players} currentPlayer = {game.player2.username}/>
                                    </ToggleDisplay>
                                </div>
                            </Td>
                            <Td column="Score 1">
                                <div>
                                    <ToggleDisplay show = {!this.state.showEdit[i]}>
                                        {game.score1}
                                    </ToggleDisplay>
                                    <ToggleDisplay show = {this.state.showEdit[i]}>
                                        <div>
                                        <EditScoreInput score = {game.score1}/>
                                        </div>
                                    </ToggleDisplay>
                                </div>
                            </Td>
                            <Td column="Score 2">
                                <div>
                                    <ToggleDisplay show = {!this.state.showEdit[i]}>
                                        {game.score2}
                                    </ToggleDisplay>
                                    <ToggleDisplay show = {this.state.showEdit[i]}>
                                        <div>
                                            <EditScoreInput score = {game.score2}/>
                                        </div>
                                    </ToggleDisplay>
                                </div>
                            </Td>
                            <Td column="Actions" >
                                <div>
                                    <a style={{cursor: 'pointer'}} onClick={() => this.showEditGame(i)} >Edit</a>
                                    &nbsp;
                                    <a style={{cursor: 'pointer'}} onClick={() => this.cancelEditGame(i)}>Cancel</a>
                                    &nbsp;
                                    <a style={{cursor: 'pointer'}} >Delete</a>
                                </div>
                            </Td>
                        </Tr>
                    )};
                </Table>
                </form>
            </div>
        );
    }
}

const EditUsernameTypeAhead = (props) => {
    let options = props.players;

    let displayOption = (option) => {
        return option.username;
    };

    return (
        <Typeahead
            options = {options}
            displayOption={displayOption}
            filterOption='username'
            value= {props.currentPlayer}
            id={props.id}
        />
    );
};

const EditScoreInput = (props) => {
    return (
           <input type="number" defaultValue={props.score}/>
    )
};


