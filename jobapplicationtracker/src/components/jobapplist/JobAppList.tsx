import JobAppListProps from "./JobAppListProps";
import { withRouter } from "react-router-dom";

const JobAppList: React.FC<JobAppListProps> = props => {
    return (
        <div>
            <h1>Job App List.</h1>
        </div>
    );
}

export default withRouter(JobAppList);