package com.skilrock.retailapp.models.scratch;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyTicketResponseNewBean {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("ticketNumber")
    @Expose
    private String ticketNumber;
    @SerializedName("virnNumber")
    @Expose
    private String virnNumber;
    @SerializedName("winningAmount")
    @Expose
    private String winningAmount;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getVirnNumber() {
        return virnNumber;
    }

    public void setVirnNumber(String virnNumber) {
        this.virnNumber = virnNumber;
    }

    public String getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(String winningAmount) {
        this.winningAmount = winningAmount;
    }

    @NonNull
    @Override
    public String toString() {
        return "VerifyTicketResponseNewBean{" + "responseCode=" + responseCode + ", responseMessage='" + responseMessage + '\'' + ", ticketNumber='" + ticketNumber + '\'' + ", virnNumber='" + virnNumber + '\'' + ", winningAmount=" + winningAmount + '}';
    }
}
