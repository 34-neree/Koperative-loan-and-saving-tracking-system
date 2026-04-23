package rw.koperative.model;

import jakarta.persistence.*;
import lombok.*;
import rw.koperative.model.enums.ShareStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "shares")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "number_of_shares", nullable = false)
    private Integer numberOfShares;

    @Column(name = "share_price_at_purchase", nullable = false, precision = 15, scale = 2)
    private BigDecimal sharePriceAtPurchase;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "total_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShareStatus status;

    @PrePersist
    protected void onCreate() {
        if (purchaseDate == null) purchaseDate = LocalDate.now();
        if (status == null) status = ShareStatus.PAID;
    }
}
