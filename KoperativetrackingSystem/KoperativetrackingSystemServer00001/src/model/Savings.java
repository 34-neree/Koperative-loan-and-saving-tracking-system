package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "savings")
public class Savings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int savingId;

    private double amount;

    @Column(name = "balance_after")
    private double balanceAfter;

    private String remark;

    @Column(updatable = false)
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Savings() {}

    public int getSavingId() {
        return savingId;
    }

    public void setSavingId(int savingId) {
        this.savingId = savingId;
    }

    public double getAmount() {
        return amount;
    }
 
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }
 
    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getRemark() {
        return remark;
    }
 
    public void setRemark(String remark) {
        this.remark = remark;
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
