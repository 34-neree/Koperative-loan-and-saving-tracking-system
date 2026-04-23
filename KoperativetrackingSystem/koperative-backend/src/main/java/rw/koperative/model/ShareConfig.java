package rw.koperative.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "share_config")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ShareConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "share_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal sharePrice;

    @Column(name = "minimum_shares_for_loan", nullable = false)
    private Integer minimumSharesForLoan;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
