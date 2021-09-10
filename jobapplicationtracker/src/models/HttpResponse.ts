class HttpResponse<T> {
    readonly status: number;
    readonly reasonForFailure: string;
    readonly data: T | undefined;

    constructor(status: number, reasonForFailure: string = "", data?: T) {
        this.status = status;
        this.reasonForFailure = reasonForFailure;
        this.data = data;
    }
}

export default HttpResponse;