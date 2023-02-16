package com.team05.codebotiics.mopi_webapp.model;

import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Holds user <i>{@link Police}</i> details.
 */
public class PoliceDetails extends Police implements UserDetails {

    public PoliceDetails(final Police police) {
        super(police);
    }

    /**
     * Grants the user with their role from {@link Police#getRole()}.
     * @return grantedAuthorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+getRole().name()));
        return grantedAuthorities;
    }

    /**
     * @return passwordHash the password hash.
     */
    @Override
    public String getPassword() {
        return super.getPasswordHash();
    }

    /**
     * @return the username <i>(the officer's badge number)</i>
     */
    @Override
    public String getUsername() {

        return Integer.toString(super.getBadgeNumber());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
