package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.*;
import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import com.team05.codebotiics.mopi_webapp.repository.AuditTrailRepository;
import com.team05.codebotiics.mopi_webapp.repository.IncidentReportRepository;
import com.team05.codebotiics.mopi_webapp.repository.PersonRepository;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class SuspectRegistrationSearchController {

    PersonRepository personRepo;
    IncidentReportRepository incidentRepo;

    @Autowired
    public void setPersonRepo(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @Autowired
    public void setIncidentRepo(IncidentReportRepository incidentRepo) {
        this.incidentRepo = incidentRepo;
    }

    @Autowired
    PoliceRepository policeRepo;

    @Autowired
    AuditTrailRepository auditTrailRepo;

    /**
     * Methods use this to record to the audit_trail what the user has done.
     * @param event What kind of operation was performed.
     * @param description Description of operation.
     * @param principal Contains security information about the client. Used for identification purposes
     */
    private void recordEvent(Event event, String description, Principal principal){
        Optional<Police> currentUser=policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        LocalDateTime timestamp= LocalDateTime.now();
        AuditTrail auditTrail= new AuditTrail(currentUser.get(), timestamp, event, description);
        auditTrailRepo.save(auditTrail);
    }

    /**
     * POST mapping for suspect registration form page. Returns incident data from db when succesful.
     *
     * @param errors Catches any errors
     * @param searchCrimeType Refers to a crime type instance used to filter data
     * @return a string of results from db
     */
    @PostMapping("/suspect/search/byCrimeType")
    public ResponseEntity<?> getSearchResultViaAjax(
            @RequestBody CrimeTypeDTO searchCrimeType, Errors errors) {

        final List<IncidentReport> incidentReports;
        final Optional<List<IncidentReport>> reportOptional;

        AjaxResponseBody<IncidentReport> result = new AjaxResponseBody<IncidentReport>();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg("400error");
            return ResponseEntity.ok(result);
        } 

        reportOptional = incidentRepo.findBycrimeTypeAndSecurityAccessLevelIn(searchCrimeType.getCrimeType(), searchCrimeType.getAccessLevels());
        incidentReports = reportOptional.get();

        // Remove the connection between tables
        incidentReports.forEach(incident->{
            incident.setSuspects(null);
            incident.setPolice(null);
            incident.setEvidence(null);
        });
        result.setResult(incidentReports);

        result.setMsg("OK");

        return ResponseEntity.ok(result);

    }

    /**
     * POST mapping for suspect registration form page. Returns person data from db when succesful.
     *
     * @param errors Catches any errors
     * @param body Refers to a generic body
     * @return a string of results from db
     */
    @PostMapping("/suspect/search/getPersons")
    public ResponseEntity<?> getPersons(@RequestBody String body, Errors errors
            ) {

        final List<Person> persons;
        
        AjaxResponseBody<Person> result = new AjaxResponseBody<Person>();

        persons = personRepo.findAll();

        if (errors.hasErrors()) {

            result.setMsg("400error");
            return ResponseEntity.ok(result);
        }         

        //If error, just return a 400 bad request, along with the error message
        if (persons.isEmpty()) {

            result.setMsg("Cannot find any persons");
            return ResponseEntity.ok(result);
            
        }
        
        // Remove the connection between tables
        persons.forEach(person->{
            person.setLicenses(null);
            person.setIncidentReports(null);
        });
        result.setResult(persons);

        result.setMsg("OK");

        return ResponseEntity.ok(result);

    }

    /**
     * POST mapping for suspect registration form page. Returns incident data from db when succesful.
     *
     * @param errors Catches any errors
     * @param body Refers to a generic body
     * @return a string of results from db
     */
    @PostMapping("/suspect/search/getReports")
    public ResponseEntity<?> getReports(@RequestBody String body, Errors errors
            ) {

        final List<IncidentReport> incidentReports;
        
        AjaxResponseBody<IncidentReport> result = new AjaxResponseBody<IncidentReport>();

        incidentReports = incidentRepo.findAll();

        if (errors.hasErrors()) {

            result.setMsg("400error");
            return ResponseEntity.ok(result);
        }         

        //If error, just return a 400 bad request, along with the error message
        if (incidentReports.isEmpty()) {

            result.setMsg("Cannot find any persons");
            return ResponseEntity.ok(result);
            
        }
        
        // Remove the connection between tables
        incidentReports.forEach(incident->{
            incident.setSuspects(null);
            incident.setEvidence(null);
            incident.setPolice(null);
        });
        result.setResult(incidentReports);

        result.setMsg("OK");

        return ResponseEntity.ok(result);

    }
}
