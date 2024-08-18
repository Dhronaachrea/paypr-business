package com.skilrock.retailapp.models.ola;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayprPayoutResponseBean {
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("responseData")
    @Expose
    private PayprBankDetailResponseBean.ResponseData responseData;

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

    public PayprBankDetailResponseBean.ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(PayprBankDetailResponseBean.ResponseData responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("statusCode")
        @Expose
        private Integer statusCode;
        @SerializedName("data")
        @Expose
        private PayprBankDetailResponseBean.ResponseData.Data data;

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

        public PayprBankDetailResponseBean.ResponseData.Data getData() {
            return data;
        }

        public void setData(PayprBankDetailResponseBean.ResponseData.Data data) {
            this.data = data;
        }

        public class Data {

            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("orgName")
            @Expose
            private String orgName;
            @SerializedName("balancePostTxn")
            @Expose
            private String balancePostTxn;
            @SerializedName("remark")
            @Expose
            private String remark;
            @SerializedName("orgAddress")
            @Expose
            private String orgAddress;
            @SerializedName("raisedBy")
            @Expose
            private String raisedBy;
            @SerializedName("transactionId")
            @Expose
            private String transactionId;
            @SerializedName("receiptNumber")
            @Expose
            private String receiptNumber;

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getBalancePostTxn() {
                return balancePostTxn;
            }

            public void setBalancePostTxn(String balancePostTxn) {
                this.balancePostTxn = balancePostTxn;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getOrgAddress() {
                return orgAddress;
            }

            public void setOrgAddress(String orgAddress) {
                this.orgAddress = orgAddress;
            }

            public String getRaisedBy() {
                return raisedBy;
            }

            public void setRaisedBy(String raisedBy) {
                this.raisedBy = raisedBy;
            }

            public String getTransactionId() {
                return transactionId;
            }

            public void setTransactionId(String transactionId) {
                this.transactionId = transactionId;
            }

            public String getReceiptNumber() {
                return receiptNumber;
            }

            public void setReceiptNumber(String receiptNumber) {
                this.receiptNumber = receiptNumber;
            }

        }
    }


}
