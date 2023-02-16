package com.team05.codebotiics.mopi_webapp.service;

import com.team05.codebotiics.mopi_webapp.config.SecurityConfig;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.model.PoliceDetails;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Used in {@link SecurityConfig#userDetailsService()}. Spring Security uses this to verify the users credentials.
 */
@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private PoliceRepository policeRepository;

    /**
     * Used by Spring Security to search for a user <i>(a {@link Police} officer)</i> with the badge number entered by the client at the login page.
     * @param badgeNumber
     * @return instance of {@link PoliceDetails} which extends {@link Police} and contains methods relevant to a user like {@link PoliceDetails#getPassword()}.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String badgeNumber) throws UsernameNotFoundException {
        Optional<Police> police= policeRepository.findByBadgeNumber(Integer.valueOf(badgeNumber));
        police
                .orElseThrow(() -> new UsernameNotFoundException("Badge Number not found"));
        return police
                .map(PoliceDetails::new).get();
    }
}
