package com.team05.codebotiics.mopi_webapp.model.beans;

import com.team05.codebotiics.mopi_webapp.model.enums.Role;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Set;

/**
 * Represents the "Police" table.
 */
@Entity
@Table(name= "Police")
public class Police {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Badge_Number")
    private int badgeNumber;

    @OneToOne(optional = false)
    @JoinColumn(name="Person_ID")
    @Valid
    private Person person;

    @Column(name = "Password_Hash")
    @Size(min=1, message = "Please insert a password")
    private String passwordHash;

    @Transient
    @Size(min=1, message = "Password does not match")
    private String confirmPassword;

    @OneToMany(mappedBy = "police")
    private Set<NRACReview> nracReviews;

    @OneToMany(mappedBy = "police")
    private Set<AuditTrail> auditTrail;

    @OneToMany(mappedBy = "police")
    private Set<IncidentReport> reports;

    @Column(name = "Rank_l")
    @NotBlank( message = "No option has been selected")
    private String rank_l;

    @Column(name= "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name= "Security_Access_Level")
    @Enumerated(EnumType.STRING)
    private SecurityAccessLevel securityAccessLevel;

    public Police(Police police){
        this.badgeNumber= police.getBadgeNumber();
        this.passwordHash= police.getPasswordHash();
        this.person= police.getPerson();
        this.rank_l = police.getRank_l();
        this.role= police.getRole();
        this.securityAccessLevel= police.getSecurityAccessLevel();
        this.nracReviews = police.getNracReviews();
        this.reports = police.getReports();
        this.confirmPassword = police.getConfirmPassword();
    }

    public Police() {}

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<NRACReview> getNracReviews() {
        return nracReviews;
    }

    public void setNracReviews(Set<NRACReview> nracReviews) {
        this.nracReviews = nracReviews;
    }

    public Set<IncidentReport> getReports() {
        return reports;
    }

    public void setReports(Set<IncidentReport> reports) {
        this.reports = reports;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public Role getRole(){
        return role;
    }

    public void setSecurityAccessLevel(SecurityAccessLevel securityAccessLevel) {
        this.securityAccessLevel = securityAccessLevel;
    }

    public SecurityAccessLevel getSecurityAccessLevel() {
        return securityAccessLevel;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // MODIFICATIONS ADDED

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkPassword();
    }

    private void checkPassword() {
        if(this.passwordHash == null || this.confirmPassword == null){
            return;
        }else if(!this.passwordHash.equals(confirmPassword)){
            this.confirmPassword = null;
        }
    }

    protected String getPassword() {
        return null;
    }

    public String getRank_l() {
        return rank_l;
    }

    public void setRank_l(String rank) {
        if(rank.equals("-1")){
            this.rank_l = null;            
        }
        else{
            this.rank_l = rank;
        }
    }
}
