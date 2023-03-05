package com.milos.loanchallange.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.milos.loanchallange.error.LoanRequestException;
import com.milos.loanchallange.mapper.LoanRequestMapper;
import com.milos.loanchallange.model.LoanRequestDto;
import com.milos.loanchallange.model.database.InstallmentPlan;
import com.milos.loanchallange.model.database.LoanRequest;
import com.milos.loanchallange.repository.LoanRequestRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanCalculatorServiceImplTest {

    @Mock
    private LoanRequestMapper loanRequestMapper;
    @Mock
    private LoanRequestRepository loanRequestRepository;
    @InjectMocks
    LoanCalculatorServiceImpl loanCalculatorService;

    @Test
    void getInstallmentPlanNullRequest() {

        Assertions.assertThatThrownBy(() -> loanCalculatorService.getInstallmentPlan(null))
                .isInstanceOf(LoanRequestException.class);
    }

    @Test
    void getInstallmentPlanBadRequest() {

        final LoanRequestDto loanRequestDto = LoanRequestDto.builder().amount(100L).annualInterestPercentage(5).build();

        Assertions.assertThatThrownBy(() -> loanCalculatorService.getInstallmentPlan(loanRequestDto))
                .isInstanceOf(LoanRequestException.class);
    }

    @Test
    void getInstallmentCalculatePlan() {

        final LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .amount(100L)
                .annualInterestPercentage(5)
                .numberOfPayments(2)
                .build();

        when(loanRequestRepository.findOneByAmountAndAnnualInterestPercentageAndNumberOfPayments(100L, 5, 2))
                .thenReturn(Optional.empty());
        when(loanRequestMapper.toLoanRequest(loanRequestDto)).thenCallRealMethod();

        final List<String> result = loanCalculatorService.getInstallmentPlan(loanRequestDto);

        Assertions.assertThat(result).isNotEmpty().hasSize(2);
        Assertions.assertThat(result.get(0)).isEqualTo("50.31271656271656271700");

        verify(loanRequestRepository, times(1)).save(any());
    }

    @Test
    void getInstallmentReadPlan() {

        final LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .amount(100L)
                .annualInterestPercentage(5)
                .numberOfPayments(2)
                .build();
        final LoanRequest loanRequest = LoanRequest.builder()
                .installmentPlan(List.of(InstallmentPlan.builder().paymentAmount(BigDecimal.ONE).build()))
                .build();

        when(loanRequestRepository.findOneByAmountAndAnnualInterestPercentageAndNumberOfPayments(100L, 5, 2))
                .thenReturn(Optional.of(loanRequest));
        when(loanRequestMapper.toLoanRequest(loanRequestDto)).thenCallRealMethod();

        final List<String> result = loanCalculatorService.getInstallmentPlan(loanRequestDto);

        Assertions.assertThat(result).isNotEmpty().hasSize(1);
        Assertions.assertThat(result.get(0)).isEqualTo("1");
    }

}
