package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "fines")
public class Fine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fineId;

    @Column(length = 500)
    private String reason;

    private double amount;

    @Column(name = "issued_date")
    private Timestamp issuedDate;

    @Column(length = 20)
    private String status; // UNPAID, PAID

    @Column(name = "paid_date")
    private Timestamp paidDate;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Fine() {}

    public int getFineId() { return fineId; }
    public void setFineId(int fineId) { this.fineId = fineId; }

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

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
}
