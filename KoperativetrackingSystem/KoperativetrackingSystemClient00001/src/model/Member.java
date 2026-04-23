package model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    private String memberId;
    private String nationalId;
    private String fullName;
    private String gender;
    private String phone;
    private String address;
    private String password;
    private double totalSavings;
    private double loanBalance;

    /* RELATIONSHIPS (for RMI data transfer only) */
    private Profile profile;
    private List<Loan> loans;
    private List<Savings> savingsList;
    private Set<Role> roles;

    /* CONSTRUCTOR */
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
