package rw.koperative.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ShareRequest {
    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Number of shares is required")
    @Min(value = 1, message = "Must purchase at least 1 share")
    private Integer numberOfShares;
}
