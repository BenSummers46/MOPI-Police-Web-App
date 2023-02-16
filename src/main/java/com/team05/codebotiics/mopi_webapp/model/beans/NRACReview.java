package com.team05.codebotiics.mopi_webapp.model.beans;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents the "NRAC Review" table
 */
@Entity
@Table(name = "NRAC_Review")
public class NRACReview {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "NRAC_ID")
    private int nracId;

    @Basic
    @Column(name = "Action")
    private String action;

    @Basic
    @Column(name = "Description")
    private String description;

    @Basic
    @Column(name = "Security_Access_Level")
    private String securityAccessLevel;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="Badge_Number", referencedColumnName = "Badge_Number")
    private Police police;


    public int getNracId() {
        return nracId;
    }

    public void setNracId(int nracId) {
        this.nracId = nracId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecurityAccessLevel() {
        return securityAccessLevel;
    }

    public void setSecurityAccessLevel(String securityAccessLevel) {
        this.securityAccessLevel = securityAccessLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NRACReview that = (NRACReview) o;
        return nracId == that.nracId &&
                Objects.equals(action, that.action) &&
                Objects.equals(description, that.description) &&
                Objects.equals(securityAccessLevel, that.securityAccessLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nracId, action, description, securityAccessLevel);
    }
}
