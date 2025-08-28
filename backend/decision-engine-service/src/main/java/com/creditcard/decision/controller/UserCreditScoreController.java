package com.creditcard.decision.controller;

import com.creditcard.decision.dataObject.UserCreditScoreDataObject;
import com.creditcard.decision.entity.UserCreditScore;
import com.creditcard.decision.service.UserCreditScoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user-credit-score")
public class UserCreditScoreController {

    @Autowired
    private UserCreditScoreService userCreditScoreService;

    @PostMapping
    public ResponseEntity<?> saveCreditScore(@Valid @RequestBody UserCreditScoreDataObject creditScoreDto) {
        try {
            UserCreditScore savedScore = userCreditScoreService.saveCreditScore(creditScoreDto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Credit score saved successfully");
            response.put("creditScore", savedScore);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCreditScoreByUserId(@PathVariable Long userId) {
        try {
            UserCreditScore creditScore = userCreditScoreService.getCreditScoreByUserId(userId);
            return ResponseEntity.ok(creditScore);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserCreditScore>> getAllCreditScores() {
        List<UserCreditScore> creditScores = userCreditScoreService.getAllCreditScores();
        return ResponseEntity.ok(creditScores);
    }
    @GetMapping("/user/{userId}/exists")
    public ResponseEntity<Map<String, Boolean>> checkCreditScoreExists(@PathVariable Long userId) {
        boolean exists = userCreditScoreService.hasCreditScore(userId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }


}
