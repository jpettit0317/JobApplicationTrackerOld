import JobAppListProps from "./JobAppListProps";
import { withRouter } from "react-router-dom";
import { Button, Container, Grid } from "@material-ui/core";
import IJobApp from "../../models/IJobApp";
import { useEffect, useState } from "react";
import { JobAppCard, displayJobAppCard } from "./JobAppCard";
import NavBar from "../navbar/NavBar";
import useJobAppListStyles from "./JobAppListStyles";
import getJobAppCard from "../../functions/networkCalls/getJobAppCard";
import URLPath from "../../enums/URLPath_enum";
import { getCookie } from "../../functions/utils/cookieUtil";

const JobAppList: React.FC<JobAppListProps> = props => {
    const classes = useJobAppListStyles();

    const [jobApps, setJobApps] = useState<IJobApp[]>(props.jobApps);
    
    const handleData = async () => {
        const sessionId = getCookie("sessionId");

        try {
            if (sessionId !== null) {
                const response = await getJobAppCard(URLPath.getJobAppCard, sessionId);
                console.log(`JobApps: ${JSON.stringify(response.data)}`);
            } else {
                setJobApps([]);
            }
        } catch (error: any) {
            console.log(`Error: ${JSON.stringify(error)}`);
            setJobApps([]);
        }
    }

    useEffect( () => {
        console.log("In use effect");

        handleData();
    }, []);
    
    const onAddJobAppButtonPressed = () => {

    }

    const onDeleteJobAppPressed = (index: number) => {
        const jobAppToDelete: IJobApp = jobApps[index];

        console.log(`Deleting job app with id ${jobAppToDelete.jobAppId}`);
        const newJobApps = jobApps.filter(i =>  i.jobAppId !== jobAppToDelete.jobAppId );

        setJobApps(newJobApps);
    }

    return (
        <div>
            <NavBar navBarTitle="JobApplicationTracker" />
            <Container className={classes.cardGrid} maxWidth="md">
                <Grid container spacing={4}>
                    <Grid item xs={12}>
                        <Button size="small" color="primary" onClick={onAddJobAppButtonPressed}>
                            Add Item
                        </Button>
                    </Grid>
                    {jobApps.map((jobApp, index) => (
                        <Grid item key={jobApp.jobAppId} xs={12} md={4}>
                            {displayJobAppCard(jobApp, index, onDeleteJobAppPressed)}
                        </Grid>
                    ))}
                </ Grid>
            </ Container>
        </div>
    );
}

export default withRouter(JobAppList);