package rw.koperative.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meeting_attendance",
       uniqueConstraints = @UniqueConstraint(columnNames = {"meeting_id", "member_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MeetingAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Boolean attended;

    @Column(name = "fine_issued", nullable = false)
    private Boolean fineIssued;

    @PrePersist
    protected void onCreate() {
        if (attended == null) attended = false;
        if (fineIssued == null) fineIssued = false;
    }
}
