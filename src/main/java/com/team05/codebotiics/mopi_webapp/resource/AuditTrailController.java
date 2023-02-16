package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.AuditTrail;
import com.team05.codebotiics.mopi_webapp.repository.AuditTrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;

/**
 * Controller for the audit logs
 */
@Controller
public class AuditTrailController {
    @Autowired
    AuditTrailRepository auditTrailRepo;

    /**
     * GET mapping for the audit trail page. Returns records from audit_logs table that match the current user's badgeNumber.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @GetMapping("/audit-trail")
    public String auditTrail(Model model, Principal principal){
        //Retrieve records from audit_logs table that match the current user's BadgeNumber
        ArrayList<AuditTrail> auditLogs= new ArrayList<>(auditTrailRepo.findByPoliceBadgeNumber(Integer.valueOf(principal.getName())));
        model.addAttribute("audit_logs", auditLogs);
        return "audit_logs";
    }
}
