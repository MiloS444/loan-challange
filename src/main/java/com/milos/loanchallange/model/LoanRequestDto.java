package com.milos.loanchallange.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class LoanRequestDto {

    private Long amount;
    private Integer annualInterestPercentage;
    private Integer numberMonths;

}
