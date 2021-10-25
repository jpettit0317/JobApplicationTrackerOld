import LoginPageProps from "./LoginPageProps";
import React, { useState } from "react";
import { withRouter } from "react-router";
import NavBar from "../navbar/NavBar";
import { Button, Container, CssBaseline, Grid, Link, TextField, Typography } from "@material-ui/core";
import loginStyles from "../../styles/loginpagestyles";
import LoginPageTestIds from "./LoginPageTestIds_enum";
import LoginPageErrorPair from "../../models/LoginPageErrorPair";
import Login from "../../models/Login";

const LoginPage: React.FC<LoginPageProps> = props => {
    const classes = loginStyles();
    const signUpLink: string = "Don't have an account? Sign Up!";
    const loginHeader: string = "Log in";
    const loginButton: string = "Log In";

    const [username, setUsername] = useState(props.username);
    const [password, setPassword] = useState(props.password);

    const [userNameError, setUserNameError] = useState("");
    const [passwordError, setPasswordError] = useState("");

    const areThereErrors = (): boolean => {
        return userNameError !== "" || passwordError !== "";
    }

    const setErrors = (errors: LoginPageErrorPair) => {
        setUserNameError(errors.usernameError);
        setPasswordError(errors.passwordError);
    }

    const renderUsernameField = (): JSX.Element => {
        if (userNameError === "") {
            return (
                <TextField
                    autoComplete="uname"
                    name="userName"
                    variant="outlined"
                    required
                    fullWidth
                    label="Username"
                    inputProps={{ "data-testid": LoginPageTestIds.userName }}
                    onChange={(e) => setUsername(e.target.value)}
                />
            );
        } else {
            return (
                <TextField
                    autoComplete="uname"
                    name="userName"
                    variant="outlined"
                    required
                    fullWidth
                    label="Username"
                    inputProps={{ "data-testid": LoginPageTestIds.userName }}
                    onChange={(e) => setUsername(e.target.value)}
                    error
                    helperText={userNameError}
                />
            );
        }
    }

    const renderPasswordField = (): JSX.Element => {
        if(passwordError === "") {
            return (
                <TextField
                    autoComplete="password"
                    name="password"
                    variant="outlined"
                    required
                    fullWidth
                    label="Password"
                    inputProps={{ "data-testid": LoginPageTestIds.password }}
                    type="password"
                    onChange={(e) => setPassword(e.target.value)}
                />
            );
        } else {
            return (
                <TextField
                    autoComplete="password"
                    name="password"
                    variant="outlined"
                    required
                    fullWidth
                    label="Password"
                    inputProps={{ "data-testid": LoginPageTestIds.password }}
                    type="password"
                    onChange={(e) => setPassword(e.target.value)}
                    error
                    helperText={passwordError}
                />
            );
        }
    }

    const handleLogin = () => {
        console.log(`Username: ${username}, Password: ${password}`);
    }

    const onSubmitButtonPressed = () => {
        const login = new Login(username, password);
        
        const errors = login.validateLogin();
        setErrors(errors);

        if (areThereErrors()) {
            return;
        }
        handleLogin();
    }

    return (
        <div>
            <div>
                <NavBar navBarTitle="JobApplicationTracker" />
                <Container component="main" maxWidth="xs">
                    <CssBaseline />
                    <div className={classes.paper}>
                        <Typography component="h1" variant="h5">
                            {loginHeader}
                        </Typography>
                        <form className={classes.form} noValidate>
                            <Grid container spacing={2}>
                                <Grid item xs={12}>
                                    {renderUsernameField()}
                                </Grid>
                                <Grid item xs={12}>
                                    {renderPasswordField()}
                                </Grid>
                                <Grid item xs={12}>
                                    <Button
                                        type="button"
                                        fullWidth
                                        variant="contained"
                                        color="primary"
                                        className={classes.submit}
                                        onClick={onSubmitButtonPressed}
                                    >
                                        {loginButton}
                                    </Button>
                                </Grid>
                            </Grid>
                            <Grid container justifyContent="flex-end">
                                <Grid item>
                                    <Link href="#" variant="body2">
                                        {signUpLink}
                                    </Link>
                                </Grid>
                            </Grid>
                        </form>
                    </div>
                </Container>
            </div>
            );
        </div>
    );
};

export default withRouter(LoginPage);