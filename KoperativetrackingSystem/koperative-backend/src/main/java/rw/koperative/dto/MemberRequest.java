package rw.koperative.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberRequest {
    @NotBlank(message = "National ID is required")
    @Size(max = 20)
    private String nationalId;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Phone is required")
    @Size(max = 20)
    private String phone;

    @Email
    private String email;

    private String cell;
    private String sector;
    private String district;
    private String nextOfKin;
    private String nextOfKinPhone;
}
