import axios from "axios";
import URLPath from "../../enums/URLPath_enum"
import HttpResponse from "../../models/HttpResponse";
import IJobApp from "../../models/IJobApp";

const getJobAppCard = async (url: URLPath, sessionId: string): Promise<HttpResponse<IJobApp[]>> => {
    const newURL = getJobAppCardUrl(url, sessionId);

    try {
        const response = await axios.get(newURL);
        const data = response.data.value;
        const dataArray = transformResponeToJobAppCards(data);
        return new HttpResponse<IJobApp[]>(response.status, "", dataArray);
    } catch (error: any) {
        const statusCode = Number(error.response.status);
        const reasonForFailure = JSON.stringify(error.response.data);
        console.log(`In error`);
        return new HttpResponse<IJobApp[]>(statusCode, reasonForFailure, []);
    }
}

const getJobAppCardUrl = (url: URLPath, sessionId: string): string => {
    return `${url}/${sessionId}`;
}

const transformResponeToJobAppCards = (data: any) : IJobApp[] => {
    return data.map((t: any) => {
            return {
                jobTitle: String(t.jobTitle),
                company: String(t.companyName),
                numberOfInterviews: Number(t.interviewCount),
                jobAppId: String(t.jobAppId)
            };
    });
}
export default getJobAppCard;