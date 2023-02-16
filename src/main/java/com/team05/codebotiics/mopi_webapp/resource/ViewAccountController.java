/**
 * Created by: Ben Summers
 * Use: Fetch user information to be displayed on the account page.
 */

package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.Person;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.repository.PersonRepository;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class ViewAccountController {

    private final PoliceRepository policeRepo;

    public ViewAccountController(PoliceRepository policeRepo, PersonRepository personRepo) {
        this.policeRepo = policeRepo;
    }

    /**
     *
     * @param model
     * @param principal
     * @return view_account.html
     */
    @GetMapping("/view-accounts")
    public String viewAccount(Model model, Principal principal){

        Optional<Police> policeOptional= policeRepo.findByBadgeNumber(Integer.valueOf(principal.getName()));
        if (policeOptional.isPresent()) {
            Police policeUser = policeOptional.get();
            model.addAttribute("police", policeUser);

        }

        return "accounts/view_account";
    }
}
