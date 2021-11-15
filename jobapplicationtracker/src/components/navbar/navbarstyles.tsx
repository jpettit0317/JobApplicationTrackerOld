import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";

const navBarStyles = makeStyles((theme: Theme) =>
    createStyles({
        root: {
            flexGrow: 1,
        },
        menuButton: {
            marginRight: theme.spacing(2),
        },
        title: {
            flexGrow: 1,
        },
        background: {
            backgroundColor: "green",
        },
    }),
);

export default navBarStyles;