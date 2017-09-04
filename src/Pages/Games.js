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
        resultPlayers:'',
        editPlayer1ID:0,
        editPlayer2ID:0,
        editScore1:0,
        editScore2:0,
        editGameID:0,
        resultEditGame:'',
        resultDelete:''
    };

    componentDidMount = () => {
        jQuery('tbody.reactable-pagination tr td').addClass('custom-pagination');
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

    showEditGame = (index,game) => {

        let showEditArray = this.state.showEdit.concat();
        let indexOfTrue = showEditArray.indexOf(true);
        if(indexOfTrue >=0){
            showEditArray[indexOfTrue]=false;
        }
        showEditArray[index]=true;


        this.setState({
            showEdit:showEditArray,
            editGameID:game.iD,
            editPlayer1ID:game.player1.id,
            editPlayer2ID:game.player2.id,
            editScore1:game.score1,
            editScore2:game.score2
        });
    };

    cancelEditGame = (index) => {
        let showEditArray = this.state.showEdit.concat();
        showEditArray[index]=false;

        this.setState({
            showEdit : showEditArray,
            editGameID:0
        });
    };

    deleteGame = (id) => {
        jQuery.ajax({

            url: "http://localhost:8080/DeleteGame?iD="+id,
            type:"DELETE",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    resultDelete:JSON.parse(data.message),
                });
            }.bind(this)
        });
    };

    onChangePlayer1 = (event) => {
        let id = event.id;
      this.setState({
          editPlayer1ID:id
      });
    };

    onChangePlayer2 = (event) => {
        let id = event.id;
        this.setState({
            editPlayer2ID:id
        });
    };

    onChangeScore1 = (event) => {
        let score = event.target.value;
        this.setState({
            editScore1:score
        });
    };

    onChangeScore2 = (event) => {
        let score = event.target.value;
        this.setState({
            editScore2:score
        });
    };

    handleSubmit = () => {
        jQuery.ajax({
            url: "http://localhost:8080/EditGame?iD="+this.state.editGameID+"&player1ID="+this.state.editPlayer1ID
            +"&player2ID="+this.state.editPlayer2ID+"&score1="+this.state.editScore1+"&score2="+this.state.editScore2,
            type:"POST",
            dataType:"json",
            async:false,
            success: this.componentDidMount
        });
    };

    render() {
        let Table = Reactable.Table;
        let Tr = Reactable.Tr;
        let Td = Reactable.Td;

        return (
            <div className="tableHolder">
                <Table className="GameTable" border="true" itemsPerPage={8}>

                    {this.state.games.map((game,i) =>
                        <Tr>
                            <Td column="Date" className="firstCell">
                                {game.timeString}
                            </Td>
                            <Td column="Player 1" className="player1Cell">
                                <div>
                                    <ToggleDisplay show={!this.state.showEdit[i]}>
                                        {game.player1.username}
                                        </ToggleDisplay>
                                    <ToggleDisplay show={this.state.showEdit[i]} >
                                        <EditUsernameTypeAhead  onOptionSelected={(event) => this.onChangePlayer1(event)}
                                                                players={this.state.players}
                                                                currentPlayer = {game.player1.username}/>
                                    </ToggleDisplay>
                                </div>
                            </Td>
                            <Td column="Player 2" className="player1Cell">
                                <div>
                                    <ToggleDisplay show={!this.state.showEdit[i]}>
                                        {game.player2.username}
                                    </ToggleDisplay>
                                    <ToggleDisplay show={this.state.showEdit[i]} >
                                        <EditUsernameTypeAhead onOptionSelected={(event) => this.onChangePlayer2(event)}
                                                               players={this.state.players}
                                                               currentPlayer = {game.player2.username}
                                                               />
                                    </ToggleDisplay>
                                </div>
                            </Td>
                            <Td column="Score 1" className="player1Cell">
                                <div>
                                    <ToggleDisplay show = {!this.state.showEdit[i]}>
                                        {game.score1}
                                    </ToggleDisplay>
                                    <ToggleDisplay show = {this.state.showEdit[i]}>
                                        <div>
                                        <EditScoreInput onChange={(event) => this.onChangeScore1(event)} score = {game.score1}/>
                                        </div>
                                    </ToggleDisplay>
                                </div>
                            </Td>
                            <Td column="Score 2" className="player1Cell">
                                <div>
                                    <ToggleDisplay show = {!this.state.showEdit[i]}>
                                        {game.score2}
                                    </ToggleDisplay>
                                    <ToggleDisplay show = {this.state.showEdit[i]}>
                                        <div>
                                            <EditScoreInput onChange={(event) => this.onChangeScore2(event)} score = {game.score2}/>
                                        </div>
                                    </ToggleDisplay>
                                </div>
                            </Td>

                            <Td column="Actions" >
                                <div>
                                    <ToggleDisplay show={this.state.showEdit[i]}>
                                        <div>
                                        <input type="button" value="Submit" onClick={this.handleSubmit}/>
                                        &nbsp;
                                        </div>
                                    </ToggleDisplay>
                                    <div className="editContainer">
                                    <a style={{cursor: 'pointer'}} onClick={() => this.showEditGame(i,game)} >Edit</a>
                                    </div>
                                    &nbsp;
                                    <div className="cancelContainer">
                                    <a style={{cursor: 'pointer'}} onClick={() => this.cancelEditGame(i)}>Cancel</a>
                                    </div>
                                    &nbsp;
                                    &nbsp;
                                    &nbsp;
                                    <div className="deleteContainer">
                                    <a style={{cursor: 'pointer'}} onClick={() => this.deleteGame(game.iD)} >Delete</a>
                                    </div>
                                </div>
                            </Td>

                        </Tr>
                    )};
                </Table>
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
            displayOption = {displayOption}
            filterOption = 'username'
            value = {props.currentPlayer}
            id = {props.id}
            onOptionSelected = {props.onOptionSelected}
            customClasses={{
                input: "typeahead-text-input",
                results: "typeahead-list__container",
                listItem: "typeahead-list__item",
                hover: "typeahead-active",
            }}
        />
    );
};

const EditScoreInput = (props) => {
    return (
           <input type = "number" defaultValue = {props.score} onChange={props.onChange}/>
    )
};


