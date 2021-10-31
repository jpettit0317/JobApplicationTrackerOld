import HttpResponse from "../../../models/HttpResponse";
import { expect } from "chai";

describe("HttpResponse tests", () => {
    describe("Init tests", () => {
        it(`when passing in status code 200, reasonForFailure "", and data hello, should return said data`, () => {
            const sut = new HttpResponse<string>(200, "", "hello");

            expect(sut.status).to.equal(200);
            expect(sut.reasonForFailure).to.equal("");
            expect(sut.data).to.equal("hello");
        });
    });

    describe("isError tests", () => {
        it(`when passing in status code of [200, 299], isError should return false`, () => {
            const statusCodes = [200, 299];
            
            statusCodes.forEach((statusCode) => {
                const sut = new HttpResponse<string>(200, "", "");

                expect(sut.isError()).to.be.false;
            });
        });

        it(`when passing in status code of [300, 404], isError should return true`, () => {
            const statusCodes = [300, 404];

            statusCodes.forEach((statusCode) => {
                const sut = new HttpResponse<string>(statusCode, "", "");

                expect(sut.isError()).to.be.true;
            });
        });
    });

    describe(`isStatusCodeEqual tests`, () => {
        it(`when initing status code of 200, passing in 200 to isStatusCodeEqual should return true`, () => {
            const sut = new HttpResponse<string>(200, "", "");
            
            expect(sut.isStatusCodeEqual(200)).to.be.true;
        });

        it(`when initing with status code of 200, passing in 404 to isStatusCodeEqual should return true`, () => {
            const sut = new HttpResponse<string>(200, "", "");
            const NOT_FOUND = 404;

            expect(sut.isStatusCodeEqual(NOT_FOUND)).to.be.false;
        });
    });
});