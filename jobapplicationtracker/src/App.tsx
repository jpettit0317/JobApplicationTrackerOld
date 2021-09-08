import React from 'react';
import logo from './logo.svg';
import './App.css';
import SignUpPage from './components/signup/SignUpPage';
import RoutePath from './enums/RoutePath_enum';

import { BrowserRouter, Route, Switch } from 'react-router-dom';

function App() {
  function createSignUpPage(): JSX.Element {
    return (
      <SignUpPage fullName="" username="" email="" password="" confirmedPassword=""/>
    );
  }

  return (
    <div className="App">
      <header className="App-header">
        <BrowserRouter>
           <Switch>
             <Route path={RoutePath.signup} component={ () => createSignUpPage() } />
             <Route path={RoutePath.home} component={ () => createSignUpPage() } />
           </Switch>
        </BrowserRouter>
      </header>
    </div>
  );
}

export default App;
