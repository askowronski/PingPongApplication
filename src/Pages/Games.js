

const React = require('react');
const jQuery = require('jquery');
var _ = require('lodash');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");
var FontAwesome = require('react-fontawesome');



export default class GamesList extends React.Component {
    state = {
        games:[], result:''
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
            }.bind(this)
        });
    };

    goToEditPlayer = () => {

    }

    render() {
        var Table = Reactable.Table;
        var Tr = Reactable.Tr;
        var Td = Reactable.Td;

        var editLinks = <div>
            <a href="">Edit</a>
            &nbsp;
            <a href="">Delete</a>
        </div>
        return (
            <div className="tableHolder">
                <Table className="table" border="true" itemsPerPage={4} pageButtonLimit={5}>

                    {this.state.games.map((game,i) =>
                        <Tr>
                            <Td column="ID" >
                                {game.id}
                            </Td>
                            <Td column="Player 1 Username" data={game.player1.newUsername}>
                                <b>{game.player1.newUsername}</b>
                            </Td>
                            <Td column="Player 2 Username">{game.player2.newUsername}</Td>
                            <Td column="Score 1">{game.score1}</Td>
                            <Td column="Score 2">{game.score2}</Td>
                            <Td column="Actions" data={editLinks} >
                                <a href="">Edit</a>
                            </Td>
                        </Tr>
                    )};
                </Table>
            </div>
        );
    }
}


