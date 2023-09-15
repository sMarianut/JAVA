package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.Arrays;

public class InterestCalculator {
    int[] interest = {10,20,45,65,70};

    public double totalAmount(double amount,int payments){
        int interestIndex = -1;

        if (payments == 6) {
            interestIndex = 0;
        } else if (payments == 12) {
            interestIndex = 1;
        } else if (payments== 24) {
            interestIndex = 2;
        } else if (payments == 36) {
            interestIndex = 3;
        } else if (payments == 48) {
            interestIndex = 4;
        } else {
            throw new IllegalArgumentException("This payments is not available.");
        }
        double interestRate = interest[interestIndex] / 100.0;

        double totalAmountWithInterest = amount * (1 + interestRate);

        return totalAmountWithInterest;
    }
    public double[] calculateMonthlyPayments(double amount, int payments) {
        double totalAmountWithInterest = totalAmount(amount, payments);
        double monthlyPayment = totalAmountWithInterest / payments;
        double[] monthlyPayments = new double[payments];
        Arrays.fill(monthlyPayments, monthlyPayment);
        return monthlyPayments;
    }
    public static Double interestCalc(Loan loan) {
        int initialPercentage = 10;
        double totalInterest = 0.0;

        for (Integer payment : loan.getPayments()) {
            double interestPayment = payment * (initialPercentage / 100.0);
            totalInterest += interestPayment;
            initialPercentage += 5;
        }
        Double interest = totalInterest;
        return interest;
    }
}
