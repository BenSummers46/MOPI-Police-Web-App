package com.team05.codebotiics.mopi_webapp.model.enums;

public enum LicenseType {
    Placeholder("Placeholder");

    private final String displayValue;
    
    LicenseType(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
