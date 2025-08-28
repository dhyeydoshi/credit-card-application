package com.creditcard.javaCreditCard.dataObject;


import java.math.BigDecimal;

public class CreditApplicationDataObject {

    private BigDecimal annualIncome;

    private String employmentStatus;
    private String employerName;
    private Integer yearsAtJob;
    private BigDecimal monthlyRent;
    private String housingStatus;

    public CreditApplicationDataObject() {}

    public BigDecimal getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(BigDecimal annualIncome) { this.annualIncome = annualIncome; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public Integer getYearsAtJob() { return yearsAtJob; }
    public void setYearsAtJob(Integer yearsAtJob) { this.yearsAtJob = yearsAtJob; }

    public BigDecimal getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(BigDecimal monthlyRent) { this.monthlyRent = monthlyRent; }

    public String getHousingStatus() { return housingStatus; }
    public void setHousingStatus(String housingStatus) { this.housingStatus = housingStatus; }


}
