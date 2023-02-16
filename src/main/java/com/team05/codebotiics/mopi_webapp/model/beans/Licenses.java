package com.team05.codebotiics.mopi_webapp.model.beans;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents the "Licenses" table.
 */
@Entity
@Table(name="Licenses")
public class Licenses {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "License_ID")
    private int licenseId;

    @Basic
    @Column(name = "License_Type")
    @NotBlank(message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    private String licenseType;

    @ManyToMany(mappedBy = "licenses")
    private Collection<Person> persons= new HashSet<>();

    public Collection<Person> getPersons() {
        return persons;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }

    public int getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(int licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Licenses licenses = (Licenses) o;
        return licenseId == licenses.licenseId &&
                Objects.equals(licenseType, licenses.licenseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licenseId, licenseType);
    }
}
 