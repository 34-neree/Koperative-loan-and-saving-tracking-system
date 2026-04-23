package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "loan_repayments")
public class LoanRepayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int repaymentId;

    @Column(name = "due_date")
    private Timestamp dueDate;

    @Column(name = "amount_due")
    private double amountDue;

    @Column(name = "amount_paid")
    private double amountPaid;

    @Column(name = "paid_date")
    private Timestamp paidDate;

    @Column(length = 20)
    private String status; // PENDING, PAID, PARTIAL, OVERDUE

    @Column(name = "penalty_applied")
    private double penaltyApplied;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    public LoanRepayment() {}

    public int getRepaymentId() { return repaymentId; }
    public void setRepaymentId(int repaymentId) { this.repaymentId = repaymentId; }

    public Timestamp getDueDate() { return dueDate; }
    public void setDueDate(Timestamp dueDate) { this.dueDate = dueDate; }

    public double getAmountDue() { return amountDue; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public Timestamp getPaidDate() { return paidDate; }
    public void setPaidDate(Timestamp paidDate) { this.paidDate = paidDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getPenaltyApplied() { return penaltyApplied; }
    public void setPenaltyApplied(double penaltyApplied) { this.penaltyApplied = penaltyApplied; }

    public Loan getLoan() { return loan; }
    public void setLoan(Loan loan) { this.loan = loan; }
}
