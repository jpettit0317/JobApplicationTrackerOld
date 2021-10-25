import LoginPageProps from "./LoginPageProps";
import React, { useState } from "react";
import { withRouter } from "react-router";

const LoginPage: React.FC<LoginPageProps> = props => {
    return (
        <div>
            <h1>Hello from LoginPage!</h1>
        </div>
    );
};

export default withRouter(LoginPage);