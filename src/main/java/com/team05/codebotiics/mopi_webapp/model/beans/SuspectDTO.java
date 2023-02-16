package com.team05.codebotiics.mopi_webapp.model.beans;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.NotNull;

public class SuspectDTO {

    @NotNull(message = "Field cannot be empty")
    private int incidentURN;

    @NotNull(message = "Field cannot be empty")
    private int personId;

    private List<IncidentReport> incidentReports;
    private List<Person> persons;

    public int getIncidentURN() {
        return incidentURN;
    }

    public void setIncidentURN(int incidentURN) {
        this.incidentURN = incidentURN;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public List<IncidentReport> getIncidentReports() {
        return incidentReports;
    }

    public void setIncidentReports(List<IncidentReport> incidentReports) {
        this.incidentReports = incidentReports;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

}