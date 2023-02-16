package com.team05.codebotiics.mopi_webapp;

import com.team05.codebotiics.mopi_webapp.model.beans.IncidentReport;
import com.team05.codebotiics.mopi_webapp.model.beans.Licenses;
import com.team05.codebotiics.mopi_webapp.model.beans.Person;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.model.enums.CrimeType;
import com.team05.codebotiics.mopi_webapp.model.enums.Role;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;
import com.team05.codebotiics.mopi_webapp.model.enums.Sex;
import com.team05.codebotiics.mopi_webapp.repository.IncidentReportRepository;
import com.team05.codebotiics.mopi_webapp.repository.LicenseRepository;
import com.team05.codebotiics.mopi_webapp.repository.PersonRepository;
import com.team05.codebotiics.mopi_webapp.repository.PoliceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class PoliceRepositoryTests {
    @Autowired
    private PoliceRepository repo;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private IncidentReportRepository reportRepo;

    @Autowired
    private LicenseRepository licenseRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        //PERSON 1
        Person person= new Person();
        Police police= new Police();
        police.setBadgeNumber(1);
        police.setRank_l("DI");
        police.setRole(Role.MANAGER);
        police.setSecurityAccessLevel(SecurityAccessLevel.TOP_SECRET);

        //We must encode the password
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
        String rawPassword= "Password1@";
        String encodedPassword= encoder.encode(rawPassword);
        police.setPasswordHash(encodedPassword);
        police.setConfirmPassword(encodedPassword);


        person.setFirstname("Mark");
        //person.setAddressPerson("FakeAddress");
        person.setAddressLine1("test line1");
        person.setAddressLine2("test line2");
        person.setCountry("UK");
        person.setCounty("Stockton on Tees");
        person.setEthnicOrigin("White");
        person.setPostCode("test");
        person.setCity("City");
        person.setTown("Yaxley");
        person.setAddressPerson();
        person.setHeight(175);
        person.setWeight(65);
        person.setLastname("Hill");
        person.setDateOfBirth(new Date(975456000));
        person.setSex(Sex.MALE.getDisplayValue());
        police.setPerson(person);

        //=======================================================================
        //PERSON 2
        Person person2= new Person();
        Police police2= new Police();
        police2.setBadgeNumber(2);
        police2.setRank_l("Officer");
        police2.setRole(Role.SUPERVISOR);
        police2.setSecurityAccessLevel(SecurityAccessLevel.OFFICIAL);

        //We must encode the password
        BCryptPasswordEncoder encoder2= new BCryptPasswordEncoder();
        String rawPassword2= "Password2@";
        String encodedPassword2= encoder2.encode(rawPassword2);
        police2.setPasswordHash(encodedPassword2);
        police2.setConfirmPassword(encodedPassword2);


        person2.setFirstname("Joe");
        //person2.setAddressPerson("test line1");
        person2.setAddressLine1("test line1");
        person2.setAddressLine2("test line2");
        person2.setCountry("UK");
        person2.setCounty("Stockton on Tees");
        person2.setEthnicOrigin("Black");
        person2.setPostCode("test");
        person2.setTown("Yaxley");
        person2.setCity("city");
        person2.setAddressPerson();
        person2.setHeight(199);
        person2.setWeight(90);
        person2.setLastname("Bloggs");
        person2.setDateOfBirth(new Date(975456000));
        person2.setSex(Sex.FEMALE.getDisplayValue());

        police2.setPerson(person2);
        //===============================================================
        Person savedPerson= personRepo.save(person);
        Police savedUser= repo.save(police);

        Person savedPerson2= personRepo.save(person2);
        Police savedUser2= repo.save(police2);

        Police existUser= entityManager.find(Police.class, savedUser.getBadgeNumber());
        System.out.println(existUser.getPasswordHash());
        System.out.println(police.getPasswordHash());
        assert(existUser.getPasswordHash().equals(police.getPasswordHash()));
    }
    @Test
    public void testCreateSuspect(){
        LocalDateTime testTime = LocalDateTime.now();

        IncidentReport crime1= new IncidentReport();
        crime1.setBadgeNumber(1);
        crime1.setCrimeType(CrimeType.GROUP_1);
        crime1.setDescription("First Crime");
        crime1.setSecurityAccessLevel(SecurityAccessLevel.OFFICIAL);
        crime1.setDateAndTime(testTime);
        //crime1.setLocation("test");
        IncidentReport crime2= new IncidentReport();
        crime2.setBadgeNumber(1);
        crime2.setCrimeType(CrimeType.GROUP_1);
        crime2.setDescription("Second Crime");
        crime2.setDateAndTime(testTime);
        //crime2.setLocation("test");
        crime2.setSecurityAccessLevel(SecurityAccessLevel.OFFICIAL);

        HashSet<IncidentReport> incidents= new HashSet<IncidentReport>();
        incidents.add(crime1);
        incidents.add(crime2);

        Person suspect= new Person();
        suspect.setFirstname("Mark");
        suspect.setMiddleName("John");
        suspect.setHeight(175);
        suspect.setWeight(65);
        suspect.setAddressLine1("test line1");
        suspect.setAddressLine2("test line2");
        suspect.setCountry("UK");
        suspect.setCounty("Stockton on Tees");
        suspect.setEthnicOrigin("Black");
        suspect.setPostCode("test");
        suspect.setTown("Yaxley");
        suspect.setCity("city");
        suspect.setAddressPerson();
        suspect.setLastname("Hill");
        suspect.setDateOfBirth(new Date(975456000));
        suspect.setSex(Sex.MALE.getDisplayValue());
        suspect.setIncidentReports(incidents);

        HashSet<Person> suspects= new HashSet<Person>();
        suspects.add(suspect);

        crime1.setSuspects(suspects);
        crime2.setSuspects(suspects);

        reportRepo.save(crime1);
        reportRepo.save(crime2);

        personRepo.save(suspect);
    }
    @Test
    public void testAddLicenses(){
        Licenses license= new Licenses();
        license.setLicenseType("TV License");
        Licenses license2= new Licenses();
        license2.setLicenseType("Driving License");
        Licenses license3= new Licenses();
        license3.setLicenseType("Gun License");
        Licenses license4= new Licenses();
        license4.setLicenseType("Forklift License");



        Collection<Licenses> licenses= new HashSet<>();
        licenses.add(license);
        licenses.add(license2);

        Person personWithLicense= new Person();
        personWithLicense.setFirstname("Luke");
        personWithLicense.setLastname("LicenseMan");
        personWithLicense.setHeight(175);
        personWithLicense.setWeight(65);
        personWithLicense.setAddressLine1("test line1");
        personWithLicense.setAddressLine2("test line2");
        personWithLicense.setCountry("UK");
        personWithLicense.setCounty("Stockton on Tees");
        personWithLicense.setEthnicOrigin("Black");
        personWithLicense.setPostCode("test");
        personWithLicense.setTown("Yaxley");
        personWithLicense.setCity("city");
        personWithLicense.setAddressPerson();
        personWithLicense.setLastname("Hill");
        personWithLicense.setDateOfBirth(new Date(975456000));
        personWithLicense.setSex(Sex.MALE.getDisplayValue());

        licenseRepo.save(license);
        licenseRepo.save(license2);
        licenseRepo.save(license3);
        licenseRepo.save(license4);
        personWithLicense.setLicenses(licenses);
        personRepo.save(personWithLicense);
    }
}

