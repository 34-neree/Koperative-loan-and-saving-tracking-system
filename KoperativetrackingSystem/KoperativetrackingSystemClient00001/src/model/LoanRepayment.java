package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class LoanRepayment implements Serializable {

    private static final long serialVersionUID = 1L;

    private int repaymentId;
    private int loanId;
    private Timestamp dueDate;
    private double amountDue;
    private double amountPaid;
    private Timestamp paidDate;
    private String status;
    private double penaltyApplied;

    public LoanRepayment() {}

    public int getRepaymentId() { return repaymentId; }
    public void setRepaymentId(int repaymentId) { this.repaymentId = repaymentId; }

    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

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
}
