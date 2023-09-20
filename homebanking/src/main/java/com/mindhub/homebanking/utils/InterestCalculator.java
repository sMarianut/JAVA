package com.mindhub.homebanking.utils;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Loan;

public class InterestCalculator {



    public static double calculateInterest(LoanApplicationDTO loanApplicationDTO,Loan loan) {
        int[] interestPayments = {5, 10, 20, 45, 65, 70, 75};
        int interestIndex = 0;
        int payments = loanApplicationDTO.getPaymentsReq();
        double amount = loanApplicationDTO.getAmount();
        double interest = loan.getInterest();

        if (payments == 3) {
            interestIndex = 0;
        } else if (payments == 6) {
            interestIndex = 1;
        } else if (payments == 12) {
            interestIndex = 2;
        } else if (payments == 24) {
            interestIndex = 3;
        } else if (payments == 36) {
            interestIndex = 4;
        } else if (payments == 48) {
            interestIndex = 5;
        } else if (payments == 60) {
            interestIndex = 6;
        } else {
            throw new IllegalArgumentException("This payments is not available.");
        }
        double interestRate = (interest + (double) interestPayments[interestIndex]) / 100;
        double totalAmountWithInterest = amount * (1 + interestRate);
        return totalAmountWithInterest;
    }

}
