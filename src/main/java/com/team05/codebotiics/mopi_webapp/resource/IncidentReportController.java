package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.AuditTrail;
import com.team05.codebotiics.mopi_webapp.model.beans.IncidentReport;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import com.team05.codebotiics.mopi_webapp.repository.AuditTrailRepository;
import com.team05.codebotiics.mopi_webapp.repository.IncidentReportRepository;
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
public class IncidentReportController {

    private final IncidentReportRepository incidentRepo;
    private final PoliceRepository policeRepo;

    public IncidentReportController(PoliceRepository policeRepo, IncidentReportRepository incidentRepo)
    {
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
     * GET mapping for incident report form page.
     *
     * @param incidentReport Refers to incident instance.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @GetMapping("/incident-report")
    public String getIncidentReportForm(IncidentReport incidentReport, Model model, Principal principal)      
{  
        Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        if (policeOptional.isPresent()) {
            Police policeUser = policeOptional.get();
            model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
        }
        return "report/incident_report";
    }

    /**
     * POST mapping for incident report form page. Redirects the user back to home page when succesful.
     *
     * @param incidentReport Refers to incident instance.
     * @param result Contains the result of the validation.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @PostMapping("/incident-report")
    public String submitIncidentReportForm(@Valid IncidentReport incidentReport, BindingResult result, Model model, Principal principal)
    {
        //Record to Audit Trail
        String eventDescription= "Added new Incident Report with IncidentURN="+ incidentReport.getIncidentURN();
        recordEvent(Event.ADD, eventDescription, principal);

        if (result.hasErrors())
        {
            Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
            if (policeOptional.isPresent()) {
                Police policeUser = policeOptional.get();
                model.addAttribute("badge_num", Integer.toString(policeUser.getBadgeNumber()));
            }
            return "report/incident_report";
        }

        Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        Police policeUser = policeOptional.get();
        incidentReport.setPolice(policeUser);
        incidentReport.setBadgeNumber(policeUser.getBadgeNumber());
            
        incidentRepo.save(incidentReport);

        return "redirect:/home";
    } 
}

