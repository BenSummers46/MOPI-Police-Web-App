package com.team05.codebotiics.mopi_webapp.model.enums;

/**
 * Represents an individuals sex
 */
public enum Sex {
    MALE("Male"),
    FEMALE("Female");

    private final String displayValue;
    
    Sex(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
