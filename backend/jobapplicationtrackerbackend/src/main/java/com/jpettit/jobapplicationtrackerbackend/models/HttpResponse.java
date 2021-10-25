package com.jpettit.jobapplicationtrackerbackend.models;

import org.springframework.http.HttpStatus;

public class HttpResponse<T> {
    private final T data;
    private final String errorMessage;

    public HttpResponse(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
