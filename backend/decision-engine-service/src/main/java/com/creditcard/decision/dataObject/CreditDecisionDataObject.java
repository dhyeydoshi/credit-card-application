package com.creditcard.decision.dataObject;

import java.math.BigDecimal;

public class CreditDecisionDataObject {
    private Long userId;
    private Long applicationId;
    private Integer creditScore;
    private BigDecimal interestRate;
    private BigDecimal approvedLimit;

    public CreditDecisionDataObject() {}

    public Long getUserId() {
        return userId;}
    public void setUserId(Long userId) {
        this.userId = userId;}

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public BigDecimal getApprovedLimit() { return approvedLimit; }
    public void setApprovedLimit(BigDecimal approvedLimit) { this.approvedLimit = approvedLimit; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
}
