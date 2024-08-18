package com.skilrock.retailapp.models.ola;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayprOlaPayoutRequestBean {
    @SerializedName("amount")
    @Expose
    private long amount;
    @SerializedName("confirmAmount")
    @Expose
    private long confirmAmount;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("orgId")
    @Expose
    private String orgId;
    @SerializedName("domainId")
    @Expose
    private Integer domainId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("beneficiaryName")
    @Expose
    private String beneficiaryName;
    @SerializedName("appType")
    @Expose
    private String appType;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("clientIp")
    @Expose
    private String clientIp;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getConfirmAmount() {
        return confirmAmount;
    }

    public void setConfirmAmount(long confirmAmount) {
        this.confirmAmount = confirmAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Override
    public String toString() {
        return "PayprOlaPayoutRequestBean{" +
                "amount=" + amount +
                ", confirmAmount=" + confirmAmount +
                ", remarks='" + remarks + '\'' +
                ", orgId='" + orgId + '\'' +
                ", domainId=" + domainId +
                ", type='" + type + '\'' +
                ", bankName='" + bankName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", beneficiaryName='" + beneficiaryName + '\'' +
                ", appType='" + appType + '\'' +
                ", device='" + device + '\'' +
                ", clientIp='" + clientIp + '\'' +
                '}';
    }
}
