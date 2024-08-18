package com.skilrock.retailapp.models.rms;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CashManagementTxnTypeResponseBean {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;

    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;

    @SerializedName("responseData")
    @Expose
    private ResponseData responseData;

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

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public static class ResponseData {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("statusCode")
        @Expose
        private Integer statusCode;
        @SerializedName("data")
        @Expose
        private ArrayList<Data> data = null;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public ArrayList<Data> getData() {
            return data;
        }

        public void setData(ArrayList<Data> data) {
            this.data = data;
        }

        public static class Data {

            @SerializedName("txnTypeName")
            @Expose
            private String txnTypeName;

            @SerializedName("txnTypeCode")
            @Expose
            private String txnTypeCode;

            public String getTxnTypeName() {
                return txnTypeName;
            }

            public void setTxnTypeName(String txnTypeName) {
                this.txnTypeName = txnTypeName;
            }

            public String getTxnTypeCode() {
                return txnTypeCode;
            }

            public void setTxnTypeCode(String txnTypeCode) {
                this.txnTypeCode = txnTypeCode;
            }

            @NonNull
            @Override
            public String toString() {
                return "Data{" + "txnTypeName='" + txnTypeName + '\'' + ", txnTypeCode='" + txnTypeCode + '\'' + '}';
            }
        }

        @NonNull
        @Override
        public String toString() {
            return "ResponseData{" + "message='" + message + '\'' + ", statusCode=" + statusCode + ", data=" + data + '}';
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "CashManagementTxnTypeResponseBean{" + "responseCode=" + responseCode + ", responseMessage='" + responseMessage + '\'' + ", responseData=" + responseData + '}';
    }
}
