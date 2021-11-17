import JobAppListProps from "./JobAppListProps";
import { Redirect, withRouter } from "react-router-dom";
import { Button, Container, Grid } from "@material-ui/core";
import IJobApp from "../../models/IJobApp";
import { useEffect, useState } from "react";
import { displayJobAppCard } from "./JobAppCard";
import NavBar from "../navbar/NavBar";
import useJobAppListStyles from "./JobAppListStyles";
import getJobAppCard from "../../functions/networkCalls/getJobAppCard";
import URLPath from "../../enums/URLPath_enum";
import { getCookie } from "../../functions/utils/cookieUtil";
import CookieFields from "../../enums/CookieFields_enum";
import HttpStatusCode from "../../enums/HttpStatusCode_enum";
import RoutePath from "../../enums/RoutePath_enum";

const JobAppList: React.FC<JobAppListProps> = props => {
    const classes = useJobAppListStyles();

    const [jobApps, setJobApps] = useState<IJobApp[]>(props.jobApps);
    const [shouldRedirect, setShouldRedirect] = useState(false);
    const [redirectDestination, setRedirectDestination] = useState("");

    useEffect( () => {
        const initData = (newJobApps: IJobApp[] = []) => {
            setJobApps(newJobApps);
        };

        const handleData = async (sessionId: string = "") => {
            try {
                if (sessionId !== null && sessionId !== "") {
                    const response = await getJobAppCard(URLPath.getJobAppCard, sessionId);
                    if (response.data !== undefined && !response.isError()) {
                        initData(response.data);
                    } else if (response.isStatusCodeEqual(HttpStatusCode.forbidden)) {
                        console.log(`Session has expired`);
                        initData();
                        setRedirect({shouldRedirect: true, destination: RoutePath.login});
                    }
                } else {
                    initData();
                }
            } catch (error: any) {
                initData();
            }
        };

        const ID: string = getCookie(CookieFields.sessionId) ?? "";

        handleData(ID);
    }, []);

    const onAddJobAppButtonPressed = () => {

    }

    const setRedirect = (redirect: { shouldRedirect: boolean, destination: string }) => {
        setShouldRedirect(redirect.shouldRedirect);
        setRedirectDestination(redirect.destination);
    }

    const redirect = (): JSX.Element | null => {
        if (shouldRedirect && redirectDestination !== "") {
            return <Redirect to={redirectDestination} />
        } else {
            return null;
        }
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
            {shouldRedirect && redirect()}
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