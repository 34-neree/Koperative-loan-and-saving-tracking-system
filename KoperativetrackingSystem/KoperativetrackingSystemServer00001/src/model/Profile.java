package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "profiles")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId;

    private String email;
    private String occupation;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Profile() {}

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }
 
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Member getMember() {
        return member;
    }
 
    public void setMember(Member member) {
        this.member = member;
    }
}
