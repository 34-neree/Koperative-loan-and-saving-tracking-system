package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Fine implements Serializable {

    private static final long serialVersionUID = 1L;

    private int fineId;
    private String memberId;
    private String reason;
    private double amount;
    private Timestamp issuedDate;
    private String status;
    private Timestamp paidDate;

    public Fine() {}

    public int getFineId() { return fineId; }
    public void setFineId(int fineId) { this.fineId = fineId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Timestamp getIssuedDate() { return issuedDate; }
    public void setIssuedDate(Timestamp issuedDate) { this.issuedDate = issuedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getPaidDate() { return paidDate; }
    public void setPaidDate(Timestamp paidDate) { this.paidDate = paidDate; }
}
