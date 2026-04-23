package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "shares")
public class Share implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shareId;

    private int numberOfShares;

    @Column(name = "share_price_at_purchase")
    private double sharePriceAtPurchase;

    @Column(name = "purchase_date")
    private Timestamp purchaseDate;

    @Column(name = "total_value")
    private double totalValue;

    @Column(length = 20)
    private String status; // PAID, PARTIAL, PENDING

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Share() {}

    public int getShareId() { return shareId; }
    public void setShareId(int shareId) { this.shareId = shareId; }

    public int getNumberOfShares() { return numberOfShares; }
    public void setNumberOfShares(int numberOfShares) { this.numberOfShares = numberOfShares; }

    public double getSharePriceAtPurchase() { return sharePriceAtPurchase; }
    public void setSharePriceAtPurchase(double sharePriceAtPurchase) { this.sharePriceAtPurchase = sharePriceAtPurchase; }

    public Timestamp getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Timestamp purchaseDate) { this.purchaseDate = purchaseDate; }

    public double getTotalValue() { return totalValue; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
}
