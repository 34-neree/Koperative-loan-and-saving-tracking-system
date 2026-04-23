package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ShareConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private int configId;
    private double sharePrice;
    private int minimumSharesForLoan;
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
