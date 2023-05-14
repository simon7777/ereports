import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import {BrowserRouter as Router, Redirect, Route} from 'react-router-dom';
import LoginPage from "./components/login/LoginPage";
import {isAuthenticated} from "./components/utils/AuthenticationUtil";

const PrivateRoute = ({component: Component, ...rest}) => (
    <Route
        {...rest}
        render={props =>
            isAuthenticated() ? (
                <Component {...props} />
            ) : (
                <Redirect to="/login"/>
            )
        }
    />
);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <Router>
        <Route exact path="/login" component={LoginPage}/>
        <PrivateRoute path="/" component={App}/>
    </Router>
);

