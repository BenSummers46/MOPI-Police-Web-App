package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.AuditTrail;
import com.team05.codebotiics.mopi_webapp.model.beans.Evidence;
import com.team05.codebotiics.mopi_webapp.model.beans.IncidentReport;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import com.team05.codebotiics.mopi_webapp.repository.AuditTrailRepository;
import com.team05.codebotiics.mopi_webapp.repository.EvidenceRepository;
import com.team05.codebotiics.mopi_webapp.repository.IncidentReportRepository;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Controller for the Evidence page
 */
@Controller
public class EvidenceController {

    @Autowired
    EvidenceRepository evidenceRepo;

    @Autowired
    IncidentReportRepository incidentReportRepo;

    @Autowired
    AuditTrailRepository auditTrailRepo;

    @Autowired
    PoliceRepository policeRepo;

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

    @GetMapping("/evidence-upload")
    public String evidenceUpload(){return "report/evidence_page";}

    /**
     * POST Mapping for evidence_report page. Converts file to byte array and stores in the evidence table
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param file File received from client
     * @param incidentURN IncidentURN to assign the Evidence to
     * @param name Received from form
     * @param description Received from form
     * @param securityAccessLevel Received from form
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping("/evidence-upload")
    public String saveEvidence(Model model,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("incidentURN") int incidentURN,
                               @RequestParam("name") String name,
                               @RequestParam("description") String description,
                               @RequestParam("securityAccessLevel") String securityAccessLevel,
                               Principal principal) {
        //Record to Audit Trail
        String eventDescription= "Added new Evidence with Filename= "+ name;
        recordEvent(Event.ADD, eventDescription, principal);

        String msg="ERROR";
        try{
            //Convert file to byte array
            byte[] blob= file.getBytes();
            //Find Incident Report by given IncidentURN
            Optional<IncidentReport> optionalIncidentReport= incidentReportRepo.findByIncidentURN(incidentURN);
            if(optionalIncidentReport.isPresent()){
                IncidentReport incidentReport= optionalIncidentReport.get();
                Evidence evidence= new Evidence();
                //Create file extension
                String mimeType= file.getContentType();
                String fileExtension= "."+mimeType.substring(mimeType.indexOf("/")+1);

                evidence.setName(name+ fileExtension);
                evidence.setIncidentReport(incidentReport);
                evidence.setDescription(description);
                evidence.setEvidenceFile(blob);
                evidence.setSecurityAccessLevel(securityAccessLevel);
                evidenceRepo.save(evidence);
                msg= "Upload Successful";
            }
            else{
                msg= "Upload Failed: IncidentURN '"+incidentURN+"' doesn't exist";
            }
        }
        catch (IOException e){
            msg= "Upload Failed: Server-side file I/O error! Try again later.";
        }
        model.addAttribute("msg", msg);
        return "report/evidence_page";
    }
}
