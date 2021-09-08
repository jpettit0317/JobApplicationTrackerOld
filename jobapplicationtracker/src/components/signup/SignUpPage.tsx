import SignUpProps from "./SignUpPageProps";
import SignUpPageTestIds from "./SignUpPageTestIds_enum";
import signUpStyles from "../../styles/signuppagestyles";

import { withRouter } from "react-router";

import { Box, Grid, Container, TextField, makeStyles, Button, Typography, CssBaseline, Link} from "@material-ui/core"; 
import NavBar from "../navbar/NavBar";

const SignUpPage: React.FC<SignUpProps> = props => {
    const classes = signUpStyles();

    const renderUsernameField = (): JSX.Element => {
        return (
            <TextField
                autoComplete="uname"
                name="userName"
                variant="outlined"
                required
                fullWidth
                label="Username"
                inputProps={{ "data-testid": SignUpPageTestIds.userName }}
            />
        )
    }

    const renderEmailField = (): JSX.Element => {
        return (
            <TextField
                autoComplete="email"
                name="email"
                variant="outlined"
                required
                fullWidth
                label="Email Address"
                inputProps={{ "data-testid": SignUpPageTestIds.email }}
            />
        )
    }

    const renderPasswordField = (): JSX.Element => {
        return (
            <TextField
                autoComplete="password"
                name="password"
                variant="outlined"
                required
                fullWidth
                label="Password"
                inputProps={{ "data-testid": SignUpPageTestIds.password }}
                type="password"
            />
        )
    }

    const renderConfirmPasswordField = (): JSX.Element => {
        return (
            <TextField
                autoComplete="password"
                name="confirmPassword"
                variant="outlined"
                required
                fullWidth
                label="Confirm Password"
                inputProps={{ "data-testid": SignUpPageTestIds.confirmPassword }}
            />
        )
    }

    const onSubmitButtonPressed = () => {

    };

    return ( 
        <div>
            <NavBar navBarTitle="JobApplicationTracker" />
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <div className={classes.paper}>
                    <Typography component="h1" variant="h5">
                        Sign up
                    </Typography>
                    <form className={classes.form} noValidate>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                               {renderUsernameField()} 
                            </Grid>
                            <Grid item xs={12}>
                                {renderEmailField()}
                            </Grid>
                            <Grid item xs={12}>
                                {renderPasswordField()}
                            </Grid>
                            <Grid item xs={12}>
                                {renderConfirmPasswordField()}
                            </Grid>
                            <Grid item xs={12}> 
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    color="primary"
                                    className={classes.submit}
                                > 
                                Sign Up 
                                </Button>
                            </Grid>
                        </Grid>
                        <Grid container justifyContent="flex-end">
                            <Grid item>
                                <Link href="#" variant="body2">
                                    Already have an account? Sign in
                                </Link>
                            </Grid>
                        </Grid>
                    </form>
                </div>
            </Container>
        </div>
    );
};

export default withRouter(SignUpPage);