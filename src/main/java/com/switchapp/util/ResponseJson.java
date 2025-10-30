package com.switchapp.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ResponseJson implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("return_code")
    private int returnCode;
    @JsonProperty("error_msg")
    private String errorMessage;
    @JsonProperty("return_body")
    private Object returnBody;

    public ResponseJson() {
        super();
    }

    public ResponseJson(int returnCode, String errorMessage) {
        super();
        this.returnCode = returnCode;
        this.errorMessage = errorMessage;
    }

    public ResponseJson(int returnCode) {
        super();
        this.returnCode = returnCode;
    }

    public ResponseJson(int returnCode, String errorMessage, Object returnBody) {
        super();
        this.returnCode = returnCode;
        this.errorMessage = errorMessage;
        this.returnBody = returnBody;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getReturnBody() {
        return returnBody;
    }

    public void setReturnBody(Object returnBody) {
        this.returnBody = returnBody;
    }

}
