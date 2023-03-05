package com.milos.loanchallange.service;

import com.milos.loanchallange.error.LoanRequestException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    private final LoanRequestMapper loanRequestMapper;
    private final LoanRequestRepository loanRequestRepository;

    @Override
    public List<String> getInstallmentPlan(final LoanRequestDto loanRequestDto) {

        validateLoanRequest(loanRequestDto);
        final Optional<LoanRequest> request = findLoanRequest(loanRequestDto);

        if (request.isPresent()) {

            return request.get().getInstallmentPlan().stream()
                    .map(installmentPlan -> installmentPlan.getPaymentAmount().toString())
                    .toList();
        } else {

            final LoanRequest loanRequest = loanRequestMapper.toLoanRequest(loanRequestDto);
            final List<InstallmentPlan> installmentPlans = calculateAmortizationPlan(
                    BigDecimal.valueOf(loanRequestDto.getAmount()),
                    BigDecimal.valueOf(loanRequestDto.getAnnualInterestPercentage()),
                    loanRequestDto.getNumberOfPayments());

            loanRequest.setInstallmentPlan(installmentPlans);
            loanRequestRepository.save(loanRequest);

            //TODO: Add mappers move outside
            return loanRequest.getInstallmentPlan().stream()
                    .map(installmentPlan -> installmentPlan.getPaymentAmount().toString())
                    .toList();
        }
    }

    private void validateLoanRequest(final LoanRequestDto loanRequestDto) {

        if (loanRequestDto == null || loanRequestDto.getAmount() == null || loanRequestDto.getAmount() < 0
                || loanRequestDto.getAnnualInterestPercentage() == null
                || loanRequestDto.getAnnualInterestPercentage() < 0
                || loanRequestDto.getNumberOfPayments() == null || loanRequestDto.getNumberOfPayments() < 0) {

            throw new LoanRequestException(HttpStatus.BAD_REQUEST.toString(), "Correct loan request parameters.",
                                           HttpStatus.BAD_REQUEST);
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

    private List<InstallmentPlan> calculateAmortizationPlan(final BigDecimal amount, final BigDecimal interestRate,
            final int numPayments) {

        List<InstallmentPlan> installmentPlans = new ArrayList<>();

        //TODO: Move/ delete overkill
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(1200), 20,
                                                     RoundingMode.HALF_UP);
        BigDecimal payment = amount.multiply(monthlyRate.add(monthlyRate.divide((BigDecimal.ONE.add(monthlyRate)).pow(
                numPayments).subtract(BigDecimal.ONE), 20, RoundingMode.HALF_UP)));
        BigDecimal balance = amount;
        BigDecimal totalInterest = BigDecimal.ZERO;
        BigDecimal principal;
        BigDecimal monthlyInterest;

        log.debug("Amortization Schedule");
        log.debug("$" + amount + " at " + interestRate + "% interest");
        log.debug("with " + numPayments + " monthly payments");

        log.debug("Payment\tAmount\t\tPrincipal\tInterest\tBalance Owed");
        for (int i = 1; i <= numPayments; i++) {
            monthlyInterest = balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            principal = payment.subtract(monthlyInterest).setScale(2, RoundingMode.HALF_UP);
            balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);
            totalInterest = totalInterest.add(monthlyInterest);
            //FIXME: Fix output
            log.debug("%d\t%.2f\t\t%.2f\t\t%.2f\t\t%.2f\n", i, payment, principal, monthlyInterest, balance);

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
