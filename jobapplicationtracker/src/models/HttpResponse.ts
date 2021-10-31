class HttpResponse<T> {
    readonly status: number;
    readonly reasonForFailure: string;
    readonly data: T | undefined;

    constructor(status: number, reasonForFailure: string = "", data?: T) {
        this.status = status;
        this.reasonForFailure = reasonForFailure;
        this.data = data;
    }

    isError = (): boolean => {
        return this.status >= 300;
    }

    isStatusCodeEqual = (statusCode: number): boolean => {
        return this.status === statusCode;
    }
}

export default HttpResponse;