package com.skilrock.retailapp.models.rms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayPrPayoutReportRequestBean {
    @SerializedName("domainId")
    @Expose
    private Integer domainId;
    @SerializedName("orgTypeCode")
    @Expose
    private String orgTypeCode;
    @SerializedName("fromDate")
    @Expose
    private String fromDate;
    @SerializedName("toDate")
    @Expose
    private String toDate;
    @SerializedName("orgId")
    @Expose
    private String orgId;
    @SerializedName("txnTypeCode")
    @Expose
    private String txnTypeCode;

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getOrgTypeCode() {
        return orgTypeCode;
    }

    public void setOrgTypeCode(String orgTypeCode) {
        this.orgTypeCode = orgTypeCode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTxnTypeCode() {
        return txnTypeCode;
    }

    public void setTxnTypeCode(String txnTypeCode) {
        this.txnTypeCode = txnTypeCode;
    }

    @Override
    public String toString() {
        return "PayPrPayoutReportRequestBean{" +
                "domainId=" + domainId +
                ", orgTypeCode='" + orgTypeCode + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", orgId='" + orgId + '\'' +
                ", txnTypeCode='" + txnTypeCode + '\'' +
                '}';
    }
}
