package com.creditcard.decision.repository;

import org.springframework.stereotype.Repository;
import com.creditcard.decision.entity.UserCreditScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


@Repository
public interface UserCreditScoreRepository extends JpaRepository<UserCreditScore, Long> {

    Optional<UserCreditScore> findByUserId(Long userId);
    Optional<UserCreditScore> findBySocialSecurityNumber(String socialSecurityNumber);
}
