package com.team05.codebotiics.mopi_webapp.model.enums;

/**
 * Represents the users Security Access Level pursuant to <a href="https://www.app.college.police.uk/app-content/information-management/management-of-police-information/collection-and-recording/#government-security-classification">
 *     Management of Police Information guidelines</a>
 */
public enum SecurityAccessLevel {
    OFFICIAL("Official"),
    OFFICIAL_SENSITIVE("Official Sensitive"),
    SECRET("Secret"),
    TOP_SECRET("Top Secret");

    private final String displayValue;
    
    SecurityAccessLevel(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
