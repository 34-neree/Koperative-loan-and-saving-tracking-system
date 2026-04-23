package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    private String memberId;
    private double amount;
    private double interest; // fixed at 10% on server
    private int repaymentPeriod;
    private double totalPayable;
    private double outstanding;
    private String status;
    private Timestamp date;

    public Loan() {}

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public double getAmount() {
        return amount;
    }
 
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterest() {
        return interest;
    }
 
    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getRepaymentPeriod() {
        return repaymentPeriod;
    }

    public void setRepaymentPeriod(int repaymentPeriod) {
        this.repaymentPeriod = repaymentPeriod;
    }

    public double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
    }

    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDate() {
        return date;
    }
 
    public void setDate(Timestamp date) {
        this.date = date;
    }
}
