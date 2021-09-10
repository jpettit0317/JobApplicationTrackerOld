import HttpResponse from "../../../models/HttpResponse";
import { expect } from "chai";

describe("HttpResponse tests", () => {
    it(`when passing in status code 200, reasonForFailure "", and data hello, should return said data`, () => {
        const sut = new HttpResponse<string>(200, "", "hello");

        expect(sut.status).to.equal(200);
        expect(sut.reasonForFailure).to.equal("");
        expect(sut.data).to.equal("hello");
    });
});