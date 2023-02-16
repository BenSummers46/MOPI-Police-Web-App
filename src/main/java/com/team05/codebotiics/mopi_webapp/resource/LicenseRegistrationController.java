package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.*;
import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import com.team05.codebotiics.mopi_webapp.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
public class LicenseRegistrationController {

    private final PersonRepository personRepo;
    private final PoliceRepository policeRepo;
    private final LicenseRepository licenseRepo;

    public LicenseRegistrationController(PoliceRepository policeRepo, PersonRepository personRepo, LicenseRepository licenseRepo)
    {
        this.policeRepo = policeRepo;
        this.personRepo = personRepo;
        this.licenseRepo = licenseRepo;
    }

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
     * GET mapping for license registration form page.
     *
     * @param license Refers to license instance.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @GetMapping("/license-registration")
    public String getLicenseRegistrationForm(Licenses licenses, LicenseDTO licenseDTO, Model model, Principal principal)      
{  
        Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        if (policeOptional.isPresent()) {
            Police policeUser = policeOptional.get();
            model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
        }

        return "registration/license_registration";
    }

    /**
     * POST mapping for license registration form page. Redirects the user back to home page when succesful.
     *
     * @param license Refers to license instance.
     * @param result Contains the result of the validation.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @PostMapping("/license-registration")
    public String submitLicenseRegistrationForm(@Valid Licenses licenses, BindingResult result, Model model, Principal principal)
    {

        if (result.hasErrors())
        {
            Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
            if (policeOptional.isPresent()) {
                Police policeUser = policeOptional.get();
                model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
            }
            return "registration/license_registration";
        }
       
        licenseRepo.save(licenses);
        //Record to Audit Trail
        String eventDescription= "Registered new License with LicenseID: "+ licenseRepo.findByLicenseType(licenses.getLicenseType()).get().getLicenseId();
        recordEvent(Event.ADD, eventDescription, principal);
        return "redirect:/home";
            
    }

    /**
     * POST mapping for license registration (link) form page. Redirects the user back to home page when succesful.
     *
     * @param licenseDTO Refers to licenseDTO instance.
     * @param result Contains the result of the validation.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @PostMapping("/license-registration-person")
    public String submitLicensePersonRegistrationForm(@Valid LicenseDTO licenseDTO, BindingResult result, Model model, Principal principal)
    {
        //Record to Audit Trail
        String eventDescription= "Added new instance of License with licenseID="+licenseDTO.getLicenseId()+" to person with PersonID: "+licenseDTO.getPersonId();
        recordEvent(Event.UPDATE, eventDescription, principal);

        if (result.hasErrors())
        {
            Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
            if (policeOptional.isPresent()) {
                Police policeUser = policeOptional.get();
                model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
            }
            return "registration/license_registration";
        }

        final Person person;
        final Licenses license;

            Optional<Person> personOptional = personRepo.findById(licenseDTO.getPersonId());
            Optional<Licenses> licenseOptional = licenseRepo.findById(licenseDTO.getLicenseId());

            if ( personOptional.isPresent() && licenseOptional.isPresent() ) {
                person = personOptional.get();
                license = licenseOptional.get();
            } else {
                return "registration/license_registration";
            }

            // Add person to the list
            license.getPersons().add(person);

            // Add license to the person
            person.getLicense().add(license);

        licenseRepo.save(license);
        
        return "redirect:/home";
            
    }
}

