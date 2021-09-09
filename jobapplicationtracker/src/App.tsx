import React from 'react';
import logo from './logo.svg';
import './App.css';
import SignUpPage from './components/signup/SignUpPage';
import RoutePath from './enums/RoutePath_enum';

import { BrowserRouter, Route, Switch } from 'react-router-dom';
import createSignUpPage from './components/signup/createSignUpPage';

function App() {

  return (
    <div>
        <BrowserRouter>
           <Switch>
             <Route path={RoutePath.signup} component={ () => createSignUpPage() } />
             <Route path={RoutePath.home} component={ () => createSignUpPage() } />
           </Switch>
        </BrowserRouter>
    </div>
  );
}

export default App;
