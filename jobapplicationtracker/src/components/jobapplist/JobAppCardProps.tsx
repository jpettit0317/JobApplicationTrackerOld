import IJobApp from "../../models/IJobApp";

interface JobAppCardProps {
    jobApp: IJobApp;
    index: number;
    onDelete: (index: number) => void;
}

export default JobAppCardProps;