package com.creditcard.decision.service;

import org.springframework.stereotype.Service;
import com.creditcard.decision.entity.CreditDecision;
import com.creditcard.decision.entity.CreditDecision.DecisionStatus;
import com.creditcard.decision.entity.UserCreditScore;
import com.creditcard.decision.repository.CreditDecisionRepository;
import com.creditcard.decision.repository.UserCreditScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


@Service
public class DecisionEngineService {
    @Autowired
    private CreditDecisionRepository creditDecisionRepository;

    @Autowired
    private UserCreditScoreRepository creditScoreRepository;


    public CreditDecision processApplication(Long userId, Long applicationId, BigDecimal annualIncome,
                                             String employmentStatus, Integer yearsAtJob) {
        UserCreditScore userCreditScore = creditScoreRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Credit score not found for user"));

        int creditScore = userCreditScore.getCreditScore();

        CreditDecision decision = new CreditDecision();
        decision.setUserId(userId);
        decision.setApplicationId(applicationId);
        decision.setCreditScore(creditScore);

        if (creditScore >= 700 && annualIncome.compareTo(new BigDecimal("30000")) >= 0) {
            decision.setDecision(DecisionStatus.APPROVED);
            decision.setApprovedLimit(calculateCreditLimit(annualIncome, creditScore));
            decision.setInterestRate(calculateInterestRate(creditScore));
            decision.setDecisionReason("Approved - Good credit score and sufficient income");

        } else if (creditScore >= 650 && annualIncome.compareTo(new BigDecimal("25000")) >= 0 &&
                "FULL_TIME".equals(employmentStatus) && yearsAtJob >= 2) {
            decision.setDecision(DecisionStatus.APPROVED);
            decision.setApprovedLimit(calculateCreditLimit(annualIncome, creditScore));
            decision.setInterestRate(calculateInterestRate(creditScore));
            decision.setDecisionReason("Approved - Stable employment and acceptable credit");

        } else if (creditScore >= 600) {
            decision.setDecision(DecisionStatus.PENDING_REVIEW);
            decision.setDecisionReason("Requires manual review");

        } else {
            decision.setDecision(DecisionStatus.REJECTED);
            decision.setDecisionReason("Credit score below minimum requirements");
        }

        return creditDecisionRepository.save(decision);

    }
    private BigDecimal calculateCreditLimit(BigDecimal annualIncome, int creditScore) {
        BigDecimal baseLimit = annualIncome.multiply(new BigDecimal("0.20"));

        if (creditScore >= 750) {
            baseLimit = baseLimit.multiply(new BigDecimal("1.5"));
        } else if (creditScore >= 700) {
            baseLimit = baseLimit.multiply(new BigDecimal("1.2"));
        }

        return baseLimit.min(new BigDecimal("15000")).max(new BigDecimal("500"));
    }

    private BigDecimal calculateInterestRate(int creditScore) {
        if (creditScore >= 750) {
            return new BigDecimal("12.99");
        } else if (creditScore >= 700) {
            return new BigDecimal("16.99");
        } else if (creditScore >= 650) {
            return new BigDecimal("19.99");
        } else {
            return new BigDecimal("24.99");
        }
    }

    public CreditDecision getDecisionByApplicationId(Long applicationId) {
        return creditDecisionRepository.findByApplicationId(applicationId).orElse(null);
    }
}