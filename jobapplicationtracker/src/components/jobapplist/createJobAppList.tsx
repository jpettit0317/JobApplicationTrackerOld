import JobAppList from "./JobAppList";

const createJobAppList = (title: string = ""): JSX.Element => {
    return (
        <JobAppList title={title} />
    );
};

export default createJobAppList;