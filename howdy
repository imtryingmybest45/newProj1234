import MassacreTheNewAmericanDream from './MassacreTheNewAmericanDream';
import WhereAreYouNowJBiebs from './WhereAreYouNowJBiebs';
import WhereAreUNow from './WhereAreUNow';
import HerHeartIsBreaking from './HerHeartIsBreaking';
import TakenItsToll from './TakenItsToll';
import ThisLoveHas from './ThisLoveHas';
import GoodMorningHon from './GoodMorningHon';
import Howdy from './Howdy';
import HelpMePlease from './HelpMePlease';
import WowYouCanReallyDance from './WowYouCanReallyDance';
import TheDescent from './TheDescent';
import Sinister from './Sinister';
import TheAutopsyofJaneDoe from './TheAutopsyofJaneDoe';
import TheConjuring4 from './TheConjuring4';
import TheConjuring from './TheConjuring';
import TheConjuring3 from './TheConjuring3';
import TheConjuring2 from './TheConjuring2';
import Weapons from './Weapons';
import {Routes} from 'react-router-dom';
import {Route} from 'react-router-dom';
import {useState} from 'react';
import axios from 'axios';
import {Link} from 'react-router-dom';

function Home(props){
  let tfVar = props.name;
  let setTFVar = props.age;
  const [stvar, setMess] = useState('');
  const handleClick = (paramm) => {
    //axios.post('http://localhost:8080/ftpEndpoint',paramm)
    //axios.post('https://tryingthisagain-e6f8d0gqfmgsevft.eastus2-01.azurewebsites.net/ftpEndpoint',paramm)
    axios.get('http://localhost:8080/genericEndpoint')
    .then(response => setMess(response.data))

    setTFVar(false);
  }
  const linksData = [
  { id: 1, text: 'Weapons', to: '/Weapons' },
  { id: 2, text: 'The Conjuring 2', to: '/TheConjuring2' },
  { id: 3, text: 'The Conjuring 3', to: '/TheConjuring3' },
  { id: 4, text: 'The Conjuring', to: '/TheConjuring' },
  { id: 5, text: 'The Conjuring 4', to: '/TheConjuring4' },
  { id: 6, text: 'The Autopsy of Jane Doe', to: '/TheAutopsyofJaneDoe' },
  { id: 7, text: 'Sinister', to: '/Sinister' },
  { id: 8, text: 'The Descent', to: '/TheDescent' },
  { id: 9, text: 'Wow You Can Really Dance', to: '/WowYouCanReallyDance' },
  { id: 10, text: 'Help Me Please', to: '/HelpMePlease' },
  { id: 11, text: 'Howdy', to: '/Howdy' },
  { id: 12, text: 'Good Morning Hon', to: '/GoodMorningHon' },
  { id: 13, text: 'This Love Has', to: '/ThisLoveHas' },
  { id: 14, text: 'Taken Its Toll', to: '/TakenItsToll' },
  { id: 15, text: 'Her Heart is Breaking', to: '/HerHeartIsBreaking' },
  { id: 16, text: 'Where Are U Now', to: '/WhereAreUNow' },
  { id: 17, text: 'Where Are You Now J Biebs', to: '/WhereAreYouNowJBiebs' },
  { id: 18, text: 'Massacre the New American Dream', to: '/MassacreTheNewAmericanDream' },
  ];
  return (
    <div>
      {tfVar&&<h1>Welcome to my website.</h1>}
      {tfVar&&<p>This is where I review horror movies.</p>}
      {tfVar&&<p>Click on a movie name to see the review.</p>}
      <Routes>
        <Route path="/Weapons" element={<Weapons name = {stvar}/>} />
        <Route path="/TheConjuring2" element={<TheConjuring2 name = {stvar}/>} />
        <Route path="/TheConjuring3" element={<TheConjuring3 name = {stvar}/>} />
        <Route path="/TheConjuring" element={<TheConjuring name = {stvar}/>} />
        <Route path="/TheConjuring4" element={<TheConjuring4 name = {stvar}/>} />
        <Route path="/TheAutopsyofJaneDoe" element={<TheAutopsyofJaneDoe name = {stvar}/>} />
        <Route path="/Sinister" element={<Sinister name = {stvar}/>} />
        <Route path="/TheDescent" element={<TheDescent name = {stvar}/>} />
        <Route path="/WowYouCanReallyDance" element={<WowYouCanReallyDance name = {stvar}/>} />
        <Route path="/HelpMePlease" element={<HelpMePlease name = {stvar}/>} />
        <Route path="/Howdy" element={<Howdy name = {stvar}/>} />
        <Route path="/GoodMorningHon" element={<GoodMorningHon name = {stvar}/>} />
        <Route path="/ThisLoveHas" element={<ThisLoveHas name = {stvar}/>} />
        <Route path="/TakenItsToll" element={<TakenItsToll name = {stvar}/>} />
        <Route path="/HerHeartIsBreaking" element={<HerHeartIsBreaking name = {stvar}/>} />
        <Route path="/WhereAreUNow" element={<WhereAreUNow name = {stvar}/>} />
        <Route path="/WhereAreYouNowJBiebs" element={<WhereAreYouNowJBiebs name = {stvar}/>} />
        <Route path="/MassacreTheNewAmericanDream" element={<MassacreTheNewAmericanDream name = {stvar}/>} />
      </Routes>
      <nav>
        <ul style={{listStyleType: 'none'}}>
              {linksData.map((link) => (
              <li key={link.id}>
                  {tfVar&&<Link to={link.to} onClick = {()=>handleClick(link.to)}>{link.text}</Link>}
              </li>
              ))}
        </ul>
      </nav>
    </div>
  );
}

export default Home;