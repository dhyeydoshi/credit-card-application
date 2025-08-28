package com.creditcard.decision.controller;

import com.creditcard.decision.entity.CreditDecision;
import com.creditcard.decision.service.DecisionEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/decision")
@CrossOrigin(origins = "*")
public class DecisionController {

    @Autowired
    private DecisionEngineService decisionEngineService;

    @PostMapping("/process")
    public ResponseEntity<CreditDecision> processApplication(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long applicationId = Long.valueOf(request.get("applicationId").toString());
            BigDecimal annualIncome = new BigDecimal(request.get("annualIncome").toString());
            String employmentStatus = request.get("employmentStatus").toString();
            Integer yearsAtJob = request.get("yearsAtJob") != null ?
                    Integer.valueOf(request.get("yearsAtJob").toString()) : 0;

            CreditDecision decision = decisionEngineService.processApplication(
                    userId, applicationId, annualIncome, employmentStatus, yearsAtJob);

            return ResponseEntity.ok(decision);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<CreditDecision> getDecision(@PathVariable Long applicationId) {
        CreditDecision decision = decisionEngineService.getDecisionByApplicationId(applicationId);
        return decision != null ? ResponseEntity.ok(decision) : ResponseEntity.notFound().build();
    }
}
