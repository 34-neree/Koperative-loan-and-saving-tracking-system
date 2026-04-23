package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "member_id", length = 30)
    private String memberId;

    @Column(name = "national_id", length = 20, nullable = false, unique = true)
    private String nationalId;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(length = 10)
    private String gender;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 150)
    private String address;

    @Column(nullable = false)
    private String password;

    @Column(name = "total_savings")
    private double totalSavings;

    @Column(name = "loan_balance")
    private double loanBalance;

    /* RELATIONSHIPS */

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Loan> loans;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Savings> savingsList;

    @ManyToMany
    @JoinTable(
        name = "member_roles",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /* CONSTRUCTORS */
    public Member() {}

    /* GETTERS & SETTERS */

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

    public double getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(double totalSavings) {
        this.totalSavings = totalSavings;
    }

    public double getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(double loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public List<Savings> getSavingsList() {
        return savingsList;
    }

    public void setSavingsList(List<Savings> savingsList) {
        this.savingsList = savingsList;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
