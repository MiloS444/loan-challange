package com.milos.loanchallange.service;

import com.milos.loanchallange.mapper.LoanRequestMapper;
import com.milos.loanchallange.model.LoanRequestDto;
import com.milos.loanchallange.model.database.LoanRequest;
import com.milos.loanchallange.repository.LoanRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    private final LoanRequestMapper loanRequestMapper;
    private final LoanRequestRepository loanRequestRepository;

    @Override
    public void getInstallmentPlan(final LoanRequestDto loanRequestDto) {

        if (requestExists(loanRequestDto)) {
            System.out.println("Yay");
        } else {
            System.out.println("Boooooooooo");
        }

    }

    private boolean requestExists(final LoanRequestDto loanRequestDto) {

        final LoanRequest loanRequest = loanRequestMapper.toLoanRequest(loanRequestDto);
        //TODO: Switch to hash
        return loanRequestRepository.existsByAmountAndAnnualInterestPercentageAndNumberMonths(loanRequest.getAmount(),
                                                                                              loanRequest.getAnnualInterestPercentage(),
                                                                                              loanRequest.getNumberMonths());
    }
}
