package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Share implements Serializable {

    private static final long serialVersionUID = 1L;

    private int shareId;
    private String memberId;
    private int numberOfShares;
    private double sharePriceAtPurchase;
    private Timestamp purchaseDate;
    private double totalValue;
    private String status;

    public Share() {}

    public int getShareId() { return shareId; }
    public void setShareId(int shareId) { this.shareId = shareId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

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
}
