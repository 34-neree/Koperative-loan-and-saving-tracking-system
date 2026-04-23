package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "loans")
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;

    private double amount;
    private double interest;
    private int repaymentPeriod;
    private double totalPayable;
    private double outstanding;
    private String status;

    @Column(updatable = false)
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Loan() {}

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
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

    public Member getMember() {
        return member;
    }
 
    public void setMember(Member member) {
        this.member = member;
    }
}
