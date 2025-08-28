package com.creditcard.javaCreditCard.repository;

import com.creditcard.javaCreditCard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends  JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsBySocialSecurityNumber(String socialSecurityNumber);
}
