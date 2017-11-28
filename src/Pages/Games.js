import ToggleDisplay from 'react-toggle-display';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import 'react-datepicker/dist/react-datepicker.css';
import TimePicker from 'react-times';
// use material theme
import 'react-times/css/material/default.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");
const Typeahead = require('react-typeahead').Typeahead;

export default class GamesList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            games: [],
            resultGames: '',
            players: [],
            showEdit: [],
            showEditTime:[],
            resultPlayers: '',
            editPlayer1ID: 0,
            editPlayer2ID: 0,
            editScore1: 0,
            editScore2: 0,
            editGameID: 0,
            editGameTime: '',
            resultEditGame: '',
            resultDelete: '',
            editDate: '',
            loading: true,
            loadTable: false,
            graphContent: null,
            editPlayer1: {
                id:'',
                username:''
            },
            editPlayer2: {
                id:'',
                username:''
            },
            openTime:false,
            editTime:'',
            editHour:'',
            editMinute:'',
            focusTime:false,
            areThereGames:true,
        }
    }

    componentDidMount = () => {
        this.finishTableSetup();
        jQuery.ajax({

            url: "http://localhost:8080/GetGamesNoRatings",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                this.setState({
                    games: JSON.parse(data.message),
                    resultGames: data.success,
                });
                if ( JSON.parse(data.message).length === 0) {
                    this.setState({
                        areThereGames:false
                    });
                } else {
                    this.setState({
                        areThereGames:true
                    });
                }
                let showEditArray = [];
                let showEditTimeArray = [];

                for (let i = 0; i < JSON.parse(data.message).length; i++) {
                    showEditArray.push(false);
                    showEditTimeArray.push(false);
                }
                this.setState({
                    showEdit: showEditArray,
                    showEditTime:showEditTimeArray
                })
            }.bind(this)
        });
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type: "GET",
            dataType: "json",
            async: true,
            success: function(data) {
                this.setState({
                    players: JSON.parse(data.message),
                    resultPlayers: data.success,
                    loading: false

                });
            }.bind(this)
        });
    };

    finishTableSetup = (props) => {
        jQuery('tbody.reactable-pagination tr td').addClass(
            'custom-pagination');

    };

    showEditGame = (index, game) => {

        let showEditArray = this.state.showEdit.concat();
        let indexOfTrue = showEditArray.indexOf(true);
        if (indexOfTrue >= 0) {
            showEditArray[indexOfTrue] = false;
        }
        showEditArray[index] = true;

        debugger;

        this.setState({
            showEdit: showEditArray,
            editGameID: game.iD,
            editPlayer1ID: game.player1.id,
            editPlayer2ID: game.player2.id,
            editScore1: game.score1,
            editScore2: game.score2,
            editDate: moment(game.timeString),
            editTime:moment(game.timeString).format("HH:mm"),
            editPlayer2:game.player2,
            editPlayer1:game.player1
        });
    };

    cancelEditGame = (index) => {
        let showEditArray = this.state.showEdit.concat();
        showEditArray[index] = false;

        this.setState({
            showEdit: showEditArray,
            editGameID: 0,
            editScore1: '',
            editScore2: '',
        });
    };

    deleteGame = (id) => {
        jQuery.ajax({

            url: "http://localhost:8080/DeleteGame?iD=" + id,
            type: "DELETE",
            dataType: "json",
            async: true,
            success: function(data) {
                alert(data.message);
                this.componentDidMount()
            }.bind(this)
        });
    };

    onChangePlayer1 = (event) => {
        if (event !== null) {
            let id = event.id;
            this.setState({
                editPlayer1: event,
                editPlayer1ID: id
            });
        } else {
            this.setState({
                editPlayer1:{
                    id:'',
                    username:''
                },
                editPlayer1ID:0
            });
        }
    };

    onChangePlayer2 = (event) => {
        debugger;
        if (event !== null) {
            let id = event.id;
            this.setState({
                editPlayer2: event,
                editPlayer2ID: id
            });
        } else {
            this.setState({
                editPlayer2:'',
                editPlayer2ID:0
            });
        }
    };

    onChangeScore1 = (event) => {
        let score = event.target.value;
        this.setState({
            editScore1: score
        });
    };

    onChangeScore2 = (event) => {
        let score = event.target.value;
        this.setState({
            editScore2: score
        });
    };

    handleSubmit = () => {

        let timeString = moment(this.state.editDate+this.state.editTime).format('YYYYMMMDD HH:mm:ss');

        jQuery.ajax({
            url: "http://localhost:8080/EditGame?iD=" + this.state.editGameID
            + "&player1ID=" + this.state.editPlayer1ID
            + "&player2ID=" + this.state.editPlayer2ID + "&score1="
            + this.state.editScore1 + "&score2=" + this.state.editScore2 +
            "&time=" + this.state.editDate.format('YYYYMMMDD')+"+"+this.state.editTime,
            type: "POST",
            dataType: "json",
            async: true,
            success: function(data) {
                alert(data.message);
                this.componentDidMount();
            }.bind(this),
            error: function(data) {
                this.componentDidMount();
            }.bind(this)
        });
    };

    handleDateChange = (date) => {
        this.setState({
            editDate: date
        });
    };

    onTimeChange(time) {
        this.setState({
            editTime: time
        })
    }

    render() {
        let Table = Reactable.Table;
        let Tr = Reactable.Tr;
        let Td = Reactable.Td;
        let gamesLength = this.state.games.length;
        return (

            <div>
            {
                !this.state.areThereGames ? <div className="noDataContainer">No Games</div> :
                <div>
                    <div>
                        <p id="loadingSpinner" style={{'text-align': 'center'}}>
                            <img src={require('../images/Spinner.gif')}
                                 width='45%' height='45%'/></p>
                    </div>
                    <div className="tableHolder">
                        <Table className="GameTable"
                               border="true" itemsPerPage={10}
                               sortable={true}
                               filterable={['Player 1', 'Player 2']}
                               defaultSortDescending
                               defaultSort={{column: 'Date', direction: 'desc'}}
                               pageButtonLimit={15}
                        >
                            {this.state.games.map((game,i) => {
                                if (i === gamesLength - 1) {
                                    jQuery('tbody.reactable-pagination tr td').addClass(
                                        'custom-pagination');
                                    jQuery('.tableHolder').css('visibility','visible');
                                    jQuery('#loadingSpinner').remove();
                                    if (jQuery('.filterGame').length) {

                                    } else {
                                        jQuery(
                                            '.reactable-filter-input').parent().prepend(
                                            '<span class="filterGame">Player Filter<span>')
                                    }
                                }

                                return <Tr className="firstCell">
                                    <Td column="Date"
                                        value={moment(game.timeString).format("YYYY-MM-DD")}
                                        sortFunction={Date}>
                                        <div>
                                            <ToggleDisplay
                                                show={!this.state.showEdit[i]}>
                                                {moment(game.timeString).format("YYYY-MM-DD")}
                                            </ToggleDisplay>
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>
                                                <DateInput
                                                    startDate={this.state.editDate}
                                                    onChange={this.handleDateChange}/>
                                            </ToggleDisplay>
                                        </div>
                                    </Td>

                                    <Td column="Time"
                                        value={moment(game.timeString).format("HH:mm:ss")}
                                        sortFunction={Date}>
                                        <div>
                                            <ToggleDisplay
                                                show={!this.state.showEdit[i]}>
                                                {moment(game.timeString).format("HH:mm:ss")}
                                            </ToggleDisplay>
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>
                                                <TimePicker
                                                    onTimeChange={this.onTimeChange.bind(this)}
                                                    time={this.state.editTime}
                                                />
                                            </ToggleDisplay>
                                        </div>
                                    </Td>
                                    <Td column="Player 1" className="player1Cell"
                                        value={game.player1.username}>
                                        <div>
                                            <ToggleDisplay
                                                show={!this.state.showEdit[i]}>
                                                {game.player1.username}
                                            </ToggleDisplay>
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>
                                                <EditUsernameSelect
                                                    players={this.state.players}
                                                    onOptionSelected={(event) => this.onChangePlayer1(
                                                        event)}
                                                    currentPlayer={this.state.editPlayer1}
                                                    className="editGamePlayer1"/>
                                            </ToggleDisplay>
                                        </div>
                                    </Td>
                                    <Td column="Player 2" className="player1Cell"
                                        value={game.player2.username}
                                        filterFunction={String}
                                    >
                                        <div>
                                            <ToggleDisplay
                                                show={!this.state.showEdit[i]}>
                                                {game.player2.username}
                                            </ToggleDisplay>
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>
                                                <EditUsernameSelect
                                                    players={this.state.players}
                                                    onOptionSelected={(event) => this.onChangePlayer2(
                                                        event)}
                                                    currentPlayer={this.state.editPlayer2}
                                                    className="editGamePlayer2"/>
                                            </ToggleDisplay>
                                        </div>
                                    </Td>
                                    <Td column="Score 1" className="player1Cell"
                                        value={game.score1}>
                                        <div>
                                            <ToggleDisplay
                                                show={!this.state.showEdit[i]}>
                                                {game.score1}
                                            </ToggleDisplay>
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>
                                                <div>
                                                    <EditScoreInput
                                                        onChange={(event) => this.onChangeScore1(
                                                            event)}
                                                        score={this.state.editScore1} scoreState={this.state.editScore1}/>
                                                </div>
                                            </ToggleDisplay>
                                        </div>
                                    </Td>
                                    <Td column="Score 2" className="player1Cell"
                                        value={game.score2}>
                                        <div>
                                            <ToggleDisplay
                                                show={!this.state.showEdit[i]}>
                                                {game.score2}
                                            </ToggleDisplay>
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>
                                                <div>
                                                    <EditScoreInput
                                                        onChange={(event) => this.onChangeScore2(
                                                            event)}
                                                        score={this.state.editScore2}/>
                                                </div>
                                            </ToggleDisplay>
                                        </div>
                                    </Td>

                                    <Td column="Actions">
                                        <div>
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>
                                                <div>
                                                    <input type="button"
                                                           value="Submit"
                                                           className="editButton"
                                                           onClick={this.handleSubmit}/>
                                                    &nbsp;
                                                </div>
                                            </ToggleDisplay>
                                            <div className="editContainer">
                                                <a style={{cursor: 'pointer'}}
                                                   onClick={() => this.showEditGame(
                                                       i,
                                                       game)}>Edit</a>
                                            </div>
                                            &nbsp;
                                            <ToggleDisplay
                                                show={this.state.showEdit[i]}>

                                                <div className="cancelContainer">
                                                    <a style={{cursor: 'pointer'}}
                                                       onClick={() => this.cancelEditGame(
                                                           i)}>Cancel</a>
                                                </div>
                                            </ToggleDisplay>
                                            &nbsp;
                                            <div className="deleteContainer">
                                                <a style={{cursor: 'pointer'}}
                                                   onClick={() => this.deleteGame(
                                                       game.iD)}>Delete</a>
                                            </div>
                                        </div>
                                    </Td>

                                </Tr>
                            })}
                        </Table>
                    </div>
                </div>

            }
            </div>


        );
    }
}

export const EditUsernameTypeAhead = (props) => {
    let options = props.players;

    let displayOption = (option) => {
        return option.username;
    };

    return (
        <Typeahead
            options={options}
            displayOption={displayOption}
            filterOption='username'
            value={props.currentPlayer}
            id={props.id}
            onOptionSelected={props.onOptionSelected}
            customClasses={{
                input: "typeahead-text-input" + props.id,
                results: "typeahead-list__container" + props.id,
                listItem: "typeahead-list__item" + props.id,
                hover: "typeahead-active" + props.id,
            }}
        />
    );
};

export const EditUsernameSelect = (props) => {
    let options = props.players;

    let displayOption = (option) => {
        return option.username;
    };
    return  <Select
        options={options}
        filterOption={displayOption}
        value={props.currentPlayer}
        valueKey="username"
        labelKey="username"
        id={props.id}
        onChange={props.onOptionSelected}
        className={props.className}
        customClasses={{
            input: "typeahead-text-input" + props.id,
            results: "typeahead-list__container" + props.id,
            listItem: "typeahead-list__item" + props.id,
            hover: "typeahead-active" + props.id,
        }}
    />
};

const EditScoreInput = (props) => {
    return (
        <input className="editScoreInput" type="number"
               value={props.score}  onChange={props.onChange}/>
    )
};

export class DateInput extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return <DatePicker
            withPortal
            showTimeSelect
            selected={moment(this.props.startDate)}
            onChange={this.props.onChange}
            className="editDateInput"
            shouldCloseOnSelect={true}
            timeIntervals={15}
        />;
    }
}


