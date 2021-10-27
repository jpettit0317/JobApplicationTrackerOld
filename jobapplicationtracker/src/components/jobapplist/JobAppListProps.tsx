import { RouteComponentProps } from 'react-router-dom';
import IJobApp from "../../models/IJobApp";
interface JobAppListProps extends RouteComponentProps {
    jobApps: IJobApp[];
};

export default JobAppListProps;