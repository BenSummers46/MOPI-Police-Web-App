package com.team05.codebotiics.mopi_webapp.model.enums;

public enum Rank {
    CONSTABLE("Constable"),
    SERGEANT("Sergeant"),
    INSPECTOR("Inspector"),
    CHIEF_INSPECTOR("Chief Inspector"),
    SUPERINTENDENT("Superintendent"),
    CHIEF_SUPERINTENDENT("Chief Superintendent"),
    ASSISTANT_CHIEF_CONSTABLE("Assistant Chief Constable"),
    DEPUTY_CHIEF_CONSTABLE("Deputy Chief Constable"),
    CHIEF_CONSTABLE("Chief Constable");

    private final String displayValue;
    
    Rank(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
