package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Savings implements Serializable {

    private static final long serialVersionUID = 1L;

    private String memberId;
    private double amount;
    private double balanceAfter;
    private String remark;
    private Timestamp date;
    private String description;

    public Savings() {}

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

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getDescription() {
        return description;
    }

public void setDescription(String description) {
    this.description = description;
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
}
