package rw.koperative.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberResponse {
    private Long id;
    private String nationalId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String cell;
    private String sector;
    private String district;
    private String nextOfKin;
    private String nextOfKinPhone;
    private LocalDate membershipDate;
    private String status;
    private LocalDateTime createdAt;

    // Aggregated data for profile
    private BigDecimal totalSavings;
    private Integer totalShares;
    private BigDecimal activeLoanBalance;
    private BigDecimal totalFinesUnpaid;
}
