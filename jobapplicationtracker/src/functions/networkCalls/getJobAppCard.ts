import axios from "axios";
import URLPath from "../../enums/URLPath_enum"
import HttpResponse from "../../models/HttpResponse";

const getJobAppCard = async (url: URLPath, sessionId: string): Promise<HttpResponse<string[]>> => {
    const newURL = getJobAppCardUrl(url, sessionId);

    try {
        const response = await axios.get(newURL);

        return new HttpResponse<string[]>(response.status, "", response.data);
    } catch (error: any) {
        const statusCode = Number(error.response.status);
        const reasonForFailure = String(error.response.data);

        return new HttpResponse<string[]>(statusCode, reasonForFailure, []);
    }
}

const getJobAppCardUrl = (url: URLPath, sessionId: string): string => {
    return `${url}/${sessionId}`;
}
export default getJobAppCard;