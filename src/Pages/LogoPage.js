import {
    Header
} from '../ReactComponents/displayComponents.js';
import {Box, Flex, Provider, Text} from "rebass";
// import '../index.css';

const React = require('react');
const jQuery = require('jquery');
const css = require("css-loader");
require("../main/css/index.css");


export default class LogoPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            buttonNames: ["InputPlayer", "Scores", "Players"],
            loading: true,
            areThereGames:true,
            data:''
        };
    }

    componentDidMount = () => {
        jQuery("#homeLogo").hide();
        jQuery("#homeTextBackstop").hide();
        jQuery("#homeTextPingPong").hide();

        jQuery("#homeLogo").fadeIn(6500);
        jQuery("#homeTextBackstop").fadeIn(6500);
        jQuery("#homeTextPingPong").fadeIn(6500);

    };

    changeLoadingState = () => {
        this.setState(
            {
                loading: false
            }
        )
    };

    changeAreThereGamesState = () => {
        let boolean = !this.state.areThereGames;
        this.setState(
            {
                areThereGames: boolean
            }
        )
    };

    render() {

        return (
            <div>
                <Header />
                <br/>
               <div className="homePageContainer">

                   <Provider
                       theme={{
                           font: '"Serif"',
                       }}
                   >
                       <Flex wrap mx={-2} align='center' width='auto'>
                           <Box px={2} py={2} width={1/3}>
                               <Text p={1} color='#000069' bg='white'
                                     f={90}>                                   <p  id="homeTextBackstop" className="homeLogoContainer">
                                   Backstop </p></Text>
                           </Box>
                           <Box px={2} py={2} width={1/3}>

                                   <p className="homeLogoContainer">
                                       <img id="homeLogo" width='100%' src={require('../images/homeLogo2.png')} />
                                   </p>
                           </Box>


                           <Box px={2} py={2} width={1/3}>
                               <Text p={1} color='#000069' bg='white'
                                     f={90}>
                                   <p id="homeTextPingPong" className="homeLogoContainer">
                                       Ping Pong </p>
                               </Text>
                           </Box>
                       </Flex>
                   </Provider>



               </div>


            </div>
        );

    }
}