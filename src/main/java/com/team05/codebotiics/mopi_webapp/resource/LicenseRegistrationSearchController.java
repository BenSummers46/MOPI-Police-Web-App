package com.team05.codebotiics.mopi_webapp.resource;
import java.util.List;
import com.team05.codebotiics.mopi_webapp.model.beans.AjaxResponseBody;
import com.team05.codebotiics.mopi_webapp.model.beans.Licenses;
import com.team05.codebotiics.mopi_webapp.model.beans.Person;
import com.team05.codebotiics.mopi_webapp.repository.LicenseRepository;
import com.team05.codebotiics.mopi_webapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LicenseRegistrationSearchController {

    PersonRepository personRepo;
    LicenseRepository licenseRepo;

    @Autowired
    public void setPersonRepo(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @Autowired
    public void setLicenseRepo(LicenseRepository licenseRepo) {
        this.licenseRepo = licenseRepo;
    }

    /**
     * POST mapping for license registration form page. Returns license data from db when succesful.
     *
     * @param errors Catches any errors
     * @param body Refers to a generic body
     * @return a string of results from db
     */
    @PostMapping("/license/search/getLicenses")
    public ResponseEntity<?> getLicenses(@RequestBody String body, Errors errors
            ) {

        final List<Licenses> licenses;
        
        AjaxResponseBody<Licenses> result = new AjaxResponseBody<Licenses>();

        licenses = licenseRepo.findAll();

        if (errors.hasErrors()) {

            result.setMsg("400error");
            return ResponseEntity.ok(result);
        }         

        //If error, just return a 400 bad request, along with the error message
        if (licenses.isEmpty()) {

            result.setMsg("Cannot find any persons");
            return ResponseEntity.ok(result);
            
        }
        
        // Remove the connection between tables
        licenses.forEach(license->{
            license.setPersons(null);
        });

        result.setResult(licenses);

        result.setMsg("OK");

        return ResponseEntity.ok(result);
    }

    /**
     * POST mapping for license registration form page. Returns person data from db when succesful.
     *
     * @param errors Catches any errors
     * @param body Refers to a generic body
     * @return a string of results from db
     */
    @PostMapping("/license/search/getPersons")
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
}
