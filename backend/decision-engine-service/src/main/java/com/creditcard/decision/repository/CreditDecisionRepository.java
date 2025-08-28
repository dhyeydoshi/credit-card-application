package com.creditcard.decision.repository;

import com.creditcard.decision.entity.CreditDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CreditDecisionRepository extends JpaRepository<CreditDecision, Long> {

    Optional<CreditDecision> findByApplicationId(Long applicationId);
    Optional<CreditDecision> findByUserId(Long userId);
}
