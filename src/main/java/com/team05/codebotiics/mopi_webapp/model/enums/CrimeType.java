package com.team05.codebotiics.mopi_webapp.model.enums;

/**
 * Represents an incident's crime type pursuant to
 * <a href="https://www.app.college.police.uk/app-content/information-management/management-of-police-information/collection-and-recording/#key-roles-in-recording">
 *     Management of Police Information guidelines</a>
 */
public enum CrimeType {
    GROUP_1("Group 1"),
    GROUP_2("Group 2"),
    GROUP_3("Group 3");

    private final String displayValue;
    
    CrimeType(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
