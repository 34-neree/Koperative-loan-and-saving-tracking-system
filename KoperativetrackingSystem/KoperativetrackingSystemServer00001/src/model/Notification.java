package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    @Column(length = 20)
    private String phone;

    @Column(length = 1000)
    private String message;

    @Column(length = 20)
    private String channel; // SMS, SYSTEM

    @Column(length = 20)
    private String status; // SENT, FAILED, PENDING

    @Column(name = "sent_at")
    private Timestamp sentAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Notification() {}

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getSentAt() { return sentAt; }
    public void setSentAt(Timestamp sentAt) { this.sentAt = sentAt; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
}
