package it.lamiapizzeria.responses;

import org.springframework.http.HttpStatus;

public class Payload<T> {
    private T payload;
    private String errorMsg;
    private HttpStatus status;

    public Payload() {
    }

    public Payload(T payload, String errorMsg, HttpStatus status) {
        this.payload = payload;
        this.errorMsg = errorMsg;
        this.status = status;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public T getPayload() {
        return payload;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
