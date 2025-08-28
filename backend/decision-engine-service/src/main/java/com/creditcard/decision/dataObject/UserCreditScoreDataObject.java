package com.creditcard.decision.dataObject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class UserCreditScoreDataObject {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score cannot exceed 850")
    private Integer creditScore;

    private String source;

    private String socialSecurityNumber;

    public UserCreditScoreDataObject() {}

    public UserCreditScoreDataObject(Long userId, Integer creditScore, String source) {
        this.userId = userId;
        this.creditScore = creditScore;
        this.source = source;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getSocialSecurityNumber() { return socialSecurityNumber;   }
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;  }
}
