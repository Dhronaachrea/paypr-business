package com.skilrock.retailapp.models;

import android.support.annotation.NonNull;

public class ResponseCodeMessageBean {

    private int responseCode;
    private String responseMessage;
    private String respMsg;

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @NonNull
    @Override
    public String toString() {
        return "ResponseCodeMessageBean{" + "responseCode=" + responseCode + ", responseMessage='" + responseMessage + '\'' + ", respMsg='" + respMsg + '\'' + '}';
    }
}
