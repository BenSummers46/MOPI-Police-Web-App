package com.team05.codebotiics.mopi_webapp.model.beans;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the "Evidence" table.
 *
 */
@Entity
@Table(name="Evidence")
public class Evidence {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "Evidence_ID")
    private int evidenceId;

    @Column(name="Incident_URN")
    private int incidentURN;

    @Basic
    @Column(name = "Name")
    private String name;

    @Basic
    @Lob
    @Column(name = "Evidence_file", length=16777215)//This length forces the data to be stored as a longblob, longblobs can store a maximum of 4GB
    private byte[] evidenceFile;

    @Basic
    @Column(name = "Security_Access_Level")
    private String securityAccessLevel;

    @Basic
    @Column(name = "Description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name="Incident_URN", referencedColumnName = "Incident_URN")
    private IncidentReport incidentReport;


    public IncidentReport getIncidentReport() {
        return incidentReport;
    }

    public void setIncidentReport(IncidentReport incidentReport) {
        this.incidentReport = incidentReport;
    }

    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getEvidenceFile() {
        return evidenceFile;
    }

    public void setEvidenceFile(byte[] evidenceFile) {
        this.evidenceFile = evidenceFile;
    }

    public String getSecurityAccessLevel() {
        return securityAccessLevel;
    }

    public void setSecurityAccessLevel(String securityAccessLevels) {
        this.securityAccessLevel = securityAccessLevels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evidence evidence = (Evidence) o;
        return evidenceId == evidence.evidenceId &&
                Objects.equals(name, evidence.name) &&
                Arrays.equals(evidenceFile, evidence.evidenceFile) &&
                Objects.equals(securityAccessLevel, evidence.securityAccessLevel) &&
                Objects.equals(description, evidence.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(evidenceId, name, securityAccessLevel, description);
        result = 31 * result + Arrays.hashCode(evidenceFile);
        return result;
    }
}
