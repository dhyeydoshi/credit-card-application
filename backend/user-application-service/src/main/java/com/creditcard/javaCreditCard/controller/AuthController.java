package com.creditcard.javaCreditCard.controller;

import com.creditcard.javaCreditCard.dataObject.userRegistrationDataObject;
import com.creditcard.javaCreditCard.dataObject.CreditApplicationDataObject;
import com.creditcard.javaCreditCard.entity.User;
import com.creditcard.javaCreditCard.entity.CreditApplication;
import com.creditcard.javaCreditCard.service.UserService;
import com.creditcard.javaCreditCard.util.jwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private jwtUtil jwtUtil;

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody userRegistrationDataObject dto) {
        try {
            User user = userService.registerUser(dto);
            String token = jwtUtil.generateToken(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("token", token);
            response.put("user", createUserResponse(user));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            User user = userService.findByEmail(email);

            if (userService.validatePassword(password, user.getPassword())) {
                String token = jwtUtil.generateToken(email);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("token", token);
                response.put("user", createUserResponse(user));

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/api/applications/submit")
    public ResponseEntity<?> submitApplication(@Valid @RequestBody CreditApplicationDataObject dto,
                                               @RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractEmail(token.substring(7));
            CreditApplication application = userService.submitApplication(dto, email);

            return ResponseEntity.ok(Map.of(
                    "message", "Application submitted successfully",
                    "applicationId", application.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/applications/user")
    public ResponseEntity<List<Map<String, Object>>> getUserApplications(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractEmail(token.substring(7));
            List<CreditApplication> applications = userService.getUserApplications(email);

            List<Map<String, Object>> result = new ArrayList<>();
            for (CreditApplication app : applications) {
                Map<String, Object> appData = new HashMap<>();
                appData.put("id", app.getId());
                appData.put("status", app.getStatus());
                appData.put("annualIncome", app.getAnnualIncome());
                appData.put("employmentStatus", app.getEmploymentStatus());
                appData.put("submittedAt", app.getSubmittedAt());

                BigDecimal approvedLimit = userService.fetchApprovedCreditLimit(app.getId());
                appData.put("approvedCreditLimit", approvedLimit);

                result.add(appData);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/applications/{applicationId}")
    public ResponseEntity<CreditApplication> getApplication(@PathVariable Long applicationId,
                                                            @RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractEmail(token.substring(7));
            CreditApplication application = userService.getApplicationById(applicationId, email);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getID());
        userResponse.put("firstName", user.getFirstName());
        userResponse.put("lastName", user.getLastName());
        userResponse.put("email", user.getEmail());
        return userResponse;
    }
}
