package com.milos.loanchallange.mapper;

import com.milos.loanchallange.model.LoanRequestDto;
import com.milos.loanchallange.model.database.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanRequestMapper {

    public LoanRequest toLoanRequest(final LoanRequestDto loanRequestDto) {

        return LoanRequest.builder()
                .amount(loanRequestDto.getAmount())
                .annualInterestPercentage(loanRequestDto.getAnnualInterestPercentage())
                .numberOfPayments(loanRequestDto.getNumberOfPayments())
                .build();
    }

}
