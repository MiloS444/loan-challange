package com.milos.loanchallange.model.database;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

    @Id
    @GeneratedValue
    private Long id;
    private Long amount;
    private Integer annualInterestPercentage;
    private Integer numberOfPayments;

    @ElementCollection
    private List<InstallmentPlan> installmentPlan = new ArrayList<>();

}
