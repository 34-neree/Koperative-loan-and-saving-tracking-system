package rw.koperative.model;

import jakarta.persistence.*;
import lombok.*;
import rw.koperative.model.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "amount_requested", nullable = false, precision = 15, scale = 2)
    private BigDecimal amountRequested;

    @Column(name = "amount_approved", precision = 15, scale = 2)
    private BigDecimal amountApproved;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @Column(columnDefinition = "TEXT")
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "disbursed_date")
    private LocalDateTime disbursedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guarantor1_id")
    private Member guarantor1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guarantor2_id")
    private Member guarantor2;

    // Repayment schedule
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoanRepayment> repayments;

    @PrePersist
    protected void onCreate() {
        if (appliedDate == null) appliedDate = LocalDateTime.now();
        if (status == null) status = LoanStatus.APPLIED;
        if (interestRate == null) interestRate = new BigDecimal("10.00");
        if (termMonths == null) termMonths = 12;
    }
}
