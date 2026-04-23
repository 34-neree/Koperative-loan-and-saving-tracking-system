package rw.koperative.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardStats {
    private long totalMembers;
    private BigDecimal totalSavings;
    private BigDecimal totalLoansDisbursed;
    private long pendingLoanApprovals;
    private long overdueRepayments;
    private BigDecimal totalUnpaidFines;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
}
