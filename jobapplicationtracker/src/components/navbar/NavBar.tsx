import { AppBar, Toolbar, Typography } from "@material-ui/core";
import navBarStyles from "./navbarstyles";
import NavBarProps from "./NavBarProps";

const NavBar = (props: NavBarProps): JSX.Element => {
    const styles = navBarStyles();

    return (
        <div className={styles.root}>
            <AppBar position="static" className={styles.background}>
                <Toolbar>
                    <Typography variant="h6" className={styles.title}>
                        {props.navBarTitle}
                    </Typography>
                </Toolbar>
            </AppBar>
        </div>
    );
}

export default NavBar;