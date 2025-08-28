package com.creditcard.decision.service;

import com.creditcard.decision.dataObject.UserCreditScoreDataObject;
import com.creditcard.decision.entity.UserCreditScore;
import com.creditcard.decision.repository.UserCreditScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserCreditScoreService {
    @Autowired
    private UserCreditScoreRepository creditScoreRepository;

    public UserCreditScore saveCreditScore(UserCreditScoreDataObject dto) {
        Optional<UserCreditScore> existingScore = creditScoreRepository.findByUserId(dto.getUserId());
        Optional<UserCreditScore> existingSocialSecurityNumber = creditScoreRepository.findBySocialSecurityNumber(dto.getSocialSecurityNumber());

        UserCreditScore creditScore;

        if (existingScore.isPresent() && existingSocialSecurityNumber.isPresent()) {
            creditScore = existingScore.get();
            creditScore.setCreditScore(dto.getCreditScore());
            creditScore.setSource(dto.getSource());
            creditScore.setLastUpdated(LocalDateTime.now());
        } else {
            creditScore = new UserCreditScore();
            creditScore.setUserId(dto.getUserId());
            creditScore.setCreditScore(dto.getCreditScore());
            creditScore.setSocialSecurityNumber(dto.getSocialSecurityNumber());
            creditScore.setSource(dto.getSource() != null ? dto.getSource() : "MANUAL");
            creditScore.setLastUpdated(LocalDateTime.now());
        }
        return creditScoreRepository.save(creditScore);
    }

    public UserCreditScore getCreditScoreByUserId(Long userId) {
        return creditScoreRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Credit score not found for user ID: " + userId));
    }
    public List<UserCreditScore> getAllCreditScores() {
        return creditScoreRepository.findAll();
    }
    public boolean hasCreditScore(Long userId) {
        return creditScoreRepository.findByUserId(userId).isPresent();
    }

}
