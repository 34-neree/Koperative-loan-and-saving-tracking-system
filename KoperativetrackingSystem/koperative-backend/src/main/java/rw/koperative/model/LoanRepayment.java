package rw.koperative.model;

import jakarta.persistence.*;
import lombok.*;
import rw.koperative.model.enums.RepaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_repayments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoanRepayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "amount_due", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountDue;

    @Column(name = "amount_paid", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RepaymentStatus status;

    @Column(name = "penalty_applied", nullable = false, precision = 15, scale = 2)
    private BigDecimal penaltyApplied;

    @PrePersist
    protected void onCreate() {
        if (amountPaid == null) amountPaid = BigDecimal.ZERO;
        if (penaltyApplied == null) penaltyApplied = BigDecimal.ZERO;
        if (status == null) status = RepaymentStatus.PENDING;
    }
}
