package com.creditcard.javaCreditCard.repository;

import com.creditcard.javaCreditCard.entity.CreditApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CreditApplicationRepository extends JpaRepository<CreditApplication, Long> {
    List<CreditApplication> findByUserId(Long userId);
}

