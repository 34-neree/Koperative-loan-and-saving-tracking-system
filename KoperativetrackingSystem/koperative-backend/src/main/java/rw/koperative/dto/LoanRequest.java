package rw.koperative.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoanRequest {
    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1000", message = "Minimum loan amount is 1000 RWF")
    private BigDecimal amountRequested;

    private BigDecimal interestRate;
    private Integer termMonths;
    private String purpose;
    private Long guarantor1Id;
    private Long guarantor2Id;
}
