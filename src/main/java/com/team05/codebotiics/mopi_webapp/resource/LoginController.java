package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.IncidentReport;
import com.team05.codebotiics.mopi_webapp.model.beans.Licenses;
import com.team05.codebotiics.mopi_webapp.model.beans.Person;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.model.enums.CrimeType;
import com.team05.codebotiics.mopi_webapp.model.enums.Role;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;
// import com.team05.codebotiics.mopi_webapp.model.enums.Sex;
import com.team05.codebotiics.mopi_webapp.repository.IncidentReportRepository;
import com.team05.codebotiics.mopi_webapp.repository.LicenseRepository;
import com.team05.codebotiics.mopi_webapp.repository.PersonRepository;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Intercepts incoming requests.
 * Converts the payload of POST requests to appropriate objects or variables.
 * Gets processed data from the Model layer (the table entity beans) and returns them for rendering.
 */
@Controller
public class LoginController {

    @Autowired
    private PoliceRepository policeRepo;

    @Autowired
    private IncidentReportRepository reportRepo;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private LicenseRepository licenseRepo;

    /**
     * POST Mapping for NRAC reviews on the Supervisor home page. Processes NRAC review action.
     *
     * @param incidentURN IncidentURN of IncidentReport in question
     * @param deletion Whether or not to delete the data
     * @param redirectAttributes Used to provide attribute to the view
     * @return
     */
    @PostMapping(value = "/nrac-submit")
    public String nracReview(@Param("incidentURN") Integer incidentURN, @Param("deletion") int deletion, RedirectAttributes redirectAttributes) {
        String msg = "Record will be retained";
        if (deletion == 1) {
            Long success = reportRepo.deleteByIncidentURN(incidentURN);
            if (success == 1) {
                msg = "Record and associated data will be deleted";
            }
        }
        redirectAttributes.addFlashAttribute("msg", msg);
        return "redirect:/home";
    }

    /**
     * GET mapping for the home pages. Controls authorisation for the home pages. Redirects the client to the appropriate home page.
     *
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposescontains security information about the client.
     * @return a string of the appropriate page.
     */
    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        Map<String, String> map = new HashMap<>();
        Optional<Police> policeOptional = policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        if (policeOptional.isPresent()) {
            Police police = policeOptional.get();
            map.put("badge_num", Integer.toString(police.getBadgeNumber()));
            map.put("firstName", police.getPerson().getFirstname());
            map.put("lastName", police.getPerson().getLastname());
            map.put("role", police.getRole().name());
            map.put("rank", police.getRank_l());
            map.put("securityAccessLevel", police.getSecurityAccessLevel().name());
            model.mergeAttributes(map);

            if (police.getRole().name().equals("USER")) {
                return "home/user_home";
            } else if (police.getRole().name().equals("MANAGER")) {
                return "home/manager_home";
            }
            if (police.getRole().name().equals("SUPERVISOR")) {
                //Generate automatic NRAC reviews
                LocalDateTime currentDateAndTime = LocalDateTime.now();
                LocalDateTime group_1Expiry = currentDateAndTime.minusYears(100);
                LocalDateTime group_2Expiry = currentDateAndTime.minusYears(10);
                LocalDateTime group_3Expiry = currentDateAndTime.minusYears(6);

                //Calculate the expiry dates for GROUP_1, GROUP_2 and GROUP_3 crimes
                Optional<Collection<IncidentReport>> optionalGroup_1IncidentReports = reportRepo.findByExpiryDate(group_1Expiry, CrimeType.GROUP_1);
                Optional<Collection<IncidentReport>> optionalGroup_2IncidentReports = reportRepo.findByExpiryDate(group_2Expiry, CrimeType.GROUP_2);
                Optional<Collection<IncidentReport>> optionalGroup_3IncidentReports = reportRepo.findByExpiryDate(group_3Expiry, CrimeType.GROUP_3);
                ArrayList<IncidentReport> incidentReports = new ArrayList<IncidentReport>();
                if (optionalGroup_1IncidentReports.isPresent()) {
                    incidentReports.addAll(optionalGroup_1IncidentReports.get());
                }
                if (optionalGroup_2IncidentReports.isPresent()) {
                    incidentReports.addAll(optionalGroup_2IncidentReports.get());
                }
                if (optionalGroup_3IncidentReports.isPresent()) {
                    incidentReports.addAll(optionalGroup_3IncidentReports.get());
                }
                model.addAttribute("incidentReports", incidentReports);
                return "home/supervisor_home";
            }
        }
        return "error";
    }

    /**
     * GET mapping for the custom login page.
     *
     * @return name of the custom login page.
     */
    @GetMapping(value = {"/", "/login"})
    public String login() {
        return "custom_login";
    }
}