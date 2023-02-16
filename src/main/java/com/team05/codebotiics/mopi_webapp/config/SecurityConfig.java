package com.team05.codebotiics.mopi_webapp.config;

import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;
import com.team05.codebotiics.mopi_webapp.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Enforces authentication and authorisation for the web app.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = PoliceRepository.class)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * @see com.team05.codebotiics.mopi_webapp.config.CustomAuthenticationSuccessHandler
     * @return an instance of {@link com.team05.codebotiics.mopi_webapp.config.CustomAuthenticationSuccessHandler}.
     */
    @Bean
    public AuthenticationSuccessHandler customeAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    /**
     *
     * @return an instance of {@link com.team05.codebotiics.mopi_webapp.service.LoginService}.
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new LoginService();
    }

    /**
     *
     * @return instance of BCryptPasswordEncoder
     * @see org.springframework.security.crypto.bcrypt
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    /**
     * Generates an instance of DaoAuthenticationProvider with the user detail service set to the one returned by
     * {@link SecurityConfig#userDetailsService()} and its password encoder set to the one returned by
     * {@link SecurityConfig#getPasswordEncoder()}.
     * @return instance of DaoAuthenticationProvider
     * @see org.springframework.security.authentication.dao
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return authenticationProvider;
    }

    /**
     * Sets authentication and authorisation for clients.
     * @param http
     * @throws Exception
     */
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/suspect/**").permitAll()
                .antMatchers("/home").hasAnyRole("MANAGER", "SUPERVISOR", "USER")//This is ok for this mapping because the Controller determines which home page the user is allowed to access
                .antMatchers("/audit-trail").hasAnyRole("MANAGER", "SUPERVISOR", "USER")
                .antMatchers("/evidence").hasAnyRole("MANAGER", "SUPERVISOR", "USER")
                .antMatchers("/incident_report").hasAnyRole("MANAGER", "SUPERVISOR", "USER")
                .antMatchers("/person").hasAnyRole("MANAGER", "SUPERVISOR", "USER")
                .antMatchers("/licenses_page").hasAnyRole("MANAGER", "SUPERVISOR", "USER")
                .antMatchers("/police-registration").hasRole("SUPERVISOR")
                .antMatchers("/police_search").hasAnyRole("MANAGER", "SUPERVISOR", "USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureUrl("/login?error")
                    .successHandler(customeAuthenticationSuccessHandler())
                    .permitAll()
                .and()
                .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        .and()
                        .csrf().disable().cors();
    }

    /**
     * Configures authentication. Sets authentication provider to the one returned by {@link SecurityConfig#authenticationProvider()}
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authenticationProvider());
    }

}
