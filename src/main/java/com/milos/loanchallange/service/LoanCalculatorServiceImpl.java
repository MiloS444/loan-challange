package com.milos.loanchallange.service;

import com.milos.loanchallange.mapper.LoanRequestMapper;
import com.milos.loanchallange.model.LoanRequestDto;
import com.milos.loanchallange.model.database.InstallmentPlan;
import com.milos.loanchallange.model.database.LoanRequest;
import com.milos.loanchallange.repository.LoanRequestRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    private final LoanRequestMapper loanRequestMapper;
    private final LoanRequestRepository loanRequestRepository;

    @Override
    public List<String> getInstallmentPlan(final LoanRequestDto loanRequestDto) {

        final Optional<LoanRequest> request = findLoanRequest(loanRequestDto);

        if (request.isPresent()) {

            return request.get().getInstallmentPlan().stream()
                    .map(installmentPlan -> installmentPlan.getPaymentAmount().toString())
                    .toList();
        } else {

            final LoanRequest loanRequest = loanRequestMapper.toLoanRequest(loanRequestDto);

            final List<InstallmentPlan> installmentPlans = calculateAmortizationSchedule(
                    BigDecimal.valueOf(loanRequestDto.getAmount()),
                    BigDecimal.valueOf(loanRequestDto.getAnnualInterestPercentage()),
                    loanRequestDto.getNumberOfPayments());

            loanRequest.setInstallmentPlan(installmentPlans);

            loanRequestRepository.save(loanRequest);

            return loanRequest.getInstallmentPlan().stream()
                    .map(installmentPlan -> installmentPlan.getPaymentAmount().toString())
                    .toList();
        }
    }

    private Optional<LoanRequest> findLoanRequest(final LoanRequestDto loanRequestDto) {

        final LoanRequest loanRequest = loanRequestMapper.toLoanRequest(loanRequestDto);
        //TODO: Switch to hash
        return loanRequestRepository.findOneByAmountAndAnnualInterestPercentageAndNumberOfPayments(
                loanRequest.getAmount(),
                loanRequest.getAnnualInterestPercentage(),
                loanRequest.getNumberOfPayments());
    }

    public static List<InstallmentPlan> calculateAmortizationSchedule(BigDecimal amount, BigDecimal interestRate,
            int numPayments) {

        List<InstallmentPlan> installmentPlans = new ArrayList<>();

        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(1200), 20,
                                                     RoundingMode.HALF_UP);
        BigDecimal payment = amount.multiply(monthlyRate.add(monthlyRate.divide((BigDecimal.ONE.add(monthlyRate)).pow(
                numPayments).subtract(BigDecimal.ONE), 20, RoundingMode.HALF_UP)));
        BigDecimal balance = amount;
        BigDecimal totalInterest = BigDecimal.ZERO;
        BigDecimal principal, monthlyInterest;

        System.out.println("Amortization Schedule");
        System.out.println("$" + amount + " at " + interestRate + "% interest");
        System.out.println("with " + numPayments + " monthly payments");

        System.out.println("Payment\tAmount\t\tPrincipal\tInterest\tBalance Owed");
        for (int i = 1; i <= numPayments; i++) {
            monthlyInterest = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            principal = payment.subtract(monthlyInterest).setScale(2, RoundingMode.HALF_UP);
            balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);
            totalInterest = totalInterest.add(monthlyInterest);
            System.out.printf("%d\t%.2f\t\t%.2f\t\t%.2f\t\t%.2f\n", i, payment, principal, monthlyInterest, balance);

            final InstallmentPlan installmentPlan = InstallmentPlan.builder()
                    .paymentAmount(payment)
                    .principalAmount(principal)
                    .interestAmount(monthlyInterest)
                    .balanceOwed(balance)
                    .build();

            installmentPlans.add(installmentPlan);
        }
        System.out.printf("Total Payments: $%.2f Total Interest: $%.2f",
                          payment.multiply(BigDecimal.valueOf(numPayments)).setScale(2, RoundingMode.HALF_UP),
                          totalInterest);

        return installmentPlans;
    }
}
