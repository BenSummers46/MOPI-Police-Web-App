package com.team05.codebotiics.mopi_webapp.model.beans;

import java.util.List;

import com.team05.codebotiics.mopi_webapp.model.enums.CrimeType;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;

public class CrimeTypeDTO {

    CrimeType crimeType;
    List<SecurityAccessLevel> accessLevels;

    public CrimeType getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(CrimeType crimeType) {
        this.crimeType = crimeType;
    }

    public List<SecurityAccessLevel> getAccessLevels() {
        return accessLevels;
    }

    public void setAccessLevels(List<SecurityAccessLevel> accessLevels) {
        this.accessLevels = accessLevels;
    }

}