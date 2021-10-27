import { Button, Card, CardActions, CardContent, Typography } from "@material-ui/core";
import { PinDropSharp } from "@material-ui/icons";
import IJobApp from "../../models/IJobApp";
import JobAppCardProps from "./JobAppCardProps";
import useJobAppCardStyles from "./JobAppCardStyles";


export const JobAppCard = (props: JobAppCardProps): JSX.Element => {
    const classes = useJobAppCardStyles();

    const buttonLabels = {
        view : "View",
        edit : "Edit",
        delete: "Delete"
    };

    const onViewPressed = () => {

    }

    const onEditPressed = () => {

    } 

    const onDeletePressed = () => {
        const jobIndex = props.index;

        props.onDelete(jobIndex);
    }

    const getInterviews = (): string => {
        if (props.jobApp.numberOfInterviews == 1) {
            return `${props.jobApp.numberOfInterviews} interview`;
        } else {
            return `${props.jobApp.numberOfInterviews} interviews`;
        }
    }

    return (
        <Card className={classes.card}>
            <CardContent className={classes.cardContent}>
                <Typography gutterBottom variant="h5" component="h2">
                    {props.jobApp.jobTitle}
                </Typography>
                <Typography>
                    {props.jobApp.company}
                </Typography>
                <Typography>
                    {getInterviews()}
                </Typography>
            </CardContent>
            <CardActions>
                <Button size="small" color="primary" onClick={onViewPressed}>
                    {buttonLabels.view}
                </Button>
                <Button size="small" color="primary" onClick={onEditPressed}>
                    {buttonLabels.edit}
                </Button>
                <Button size="small" color="secondary" onClick={onDeletePressed}>
                    {buttonLabels.delete}
                </Button>
            </CardActions>
        </Card>
    );
}

export const displayJobAppCard = (jobApp: IJobApp, index: number,
    onDeletePressed: (index: number) => void): JSX.Element => {
    return (
        <JobAppCard index={index} jobApp={jobApp} onDelete={onDeletePressed}/>
    );
}