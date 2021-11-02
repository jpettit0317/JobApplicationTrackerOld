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
import CookieFields from "../../enums/CookieFields_enum";

const JobAppList: React.FC<JobAppListProps> = props => {
    const classes = useJobAppListStyles();

    const [jobApps, setJobApps] = useState<IJobApp[]>(props.jobApps);

    useEffect( () => {
        fetchData();
    }, []);
   
    const fetchData = () => {
        const sessionId = getCookie(CookieFields.sessionId);
        handleData(sessionId ?? "");
    }

    const handleData = async (sessionId: string) => {
        try {
            if (sessionId !== null && sessionId !== "") {
                const response = await getJobAppCard(URLPath.getJobAppCard, sessionId);
                if (response.data !== undefined) {
                    console.log(`Response is ${JSON.stringify(response)}`);
                    initData(response.data);
                }
            } else {
                initData([]);
            }
        } catch (error: any) {
            console.log(`Error: ${JSON.stringify(error)}`);
            initData([]);
        }
    }

    const initData = (newJobApps: IJobApp[]) => {
        setJobApps(newJobApps);
    }

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