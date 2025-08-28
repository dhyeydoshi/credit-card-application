package com.creditcard.decision.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_credit_scores")
public class UserCreditScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private Integer creditScore;
    private LocalDateTime lastUpdated = LocalDateTime.now();
    private String source;

    @Column(unique = true)
    private String socialSecurityNumber;
    public UserCreditScore() {}

    public UserCreditScore(Long userId, Integer creditScore, String source) {
        this.userId = userId;
        this.creditScore = creditScore;
        this.source = source;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber;}
}
