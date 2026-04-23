package rw.koperative.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MeetingRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Meeting date is required")
    private LocalDateTime meetingDate;

    private String location;
    private String agenda;
    private String minutes;
}
