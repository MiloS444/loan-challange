package com.milos.loanchallange;

import com.milos.loanchallange.model.LoanRequestDto;
import com.milos.loanchallange.repository.LoanRequestRepository;
import com.milos.loanchallange.service.LoanCalculatorServiceImpl;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
//TODO: Finish test
class LoanCalculatorServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private LoanCalculatorServiceImpl loanCalculatorService;

    @BeforeEach
    void setup() {

        loanRequestRepository.deleteAll();
    }

    @Test
    void shouldBeAbleToGetNoEpgEvents() {

        final LoanRequestDto loanRequestDto = LoanRequestDto.builder().amount(100L).annualInterestPercentage(5).build();

        loanCalculatorService.getInstallmentPlan(loanRequestDto);

        Assertions.assertThat(0).isOdd();
    }
}
