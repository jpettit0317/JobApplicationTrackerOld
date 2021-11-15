import IJobApp from "../../models/IJobApp";
import JobAppList from "./JobAppList";

const createJobAppList = (jobApps: IJobApp[] = []): JSX.Element => {
    return (
        <JobAppList jobApps={jobApps} />
    );
};

export default createJobAppList;