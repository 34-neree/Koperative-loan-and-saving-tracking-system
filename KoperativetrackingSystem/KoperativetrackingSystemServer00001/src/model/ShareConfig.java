package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "share_config")
public class ShareConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int configId;

    @Column(name = "share_price")
    private double sharePrice;

    @Column(name = "minimum_shares_for_loan")
    private int minimumSharesForLoan;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public ShareConfig() {}

    public int getConfigId() { return configId; }
    public void setConfigId(int configId) { this.configId = configId; }

    public double getSharePrice() { return sharePrice; }
    public void setSharePrice(double sharePrice) { this.sharePrice = sharePrice; }

    public int getMinimumSharesForLoan() { return minimumSharesForLoan; }
    public void setMinimumSharesForLoan(int minimumSharesForLoan) { this.minimumSharesForLoan = minimumSharesForLoan; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
