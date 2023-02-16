package com.team05.codebotiics.mopi_webapp.model.enums;

/**
 * Represents the user's role pursuant to
 * <a href="https://www.app.college.police.uk/app-content/information-management/management-of-police-information/collection-and-recording/#key-roles-in-recording">
 *     Management of Police Information guidelines</a>
 */
public enum Role {
    USER("User"),
    MANAGER("Manager"),
    SUPERVISOR("Supervisor");

    private final String displayValue;
    
    Role(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
