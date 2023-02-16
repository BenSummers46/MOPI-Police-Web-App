package com.team05.codebotiics.mopi_webapp.model.beans;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.NotNull;

public class LicenseDTO {

    @NotNull(message = "Field cannot be empty")
    private int personId;

    @NotNull(message = "Field cannot be empty")
    private int licenseId;

    public int getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(int licenseId) {
        this.licenseId = licenseId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}