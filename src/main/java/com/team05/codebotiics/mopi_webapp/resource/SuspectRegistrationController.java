package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.*;
import com.team05.codebotiics.mopi_webapp.model.enums.CrimeType;
import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;
import com.team05.codebotiics.mopi_webapp.repository.AuditTrailRepository;
import com.team05.codebotiics.mopi_webapp.repository.IncidentReportRepository;
import com.team05.codebotiics.mopi_webapp.repository.PersonRepository;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;

import org.hibernate.id.IncrementGenerator;
import org.hibernate.metamodel.model.domain.spi.SetPersistentAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ch.qos.logback.core.joran.conditional.ElseAction;

import java.security.Principal;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class SuspectRegistrationController {

  private final PoliceRepository policeRepo;
  private final IncidentReportRepository incidentRepo;

  @Autowired
  private PersonRepository personRepo;

  public SuspectRegistrationController(PoliceRepository policeRepo, IncidentReportRepository incidentRepo) {
    this.policeRepo = policeRepo;
    this.incidentRepo = incidentRepo;
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
   * GET mapping for suspect registration form page.
   *
   * @param suspectDTO Refers to suspectDTO instance.
   * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
   * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
   * @return a string of the appropriate page.
   */
  @GetMapping("/suspect-registration")
  public String getSuspectRegistrationForm(SuspectDTO suspectDTO, Model model, Principal principal)
{  
        final List<IncidentReport> incidentReports;
        final List<Person> personsList;
        final Collection<SecurityAccessLevel> accessLevels;
        final Optional<List<IncidentReport>> reportOptional;
        
        Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        if (policeOptional.isPresent()) {
            Police policeUser = policeOptional.get();
            model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));

            SecurityAccessLevel accessType = policeUser.getSecurityAccessLevel();
            personsList = personRepo.findAll();
            suspectDTO.setPersons(personsList);
            model.addAttribute("persons", personsList); // CHECK IF LIST IS EMPTY
            
            switch(accessType) {
 
                case TOP_SECRET:
                  // fetch all reports from db
                  accessLevels = new HashSet<>();
                  accessLevels.add(SecurityAccessLevel.TOP_SECRET);
                  accessLevels.add(SecurityAccessLevel.SECRET);
                  accessLevels.add(SecurityAccessLevel.OFFICIAL_SENSITIVE);
                  accessLevels.add(SecurityAccessLevel.OFFICIAL);

                  reportOptional = incidentRepo.findBysecurityAccessLevelIn(accessLevels);

                  if ( reportOptional.isPresent() ) {
                    incidentReports = reportOptional.get();
                    suspectDTO.setIncidentReports(incidentReports);
                    model.addAttribute("incidents", incidentReports);
                    model.addAttribute("accessLevels", listToString(accessLevels));
                  
                  } else { model.addAttribute("incidents", ""); }  // empty string or null
                  break;

                case SECRET:
                  // fetch reports of secret access and below
                  accessLevels = new HashSet<>();
                  accessLevels.add(SecurityAccessLevel.SECRET);
                  accessLevels.add(SecurityAccessLevel.OFFICIAL_SENSITIVE);
                  accessLevels.add(SecurityAccessLevel.OFFICIAL);

                  reportOptional = incidentRepo.findBysecurityAccessLevelIn(accessLevels);

                  if ( reportOptional.isPresent() ) {
                    incidentReports = reportOptional.get();
                    suspectDTO.setIncidentReports(incidentReports);
                    model.addAttribute("incidents", incidentReports);
                    model.addAttribute("accessLevels", listToString(accessLevels));
                  
                  } else { model.addAttribute("incidents", ""); }  // empty string or null     
                  break;

                case OFFICIAL_SENSITIVE:
                  // fetch reports of official-sensitive access and below
                  accessLevels = new HashSet<>();
                  accessLevels.add(SecurityAccessLevel.OFFICIAL_SENSITIVE);
                  accessLevels.add(SecurityAccessLevel.OFFICIAL);

                  reportOptional = incidentRepo.findBysecurityAccessLevelIn(accessLevels);

                  if ( reportOptional.isPresent() ) {
                    incidentReports = reportOptional.get();
                    suspectDTO.setIncidentReports(incidentReports);
                    model.addAttribute("incidents", incidentReports);                    
                    model.addAttribute("accessLevels", listToString(accessLevels));
                  
                  } else { model.addAttribute("incidents", ""); }  // empty string or null     
                break;

                case OFFICIAL:
                  // fetch reports of official access and below
                  accessLevels = new HashSet<>();
                  accessLevels.add(SecurityAccessLevel.OFFICIAL);

                  reportOptional = incidentRepo.findBysecurityAccessLevelIn(accessLevels);

                  if ( reportOptional.isPresent() ) {
                    incidentReports = reportOptional.get();
                    suspectDTO.setIncidentReports(incidentReports);
                    model.addAttribute("incidents", incidentReports);
                    model.addAttribute("accessLevels", listToString(accessLevels));
                    
                  } else { model.addAttribute("incidents", ""); }  // empty string or null                  
                  break;

                default:
                  // return error (tbd)
                  return "redirect:/error";
              }
              return "registration/suspect_registration";
        }
        else { return "redirect:/error";
    }

    }

    /**
   * POST mapping for suspect registration form page. Redirects the user back to home page when succesful.
   *
   * @param suspectDTO Refers to suspectDTO instance.
   * @param result Contains the result of the validation.
   * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
   * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
   * @return a string of the appropriate page.
   */
    @PostMapping("/suspect-registration")
    public String submitSuspectRegistrationForm(@Valid SuspectDTO suspectDTO, BindingResult result, Model model, Principal principal)
    {
        //Record to Audit Trail
        String eventDescription= "Registered person with PersonID="+suspectDTO.getPersonId()+" as a suspect in incident with incidentURN="+suspectDTO.getIncidentURN();
        recordEvent(Event.ADD, eventDescription, principal);

        if (result.hasErrors())
        {
            Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
            if (policeOptional.isPresent()) {
            Police policeUser = policeOptional.get();
            model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
        }
            return "registration/suspect_registration";
        }

        final Person person;
        final IncidentReport incidentReports;

            Optional<Person> personOptional = personRepo.findById(suspectDTO.getPersonId());
            Optional<IncidentReport> incidentOptional = incidentRepo.findById(suspectDTO.getIncidentURN());

            if ( personOptional.isPresent() && incidentOptional.isPresent() ) {
                person = personOptional.get();
                incidentReports = incidentOptional.get();
            } else {
                return "registration/suspect_registration";
            }

            // Add suspect to the incident
            incidentReports.getSuspects().add(person);

            // Add incident to the suspect
            person.getIncidentReports().add(incidentReports);

        incidentRepo.save(incidentReports);

        return "redirect:/home";
    }

    /**
   * Method that builds results from the database into strings
   *
   * @param list Refers to list instance of the security access levels
   * @return a string builder.
   */
    private String listToString(Collection<SecurityAccessLevel> list)
    {
      StringBuilder builder = new StringBuilder();
      builder.append("[");
                    
      list.forEach(level->{
        builder.append("\"");
        builder.append(level.toString());
        builder.append("\"");
        builder.append(",");
      });
      builder.deleteCharAt(builder.lastIndexOf(","));
      builder.append("]");

      return builder.toString();
    }

}


