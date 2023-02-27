package com.milos.loanchallange.model.database;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    private Integer numberMonths;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "installment_plan_id", referencedColumnName = "id")
    private InstallmentPlan installmentPlan;

}
