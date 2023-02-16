package com.team05.codebotiics.mopi_webapp.model.beans;

import com.team05.codebotiics.mopi_webapp.model.enums.CrimeType;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the "Incident Report" table.
 */

@Entity
@Table(name = "Incident_Report")
public class IncidentReport {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "Incident_URN")
    private int incidentURN;

    @Basic
    @Column(name = "Date_and_Time")
    @NotNull( message = "Please enter date/time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateAndTime;

    @Basic
    @Column(name = "latitude")
    private double latitude;

    @Basic
    @Column(name = "longitude")
    private double longitude;

    @Basic
    @Column(name = "Crime_Type")
    @Enumerated(EnumType.STRING)
    private CrimeType crimeType;

    @Column(name="Description")
    @NotBlank(message = "Field cannot be empty")
    @Size(max=150, message = "Field must contain less than 150 characters")
    private String description;

    @Column(name="Security_Access_Level")
    @Enumerated(EnumType.STRING)
    private SecurityAccessLevel securityAccessLevel;

    public Set<Person> getSuspects() {
        return suspects;
    }

    public void setSuspects(Set<Person> suspects) {
        this.suspects = suspects;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="SuspectLink",
        joinColumns = {@JoinColumn(name="Incident_URN")},
        inverseJoinColumns = {@JoinColumn(name="Person_ID")})
    private Set<Person> suspects = new HashSet<>();

    @OneToMany(mappedBy = "incidentReport")
    private Set<Evidence> evidence;

    @Column(name="Badge_Number")
    private int badgeNumber;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="Badge_Number", referencedColumnName = "Badge_Number")
    private Police police;

    public IncidentReport() {
    }

    public Set<Evidence> getEvidence() {
        return evidence;
    }

    public void setEvidence(Set<Evidence> evidence) {
        this.evidence = evidence;
    }

    public int getIncidentURN() {
        return incidentURN;
    }

    public void setIncidentURN(int incidentUrn) {
        this.incidentURN = incidentUrn;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public CrimeType getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(CrimeType crimeType) {
        this.crimeType = crimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SecurityAccessLevel getSecurityAccessLevel() {
        return securityAccessLevel;
    }

    public void setSecurityAccessLevel(SecurityAccessLevel securityAccessLevel) {
        this.securityAccessLevel = securityAccessLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentReport incidentReport = (IncidentReport) o;
        return incidentURN == incidentReport.incidentURN &&
                Objects.equals(badgeNumber, incidentReport.badgeNumber) &&
                Objects.equals(dateAndTime, incidentReport.dateAndTime) &&
                Objects.equals(latitude, incidentReport.latitude) &&
                Objects.equals(latitude, incidentReport.longitude) &&
                Objects.equals(crimeType, incidentReport.crimeType) &&
                Objects.equals(description, incidentReport.description) &&
                Objects.equals(securityAccessLevel, incidentReport.securityAccessLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(incidentURN, badgeNumber, dateAndTime, latitude, longitude, crimeType, description, securityAccessLevel);
    }

    public Police getPolice() {
        return police;
    }

    public void setPolice(Police police) {
        this.police = police;
    }
}

