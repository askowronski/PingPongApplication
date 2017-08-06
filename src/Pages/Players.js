import ToggleDisplay from 'react-toggle-display';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
const Reactable = require('reactable');
require("../stylesheet.css");



export default class PlayersList extends React.Component {
    state = {
      players:[], result:'',showUsername:[],showEditPlayer:[],deletePlayerResponse:''
    };

    componentDidMount = () => {
        jQuery.ajax({

            url: "http://localhost:8080/GetPlayers",
            type:"GET",
            dataType:"json",
            async:false,
            success: function(data){
                this.setState({
                    players:JSON.parse(data.message),
                    result:data.success,
                });
                let showEditArray = [];
                let showUsernameArray = [];
                for(let i = 0; i< JSON.parse(data.message).length; i++){
                   showUsernameArray.push(true);
                   showEditArray.push(false);
                }
                this.setState({
                    showUsername:  showUsernameArray,
                    showEditPlayer: showEditArray,
                });
            }.bind(this)
        });


    };



    showEditPlayer = (index) => {

        let showUsernameArray = this.state.showUsername.concat();
        let showEditArray = this.state.showEditPlayer.concat();
        showUsernameArray[index]=false;
        showEditArray[index]=true;

        this.setState({
            showUsername:showUsernameArray,
            showEditPlayer:showEditArray
        });
    };

    cancelEditPlayer = (index) => {
        let showUsernameArray = this.state.showUsername.concat();
        let showEditArray = this.state.showEditPlayer.concat();
        showUsernameArray[index]=true;
        showEditArray[index]=false;

        this.setState({
            showUsername:showUsernameArray,
            showEditPlayer:showEditArray
        });
    };

    processDeletePlayer = (id,username) => {
        jQuery.ajax({

            url: "http://localhost:8080/DeletePlayer?id="+id+"&username="+username,
            type:"DELETE",
            dataType:"json",
            async:false,
            success: function(data){
                alert(data.message);
                this.componentDidMount();
            }.bind(this)
        });
    };

    render() {
        let Table = Reactable.Table;
        let Tr = Reactable.Tr;
        let Td = Reactable.Td;

        return (
            <div className="tableHolder">
          <Table className="table" border="true" itemsPerPage={4} pageButtonLimit={5}>

              {this.state.players.map((player,i) =>
                  <Tr>
                      <Td column="ID"  >
                          {player.id}
                      </Td>
                      <Td column="Username" >
                          <div>
                          <ToggleDisplay id="usernameToggleDisplay" show={this.state.showUsername[i]}>
                              {player.username}
                          </ToggleDisplay>
                          <ToggleDisplay id="editUsernameToggleDisplay" show={this.state.showEditPlayer[i]}>
                              <EditPlayer username = {player.username} id = {player.id} />
                          </ToggleDisplay>
                          </div>
                      </Td>
                      <Td column="Elo Rating">{player.eloRating.rating}</Td>
                      <Td column="Actions" id={player.id}  >
                          <div>
                              <a style={{cursor: 'pointer'}} onClick={() => this.showEditPlayer(i)} >Edit</a>
                              &nbsp;
                              <a style={{cursor: 'pointer'}} onClick={() => this.processDeletePlayer(player.id,player.username)}>Delete</a>
                              &nbsp;
                              <a style={{cursor: 'pointer'}} onClick={() => this.cancelEditPlayer(i)}>Cancel</a>
                          </div>
                      </Td>
                  </Tr>
              )};
          </Table>
            </div>
        );
    }
}

class EditPlayer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            newUsername: props.username,
            id:props.id,
            result:'',
            messsage:''
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }



    handleChange(event) {
            this.setState({
                newUsername: event.target.value
            });
    };


    handleSubmit(event) {
        alert(this.state.newUsername);

        jQuery.ajax({

            url: "http://localhost:8080/EditPlayer?id="+this.state.id+"&newUsername="+this.state.newUsername,
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

const EditUsernameInput = (props) => {

    const onSubmit = () => {

    };
    return (
        <div id="editUsernameContainer">
            <form onSubmit={onSubmit}>
                <input type="text" defaultValue={props.username} />
                <input type="submit" value="Submit" />
            </form>
        </div>
    );
};




