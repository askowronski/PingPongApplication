const React = require('react');
const ReactDOM = require('react-dom')
const jQuery = require('jquery')

const Button = (props) => {
    return (
      <button onClick={()=> props.getGamesPlayed(2)}> Get Player </button>
    );
}

const Display = (props) => {

    return (
        <div>
      <span>Games Played</span>
            <span> equals + {props.gamesPlayed(4)} </span>
        </div>

    );
}

const input = (props) => {
    return (
        <input id="idInput" type='submit' >input</input>
    );
}

class App extends React.Component {
    state = {
        gamesPlayed:0,
        averageScore: 0,
    };

    getGamesPlayed = (id) => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayer?id="+id,
            dataType: 'json',
            cache: false,
            error: function (xhr, status, err) {
                console.error("http://localhost:8080/GetPlayer?id="+id, status, err.toString());
            }
        }).then(function (data) {
            this.setState({
                userName: JSON.parse(data.message).username
            })
            return data.message
        }.bind(this));
    };

    render() {
        const {gamesPlayed} = this.state;

        return(
           <div>
               {/*<Button getGamesPlayed={this.getGamesPlayed} gamesPlayed = {gamesPlayed} />*/}
               {/*<Display gamesPlayed={gamesPlayed}/>*/}
               {/*<input />*/}
               {/*<form id="idInputForm" action={}>*/}
                   {/*Player ID: <input type="text" name="id" /><br/>*/}
                   {/*<input type="submit" value="Submit"  />*/}
               {/*</form>*/}
               <IDForm />
               {/*<Display gamesPlayed={gamesPlayed} />*/}
           </div>
        );
    }
}

class IDForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '',
            userName:'huh'
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    getGamesPlayed = (id) => {

        jQuery.ajax({

            url: "http://localhost:8080/GetPlayer?id="+id,
            dataType: 'json',
            cache: false,

        }).then(function (data) {
            window.alert(data.message);
            this.setState({

                userName:data.message
            })
            if(this.userName===null){
                this.setState({
                    userName: "User Not Found"
                })

            }
        }.bind(this));
    };

    handleChange(event) {
        this.setState({
            value: event.target.value
        });
    }

    handleSubmit(event) {
        console.log(this.state.value);
        {this.getGamesPlayed(this.state.value)};
    }

    render() {
        return (
            <div>
            <form onSubmit={this.handleSubmit}>
                <label>
                    Name:
                    <input type="text" value={this.state.value} onChange={this.handleChange} />
                </label>
                <input type="submit" value="Submit" />
            </form>

            <Display gamesPlayed={this.getGamesPlayed} />
            </div>
        );
    }
}

ReactDOM.render(<App/>,document.getElementById('root'));


