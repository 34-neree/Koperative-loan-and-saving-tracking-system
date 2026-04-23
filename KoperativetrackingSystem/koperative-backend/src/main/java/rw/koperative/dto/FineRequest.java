package rw.koperative.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FineRequest {
    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;
}
