package com.skilrock.retailapp.models.ola;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayprBankDetailResponseBean {
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
        private Data data;

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

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public class Data {

            @SerializedName("orgId")
            @Expose
            private Integer orgId;
            @SerializedName("domainId")
            @Expose
            private Integer domainId;
            @SerializedName("orgCode")
            @Expose
            private String orgCode;
            @SerializedName("orgTypeCode")
            @Expose
            private String orgTypeCode;
            @SerializedName("parentMagtOrgId")
            @Expose
            private Integer parentMagtOrgId;
            @SerializedName("parentAgtOrgId")
            @Expose
            private Integer parentAgtOrgId;
            @SerializedName("parentSagtOrgId")
            @Expose
            private Integer parentSagtOrgId;
            @SerializedName("orgName")
            @Expose
            private String orgName;
            @SerializedName("contactPerson")
            @Expose
            private String contactPerson;
            @SerializedName("mobileNumber")
            @Expose
            private String mobileNumber;
            @SerializedName("mobileCode")
            @Expose
            private String mobileCode;
            @SerializedName("mobileCodeNumber")
            @Expose
            private String mobileCodeNumber;
            @SerializedName("phoneNumber")
            @Expose
            private Object phoneNumber;
            @SerializedName("emailId")
            @Expose
            private String emailId;
            @SerializedName("addressOne")
            @Expose
            private String addressOne;
            @SerializedName("addressTwo")
            @Expose
            private String addressTwo;
            @SerializedName("zipCode")
            @Expose
            private String zipCode;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("country")
            @Expose
            private String country;
            @SerializedName("cityCode")
            @Expose
            private String cityCode;
            @SerializedName("stateCode")
            @Expose
            private String stateCode;
            @SerializedName("countryCode")
            @Expose
            private String countryCode;
            @SerializedName("regionCode")
            @Expose
            private String regionCode;
            @SerializedName("regionBinding")
            @Expose
            private String regionBinding;
            @SerializedName("walletType")
            @Expose
            private String walletType;
            @SerializedName("walletMode")
            @Expose
            private String walletMode;
            @SerializedName("distributableLimit")
            @Expose
            private String distributableLimit;
            @SerializedName("balance")
            @Expose
            private String balance;
            @SerializedName("creditLimit")
            @Expose
            private String creditLimit;
            @SerializedName("billConfigid")
            @Expose
            private Integer billConfigid;
            @SerializedName("lastBillPaidDate")
            @Expose
            private Object lastBillPaidDate;
            @SerializedName("billFirstSaleDate")
            @Expose
            private Object billFirstSaleDate;
            @SerializedName("firstSaleDate")
            @Expose
            private String firstSaleDate;
            @SerializedName("lastSaleDate")
            @Expose
            private String lastSaleDate;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("blockDate")
            @Expose
            private Object blockDate;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("updatedAt")
            @Expose
            private String updatedAt;
            @SerializedName("billTypeId")
            @Expose
            private Object billTypeId;
            @SerializedName("rawCreditLimit")
            @Expose
            private String rawCreditLimit;
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
            @SerializedName("userCount")
            @Expose
            private Integer userCount;
            @SerializedName("activeUserCount")
            @Expose
            private Integer activeUserCount;
            @SerializedName("regionName")
            @Expose
            private Object regionName;
            @SerializedName("billConfigDescription")
            @Expose
            private Object billConfigDescription;
            @SerializedName("assignedFieldXId")
            @Expose
            private Object assignedFieldXId;
            @SerializedName("assignedFieldXName")
            @Expose
            private Object assignedFieldXName;
            @SerializedName("isAffiliate")
            @Expose
            private String isAffiliate;

            public Integer getOrgId() {
                return orgId;
            }

            public void setOrgId(Integer orgId) {
                this.orgId = orgId;
            }

            public Integer getDomainId() {
                return domainId;
            }

            public void setDomainId(Integer domainId) {
                this.domainId = domainId;
            }

            public String getOrgCode() {
                return orgCode;
            }

            public void setOrgCode(String orgCode) {
                this.orgCode = orgCode;
            }

            public String getOrgTypeCode() {
                return orgTypeCode;
            }

            public void setOrgTypeCode(String orgTypeCode) {
                this.orgTypeCode = orgTypeCode;
            }

            public Integer getParentMagtOrgId() {
                return parentMagtOrgId;
            }

            public void setParentMagtOrgId(Integer parentMagtOrgId) {
                this.parentMagtOrgId = parentMagtOrgId;
            }

            public Integer getParentAgtOrgId() {
                return parentAgtOrgId;
            }

            public void setParentAgtOrgId(Integer parentAgtOrgId) {
                this.parentAgtOrgId = parentAgtOrgId;
            }

            public Integer getParentSagtOrgId() {
                return parentSagtOrgId;
            }

            public void setParentSagtOrgId(Integer parentSagtOrgId) {
                this.parentSagtOrgId = parentSagtOrgId;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getContactPerson() {
                return contactPerson;
            }

            public void setContactPerson(String contactPerson) {
                this.contactPerson = contactPerson;
            }

            public String getMobileNumber() {
                return mobileNumber;
            }

            public void setMobileNumber(String mobileNumber) {
                this.mobileNumber = mobileNumber;
            }

            public String getMobileCode() {
                return mobileCode;
            }

            public void setMobileCode(String mobileCode) {
                this.mobileCode = mobileCode;
            }

            public String getMobileCodeNumber() {
                return mobileCodeNumber;
            }

            public void setMobileCodeNumber(String mobileCodeNumber) {
                this.mobileCodeNumber = mobileCodeNumber;
            }

            public Object getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(Object phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public String getEmailId() {
                return emailId;
            }

            public void setEmailId(String emailId) {
                this.emailId = emailId;
            }

            public String getAddressOne() {
                return addressOne;
            }

            public void setAddressOne(String addressOne) {
                this.addressOne = addressOne;
            }

            public String getAddressTwo() {
                return addressTwo;
            }

            public void setAddressTwo(String addressTwo) {
                this.addressTwo = addressTwo;
            }

            public String getZipCode() {
                return zipCode;
            }

            public void setZipCode(String zipCode) {
                this.zipCode = zipCode;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getStateCode() {
                return stateCode;
            }

            public void setStateCode(String stateCode) {
                this.stateCode = stateCode;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public String getRegionCode() {
                return regionCode;
            }

            public void setRegionCode(String regionCode) {
                this.regionCode = regionCode;
            }

            public String getRegionBinding() {
                return regionBinding;
            }

            public void setRegionBinding(String regionBinding) {
                this.regionBinding = regionBinding;
            }

            public String getWalletType() {
                return walletType;
            }

            public void setWalletType(String walletType) {
                this.walletType = walletType;
            }

            public String getWalletMode() {
                return walletMode;
            }

            public void setWalletMode(String walletMode) {
                this.walletMode = walletMode;
            }

            public String getDistributableLimit() {
                return distributableLimit;
            }

            public void setDistributableLimit(String distributableLimit) {
                this.distributableLimit = distributableLimit;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getCreditLimit() {
                return creditLimit;
            }

            public void setCreditLimit(String creditLimit) {
                this.creditLimit = creditLimit;
            }

            public Integer getBillConfigid() {
                return billConfigid;
            }

            public void setBillConfigid(Integer billConfigid) {
                this.billConfigid = billConfigid;
            }

            public Object getLastBillPaidDate() {
                return lastBillPaidDate;
            }

            public void setLastBillPaidDate(Object lastBillPaidDate) {
                this.lastBillPaidDate = lastBillPaidDate;
            }

            public Object getBillFirstSaleDate() {
                return billFirstSaleDate;
            }

            public void setBillFirstSaleDate(Object billFirstSaleDate) {
                this.billFirstSaleDate = billFirstSaleDate;
            }

            public String getFirstSaleDate() {
                return firstSaleDate;
            }

            public void setFirstSaleDate(String firstSaleDate) {
                this.firstSaleDate = firstSaleDate;
            }

            public String getLastSaleDate() {
                return lastSaleDate;
            }

            public void setLastSaleDate(String lastSaleDate) {
                this.lastSaleDate = lastSaleDate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Object getBlockDate() {
                return blockDate;
            }

            public void setBlockDate(Object blockDate) {
                this.blockDate = blockDate;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public Object getBillTypeId() {
                return billTypeId;
            }

            public void setBillTypeId(Object billTypeId) {
                this.billTypeId = billTypeId;
            }

            public String getRawCreditLimit() {
                return rawCreditLimit;
            }

            public void setRawCreditLimit(String rawCreditLimit) {
                this.rawCreditLimit = rawCreditLimit;
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

            public Integer getUserCount() {
                return userCount;
            }

            public void setUserCount(Integer userCount) {
                this.userCount = userCount;
            }

            public Integer getActiveUserCount() {
                return activeUserCount;
            }

            public void setActiveUserCount(Integer activeUserCount) {
                this.activeUserCount = activeUserCount;
            }

            public Object getRegionName() {
                return regionName;
            }

            public void setRegionName(Object regionName) {
                this.regionName = regionName;
            }

            public Object getBillConfigDescription() {
                return billConfigDescription;
            }

            public void setBillConfigDescription(Object billConfigDescription) {
                this.billConfigDescription = billConfigDescription;
            }

            public Object getAssignedFieldXId() {
                return assignedFieldXId;
            }

            public void setAssignedFieldXId(Object assignedFieldXId) {
                this.assignedFieldXId = assignedFieldXId;
            }

            public Object getAssignedFieldXName() {
                return assignedFieldXName;
            }

            public void setAssignedFieldXName(Object assignedFieldXName) {
                this.assignedFieldXName = assignedFieldXName;
            }

            public String getIsAffiliate() {
                return isAffiliate;
            }

            public void setIsAffiliate(String isAffiliate) {
                this.isAffiliate = isAffiliate;
            }

        }

    }
}
