package com.creditcard.decision.entity;

import jakarta.persistence.*;
import java.math.*;
import java.time.*;

@Entity
@Table(name = "credit_decisions")
public class CreditDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long applicationId;
    private Integer creditScore;
    private BigDecimal interestRate;
    private BigDecimal approvedLimit;

    @Enumerated(EnumType.STRING)
    private DecisionStatus decision;

    private String decisionReason;

    private LocalDateTime processedAt = LocalDateTime.now();

    public CreditDecision() {}

    public Long getId() {
        return id;}
    public void setId(Long id) {
        this.id = id;}

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

    public DecisionStatus getDecision() { return decision; }
    public void setDecision(DecisionStatus decision) { this.decision = decision; }

    public String getDecisionReason() { return decisionReason; }
    public void setDecisionReason(String decisionReason) { this.decisionReason = decisionReason; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public enum DecisionStatus {
        APPROVED,
        REJECTED,
        PENDING_REVIEW,

    }
}

