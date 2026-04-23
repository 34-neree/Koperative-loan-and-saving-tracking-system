package rw.koperative.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AttendanceRequest {
    // Map of memberId → attended (true/false)
    private Map<Long, Boolean> attendance;
}
