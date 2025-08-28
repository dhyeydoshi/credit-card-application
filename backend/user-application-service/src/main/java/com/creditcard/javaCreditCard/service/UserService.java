package com.creditcard.javaCreditCard.service;

import com.creditcard.javaCreditCard.dataObject.userRegistrationDataObject;
import com.creditcard.javaCreditCard.dataObject.CreditApplicationDataObject;
import com.creditcard.javaCreditCard.entity.User;
import com.creditcard.javaCreditCard.entity.CreditApplication;
import  com.creditcard.javaCreditCard.repository.UserRepository;
import  com.creditcard.javaCreditCard.repository.CreditApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.creditcard.decision.dataObject.CreditDecisionDataObject;


import java.math.BigDecimal;
import java.util.*;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private CreditApplicationRepository applicationRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${services.decision-engine.url:http://localhost:8083}")
    private String decisionEngineUrl;

    @Value("${services.document-service.url:http://localhost:8082}")
    private String documentServiceUrl;



    public User registerUser(userRegistrationDataObject userRegistrationDataObject) {
        if(userRepository.existsByEmail(userRegistrationDataObject.getEmail())) {
            throw  new RuntimeException("Email Already Exists");
        }

        if (userRepository.existsBySocialSecurityNumber(userRegistrationDataObject.getSocialSecurityNumber())) {
            throw  new RuntimeException("Social Security Number Already Exists");
        }

        User user = new User();
        user.setEmail(userRegistrationDataObject.getEmail());
        user.setSocialSecurityNumber(userRegistrationDataObject.getSocialSecurityNumber());
        user.setFirstName(userRegistrationDataObject.getFirstName());
        user.setLastName(userRegistrationDataObject.getLastName());
        user.setPassword(passwordEncoder.encode(userRegistrationDataObject.getPassword()));
        user.setPhoneNumber(userRegistrationDataObject.getPhoneNumber());
        user.setDateOfBirth(userRegistrationDataObject.getDateOfBirth());
        user.setStreetAddress(userRegistrationDataObject.getStreetAddress());
        user.setCity(userRegistrationDataObject.getCity());
        user.setState(userRegistrationDataObject.getState());
        user.setPostalCode(userRegistrationDataObject.getPostalCode());

        return   userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email Not Found"));

    }

    public boolean validatePassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    public CreditApplication submitApplication(CreditApplicationDataObject dto, String email) {
        User user = findByEmail(email);

        CreditApplication application = new CreditApplication(user);
        application.setAnnualIncome(dto.getAnnualIncome());
        application.setEmploymentStatus(dto.getEmploymentStatus());
        application.setEmployerName(dto.getEmployerName());
        application.setYearsAtJob(dto.getYearsAtJob());
        application.setMonthlyRent(dto.getMonthlyRent());
        application.setHousingStatus(dto.getHousingStatus());
        application.setStatus(CreditApplication.ApplicationStatus.PENDING_REVIEW);
        application = applicationRepository.save(application);

        initiateDocumentCollection(user.getID(), application.getId());

        processApplicationDecision(application);

        return application;
    }
    private void initiateDocumentCollection(Long userId, Long applicationId) {
        try {
            String[] requiredDocuments = {"ID", "PAYSTUB", "BANK_STATEMENT"};

            for (String docType : requiredDocuments) {
                Map<String, Object> docRequirement = new HashMap<>();
                docRequirement.put("userId", userId);
                docRequirement.put("applicationId", applicationId);
                docRequirement.put("documentType", docType);
                docRequirement.put("required", true);
                docRequirement.put("status", "REQUIRED");

                webClientBuilder.build()
                        .post()
                        .uri(documentServiceUrl + "/api/documents/requirement")
                        .bodyValue(docRequirement)
                        .retrieve()
                        .bodyToMono(String.class)
                        .subscribe(
                                response -> System.out.println("Document requirement created: " + docType),
                                error -> System.err.println("Failed to create document requirement: " + error.getMessage())
                        );
            }
        } catch (Exception e) {
            System.err.println("Error initiating document collection: " + e.getMessage());
        }
    }

    private void processApplicationDecision(CreditApplication application) {
        try {
            Map<String, Object> decisionRequest = new HashMap<>();
            decisionRequest.put("userId", application.getUser().getID());
            decisionRequest.put("applicationId", application.getId());
            decisionRequest.put("annualIncome", application.getAnnualIncome());
            decisionRequest.put("employmentStatus", application.getEmploymentStatus());
            decisionRequest.put("yearsAtJob", application.getYearsAtJob());

            webClientBuilder.build()
                    .post()
                    .uri(decisionEngineUrl + "/api/decision/process")
                    .bodyValue(decisionRequest)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .subscribe(
                            response -> {
                                updateApplicationStatus(application.getId(), response);
                                System.out.println("Decision processed for application: " + application.getId());
                            },
                            error -> {
                                System.err.println("Decision processing failed: " + error.getMessage());
                                updateApplicationStatusToError(application.getId());
                            }
                    );
        } catch (Exception e) {
            System.err.println("Error processing application decision: " + e.getMessage());
            updateApplicationStatusToError(application.getId());
        }
    }
    private void updateApplicationStatus(Long applicationId, Map<String, Object> decisionResponse) {
        try {
            applicationRepository.findById(applicationId).ifPresent(app -> {
                String decision = decisionResponse.get("decision").toString();
                switch (decision) {
                    case "APPROVED":
                        app.setStatus(CreditApplication.ApplicationStatus.APPROVED);
                        break;
                    case "REJECTED":
                        app.setStatus(CreditApplication.ApplicationStatus.REJECTED);
                        break;
                    default:
                        app.setStatus(CreditApplication.ApplicationStatus.PENDING_REVIEW);
                }
                applicationRepository.save(app);
            });
        } catch (Exception e) {
            System.err.println("Error updating application status: " + e.getMessage());
        }
    }
    private void updateApplicationStatusToError(Long applicationId) {
        applicationRepository.findById(applicationId).ifPresent(app -> {
            app.setStatus(CreditApplication.ApplicationStatus.PENDING_REVIEW);
            applicationRepository.save(app);
        });
    }

    public List<CreditApplication> getUserApplications(String email) {
        User user = findByEmail(email);
        return applicationRepository.findByUserId(user.getID());
    }

    public BigDecimal fetchApprovedCreditLimit(Long applicationId) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(decisionEngineUrl + "/api/decision/application/" + applicationId)
                    .retrieve()
                    .bodyToMono(CreditDecisionDataObject.class)
                    .map(CreditDecisionDataObject::getApprovedLimit)
                    .block();
        } catch (Exception e) {
            System.err.println("Error fetching approved credit limit: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public CreditApplication getApplicationById(Long applicationId, String email) {
        User user = findByEmail(email);
        return applicationRepository.findById(applicationId)
                .filter(app -> app.getUser().getID().equals(user.getID()))
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
}

