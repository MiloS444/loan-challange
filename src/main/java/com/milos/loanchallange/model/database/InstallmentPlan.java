package com.milos.loanchallange.model.database;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstallmentPlan {

    private BigDecimal paymentAmount;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal balanceOwed;

}
