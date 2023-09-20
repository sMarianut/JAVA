package com.mindhub.homebanking.dtos;
public class LoanApplicationDTO {
    private long id;
    private double amount;
    private Integer paymentsReq;
    private String accountDest;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long id, double amount, Integer payments, String accountDest) {
        this.id = id;
        this.amount = amount;
        this.paymentsReq = payments;
        this.accountDest = accountDest;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPaymentsReq() {
        return paymentsReq;
    }

    public void setPaymentsReq(Integer payments) {
        this.paymentsReq = payments;
    }

    public String getAccountDest() {
        return accountDest;
    }

    public void setAccountDest(String accountDest) {
        this.accountDest = accountDest;
    }
}
