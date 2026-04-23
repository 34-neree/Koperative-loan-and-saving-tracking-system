package rw.koperative.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NotificationRequest {
    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotBlank(message = "Message is required")
    private String message;

    private String channel; // SMS or SYSTEM
}
