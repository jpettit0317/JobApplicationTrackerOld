import IJobApp from "../../models/IJobApp";
import JobAppList from "./JobAppList";
import fakeIJobApps from "./fakejobapps";

const createJobAppList = (jobApps: IJobApp[] = fakeIJobApps): JSX.Element => {
    return (
        <JobAppList jobApps={jobApps} />
    );
};

export default createJobAppList;