import React from 'react';
import logo from './logo.svg';
import './App.css';
import SignUpPage from './components/signup/SignUpPage';
import RoutePath from './enums/RoutePath_enum';

import { BrowserRouter, Route, Switch } from 'react-router-dom';
import createSignUpPage from './components/signup/createSignUpPage';
import createLoginPage from './components/login/createLoginPage';
import createJobAppList from './components/jobapplist/createJobAppList';

function App() {

  return (
    <div>
        <BrowserRouter>
           <Switch>
             <Route exact path={RoutePath.home} component={() => createLoginPage()} />
             <Route exact path={RoutePath.jobapplist} component={() => createJobAppList()} />
             <Route exact path={RoutePath.signup} component={ () => createSignUpPage() } />
             <Route exact path={RoutePath.login} component={ () => createLoginPage() } />
           </Switch>
        </BrowserRouter>
    </div>
  );
}

export default App;
