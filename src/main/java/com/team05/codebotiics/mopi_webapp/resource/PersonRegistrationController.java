package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.AuditTrail;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import com.team05.codebotiics.mopi_webapp.repository.AuditTrailRepository;
import com.team05.codebotiics.mopi_webapp.repository.PersonRepository;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class PersonRegistrationController {

    private final PoliceRepository policeRepo; 
    private final PersonRepository personRepo; 

    public PersonRegistrationController(PoliceRepository policeRepo, PersonRepository personRepo)
    {
        this.policeRepo = policeRepo;
        this.personRepo = personRepo;
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
     * GET mapping for person registration form page.
     *
     * @param police Refers to police instance.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @GetMapping("/person-registration")
    public String getPersonRegistrationForm(Police police, Model model, Principal principal)      
{  
        Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        if (policeOptional.isPresent()) {
            Police policeUser = policeOptional.get();
            model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
        }

        police.setRank_l("placeholder");
        police.setPasswordHash("Password1");
        police.setConfirmPassword("Password1");

        return "registration/person_registration";
    }

    /**
     * POST mapping for person registration form page. Redirects the user back to home page when succesful.
     *
     * @param police Refers to police instance.
     * @param result Contains the result of the validation.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @PostMapping("/person-registration")
    public String submitPersonRegistrationForm(@Valid Police police, BindingResult result, Model model, Principal principal)
    {
        //Record to Audit Trail
        String eventDescription= "Registered new Person with PersonID: "+police.getPerson().getPersonId();
        recordEvent(Event.ADD, eventDescription, principal);

        if (result.hasErrors())
        {
            Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
            if (policeOptional.isPresent()) {
            Police policeUser = policeOptional.get();
            model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
        }
            return "registration/person_registration";
        }

        var person = police.getPerson();
        String address = person.getAddressLine1() + "," + person.getAddressLine2() + "," + person.getCountry() + "," +
                        person.getCity() + "," + person.getTown() + "," + person.getCounty() + "," + person.getPostCode();

        person.setAddressPerson(address);
        
        police.setPerson(person);
        
        personRepo.save(person);

        return "redirect:/home";
    }
}

