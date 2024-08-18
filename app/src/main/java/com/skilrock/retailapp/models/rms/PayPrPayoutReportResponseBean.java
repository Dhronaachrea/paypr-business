package com.skilrock.retailapp.models.rms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayPrPayoutReportResponseBean {
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

    public class ResponseData {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("statusCode")
        @Expose
        private Integer statusCode;
        @SerializedName("data")
        @Expose
        private List<Data> data = null;

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

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }


        public class Data {

            @SerializedName("orgName")
            @Expose
            private String orgName;
            @SerializedName("txnTypeCode")
            @Expose
            private String txnTypeCode;
            @SerializedName("branchName")
            @Expose
            private String branchName;
            @SerializedName("bankName")
            @Expose
            private String bankName;
            @SerializedName("unformattedTxnValue")
            @Expose
            private String unformattedTxnValue;
            @SerializedName("orgId")
            @Expose
            private String orgId;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("txnTypeName")
            @Expose
            private String txnTypeName;
            @SerializedName("txnValue")
            @Expose
            private String txnValue;
            @SerializedName("beneficiaryName")
            @Expose
            private String beneficiaryName;
            @SerializedName("requestId")
            @Expose
            private String requestId;
            @SerializedName("accountNo")
            @Expose
            private String accountNo;
            @SerializedName("doerUserName")
            @Expose
            private String doerUserName;
            @SerializedName("receiptNumber")
            @Expose
            private String receiptNumber;
            @SerializedName("orgDueAmount")
            @Expose
            private String orgDueAmount;
            @SerializedName("txnId")
            @Expose
            private String txnId;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("remark")
            @Expose
            private String remark;


            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getTxnTypeCode() {
                return txnTypeCode;
            }

            public void setTxnTypeCode(String txnTypeCode) {
                this.txnTypeCode = txnTypeCode;
            }

            public String getBranchName() {
                return branchName;
            }

            public void setBranchName(String branchName) {
                this.branchName = branchName;
            }

            public String getBankName() {
                return bankName;
            }

            public void setBankName(String bankName) {
                this.bankName = bankName;
            }

            public String getUnformattedTxnValue() {
                return unformattedTxnValue;
            }

            public void setUnformattedTxnValue(String unformattedTxnValue) {
                this.unformattedTxnValue = unformattedTxnValue;
            }

            public String getOrgId() {
                return orgId;
            }

            public void setOrgId(String orgId) {
                this.orgId = orgId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getTxnTypeName() {
                return txnTypeName;
            }

            public void setTxnTypeName(String txnTypeName) {
                this.txnTypeName = txnTypeName;
            }

            public String getTxnValue() {
                return txnValue;
            }

            public void setTxnValue(String txnValue) {
                this.txnValue = txnValue;
            }

            public String getBeneficiaryName() {
                return beneficiaryName;
            }

            public void setBeneficiaryName(String beneficiaryName) {
                this.beneficiaryName = beneficiaryName;
            }

            public String getRequestId() {
                return requestId;
            }

            public void setRequestId(String requestId) {
                this.requestId = requestId;
            }

            public String getAccountNo() {
                return accountNo;
            }

            public void setAccountNo(String accountNo) {
                this.accountNo = accountNo;
            }

            public String getDoerUserName() {
                return doerUserName;
            }

            public void setDoerUserName(String doerUserName) {
                this.doerUserName = doerUserName;
            }

            public String getReceiptNumber() {
                return receiptNumber;
            }

            public void setReceiptNumber(String receiptNumber) {
                this.receiptNumber = receiptNumber;
            }

            public String getOrgDueAmount() {
                return orgDueAmount;
            }

            public void setOrgDueAmount(String orgDueAmount) {
                this.orgDueAmount = orgDueAmount;
            }

            public String getTxnId() {
                return txnId;
            }

            public void setTxnId(String txnId) {
                this.txnId = txnId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }

    }
}
