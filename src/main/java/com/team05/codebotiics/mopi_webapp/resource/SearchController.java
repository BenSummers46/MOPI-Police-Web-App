package com.team05.codebotiics.mopi_webapp.resource;

import com.team05.codebotiics.mopi_webapp.model.beans.*;
import com.team05.codebotiics.mopi_webapp.model.enums.Event;
import com.team05.codebotiics.mopi_webapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller for the search pages
 * Intercepts incoming requests.
 * Converts the payload of POST requests to appropriate objects or variables.
 * Gets processed data from the Model layer (the table entity beans) and returns them for rendering.
 */
@Controller
public class SearchController {

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    PoliceRepository policeRepo;

    @Autowired
    LicenseRepository licenseRepo;

    @Autowired
    IncidentReportRepository reportRepo;

    @Autowired
    EvidenceRepository evidenceRepo;

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

    //==================================================================================================================
    //Incident Report Search
    @GetMapping("/incident-search")
    public String incidentSearch(){
        return "search/incident_search";
    }

    /**
     * POST Mapping for the download file buttons in the IncidentReport search results. Finds the Evidence instance,
     * converts the byte array back to a file and sends it to the client
     * @param evidenceID Used to identify the Evidence record to retrieve
     * @return
     * @throws IOException
     */
    @PostMapping(value="/download-file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    ResponseEntity<Resource> downloadFile(@RequestParam("downloadEvidenceID") Integer evidenceID) throws IOException{
        Optional<Evidence> optionalEvidence= evidenceRepo.findByEvidenceId(evidenceID);
        if(optionalEvidence.isPresent()){
            Evidence evidence= optionalEvidence.get();
            ByteArrayResource resource= new ByteArrayResource(evidence.getEvidenceFile());
            //Set filename
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+evidence.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        else{
            return null;
        }
    }

    /**
     * POST Mapping for the IncidentReport search by location. Finds IncidentReport records that lie within the provided
     * bounding box coordinates and sends them back to the client.
     * @param boundingBoxMinLatitudeString Latitude of the Southern side of the bounding box
     * @param boundingBoxMaxLatitudeString Latitude of the Northern side of the bounding box
     * @param boundingBoxMinLongitudeString Longitude of the Western side of the bounding box
     * @param boundingBoxMaxLongitudeString Longitude of the Eastern side of the bounding box
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value= "/incidentReportSearchByLocation")
    public String incidentReportSearchByLocation(
            @RequestParam("boundingBoxMinLatitude") String boundingBoxMinLatitudeString,
            @RequestParam("boundingBoxMaxLatitude") String boundingBoxMaxLatitudeString,
            @RequestParam("boundingBoxMinLongitude") String boundingBoxMinLongitudeString,
            @RequestParam("boundingBoxMaxLongitude") String boundingBoxMaxLongitudeString,
            Model model,
            Principal principal
    ){
        //Record to Audit Trail
        String eventDescription= "Searched Incident Report table for incidents within: "
                +boundingBoxMinLatitudeString+" to "+ boundingBoxMaxLatitudeString+" Latitude, "
                +boundingBoxMinLongitudeString+" to "+ boundingBoxMaxLongitudeString+" Longitude.";
        recordEvent(Event.SEARCH, eventDescription, principal);

        double boundingBoxMinLatitude= Double.parseDouble(boundingBoxMinLatitudeString);
        double boundingBoxMaxLatitude= Double.parseDouble(boundingBoxMaxLatitudeString);
        double boundingBoxMinLongitude= Double.parseDouble(boundingBoxMinLongitudeString);
        double boundingBoxMaxLongitude= Double.parseDouble(boundingBoxMaxLongitudeString);
        Optional<Collection<IncidentReport>> optionalIncidentReportCollection= reportRepo.findByBoundingBox(boundingBoxMinLatitude, boundingBoxMaxLatitude, boundingBoxMinLongitude, boundingBoxMaxLongitude);
        if(optionalIncidentReportCollection.isPresent()){
            ArrayList<IncidentReport> reportList= (ArrayList) optionalIncidentReportCollection.get();
            model.addAttribute("reportList", reportList);
        }
        else{
            String msg= "No Results";
            model.addAttribute("msg", msg);
        }
        return "search/incident_search";
    }

    /**
     * POST Mapping for the IncidentReport search by date and time. Finds IncidentReport records that lie within the provided
     * dates and sends them back to the client.
     * @param from Older date
     * @param to Later date
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/incidentReportSearchByDateAndTime")
    public String incidentReportSearchByDateAndTime(
            @RequestParam("dateAndTimeFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("dateAndTimeTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Model model,
            Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched Incident Report table for dates between '"+from+"' and '"+to+"'";
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<Collection<IncidentReport>> optionalIncidentReportCollection= reportRepo.findByDateRange(from, to);
        if(optionalIncidentReportCollection.isPresent()){
            ArrayList<IncidentReport> reportList= (ArrayList) optionalIncidentReportCollection.get();
            model.addAttribute("reportList", reportList);
        }
        else{
            String msg= "No Results";
            model.addAttribute("msg", msg);
        }
        return "search/incident_search";
    }

    /**
     * POST Mapping for the IncidentReport search by IncidentURN. Finds the IncidentReport record that with the provided
     * Incident URN and sends it back to the client.
     * @param incidentURN IncidentURN to search by
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/incidentReportSearchByIncidentURN")
    public String incidentReportSearchByIncidentURN(@Param("incidentURN") int incidentURN, Model model, Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched Incident Report table for Incident URN: "+incidentURN;
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<IncidentReport> optionalIncidentReport= reportRepo.findByIncidentURN(incidentURN);
        if(optionalIncidentReport.isPresent()){
            ArrayList<IncidentReport> reportList= new ArrayList<>();
            reportList.add(optionalIncidentReport.get());
            model.addAttribute("reportList", reportList);
        }
        else{
            String msg= "No Results";
            model.addAttribute("msg", msg);
        }
        return "search/incident_search";
    }

    /**
     * POST Mapping for the IncidentReport search by BadgeNumber. Finds the IncidentReport records that match the given
     * BadgeNumber and sends it back to the client.
     * @param badgeNumber BadgeNumber to search by
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/incidentReportSearchByBadgeNumber")
    public String incidentReportSearchByBadgeNumber(@Param("badgeNumber") int badgeNumber, Model model, Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched Incident Report table for Badge Number: "+badgeNumber;
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<Collection<IncidentReport>> optionalIncidentReportCollection= reportRepo.findByBadgeNumber(badgeNumber);
        if(optionalIncidentReportCollection.isPresent()){
            ArrayList<IncidentReport> reportList=(ArrayList) optionalIncidentReportCollection.get();
            model.addAttribute("reportList", reportList);
        }
        else{
            String msg= "No Results";
            model.addAttribute("msg", msg);
        }
        return "search/incident_search";
    }

    //==================================================================================================================
    //Suspect Search
    @GetMapping("/suspect-search")
    public String suspectSearch(){
        return "search/suspect_search";
    }

    /**
     * POST Mapping for removing Suspects from an IncidentReport from the suspect_search page.
     * @param personId Used to identify the suspect
     * @param allParams Used to retrieve the incidents the suspect is a suspect in
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping("/modifySuspect")
    public String modifySuspect(@Param("personId") int personId, @RequestParam Map<String, String> allParams, Principal principal){
        List<Person> suspects= new ArrayList<>();

        List<IncidentReport> allIncidentReports= reportRepo.findAll();
        Optional<Person> optionalSuspect= personRepo.findById(personId);
        if(optionalSuspect.isPresent()){
            //Remove the suspect from all incidents first
            for (IncidentReport report: allIncidentReports){
                for (Person person: report.getSuspects()){
                    report.getSuspects().remove(optionalSuspect.get());
                }
            }
            reportRepo.saveAll(allIncidentReports);

            //Add the suspect to the incidents from the form
            for (String key: allParams.keySet()){
                if (key.contains("individualincident")){
                    Optional<IncidentReport> optionalIncidentReport= reportRepo.findByIncidentURN(Integer.valueOf(allParams.get(key)));
                    IncidentReport incidentReport= optionalIncidentReport.get();
                    suspects= new ArrayList<>(incidentReport.getSuspects());//Pre-existing suspects in the report
                    Person suspect= optionalSuspect.get();
                    suspects.add(suspect);//Add current suspect to the list of pre-existing suspects in the report
                    incidentReport.setSuspects(new HashSet<Person>(suspects));
                    reportRepo.save(incidentReport);

                    //Record to Audit Trail
                    String eventDescription= "Updated suspect with PersonID= "+suspect.getPersonId()+" in Suspect Link table";
                    recordEvent(Event.UPDATE, eventDescription, principal);
                }
            }
        }
        return "search/suspect_search";
    }

    /**
     * POST Mapping for Suspect search by first name.
     * @param firstName Used to identify the suspect
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/suspectSearchByFirstName")
    public String suspectSearchByFirstName(@Param("firstName") String firstName, Model model, Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched Person table for suspect with First name: "+firstName;
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<Collection<Person>> optionalPersonCollection= personRepo.findBySuspectFirstName(firstName);
        if(optionalPersonCollection.isPresent()){
            ArrayList<Person> suspectList=(ArrayList) optionalPersonCollection.get();
            model.addAttribute("suspectList", suspectList);
        }
        else{
            String msg= "No results";
            model.addAttribute("msg", msg);
        }
        return "search/suspect_search";
    }

    //==================================================================================================================
    //License Search
    @GetMapping("/license-search")
    public String licenseSearch(){
        return "search/license_search";
    }

    /**
     * POST Mapping for modifying a License.
     * @param action Whether the user wishes to DELETE or UPDATE
     * @param license License in question
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value= "/modifyLicense")
    public String modifyLicense(@Param("action") int action, Licenses license, Model model, Principal principal){
        if (action==0){//UPDATE
            //Record to Audit Trail
            String eventDescription= "Updated license with LicenseID= "+license.getLicenseId()+" in License table";
            recordEvent(Event.UPDATE, eventDescription, principal);

            licenseRepo.save(license);
        }
        else if (action==1){//DELETE
            //Record to Audit Trail
            String eventDescription= "Deleted license with LicenseID= "+license.getLicenseId()+" in License table";
            recordEvent(Event.DELETE, eventDescription, principal);

            //Iterates through all persons and removes any instance of this license type.
            ArrayList<Person> personList= new ArrayList<>(personRepo.findAll());
            for (Person person:personList){
                if (person.getLicense().contains(license)){
                    person.getLicense().remove(license);
                }
            }
            personRepo.saveAll(personList);
            licenseRepo.deleteById(license.getLicenseId());
        }
        return "search/license_search";
    }

    /**
     * POST Mapping for License search by LicenseID.
     * @param licenseID Used to identify the License.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/licenseSearchByLicenseID")
    public String licenseSearchByLicenseID(@Param("licenseID") int licenseID, Model model, Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched License table for License ID: "+licenseID;
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<Licenses> optionalLicense= licenseRepo.findByLicenseId(licenseID);
        ArrayList<Licenses> licenseList= new ArrayList<Licenses>();
        if (optionalLicense.isPresent()){
            Licenses license= optionalLicense.get();
            licenseList.add(license);
            model.addAttribute("licenseList", licenseList);
        }
        else{
            String msg= "No results";
            model.addAttribute("msg", msg);
        }
        return "search/license_search";
    }


    //==================================================================================================================
    //Person Search
    private ArrayList<String> addressSeperator(String address){
        ArrayList<String> addressLines = new ArrayList<>();
        if (address!=null) {
            addressLines = new ArrayList<>(Arrays.asList(address.split(",")));
        }
        else {
            addressLines= new ArrayList<>();
            for (int i=0;i<8;i++){
                addressLines.add("N/A");
            }
        }
        return addressLines;
    }

    @GetMapping("/person-search")
    public String personSearch(){
        return "search/person_search";
    }

    /**
     * POST Mapping for modifying a Person.
     * @param action Whether the user wishes to DELETE or UPDATE
     * @param dob Date of Birth
     * @param addressLine1 Address Line 1
     * @param addressLine2 Address Line 2
     * @param city City
     * @param county County
     * @param country Country
     * @param postcode Postcode
     * @param allParams Used to retrieve the License the person owns
     * @param person Person in question
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping("/modifyPerson")
    public String modifyPerson(@Valid
                               @Param("action") int action,
                               @RequestParam("dateOfBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dob,
                               @Param("addressLine1") String addressLine1,
                               @Param("addressLine2") String addressLine2,
                               @Param("city") String city,
                               @Param("county") String county,
                               @Param("country") String country,
                               @Param("postcode") String postcode,
                               @RequestParam Map<String, String> allParams,
                               Person person, Model model, Principal principal){
        if (action==0){//UPDATE
            //Record to Audit Trail
            String eventDescription= "Updated person with PersonID= "+person.getPersonId()+" in Person table";
            recordEvent(Event.UPDATE, eventDescription, principal);

            //Create a HashSet of Licenses and assign them to the Person (JPA automatically handles the link table)
            HashSet<Licenses> licenses= new HashSet<>();
            for (String key: allParams.keySet()){
                if (key.contains("individualLicense")){
                    Optional<Licenses> license= licenseRepo.findByLicenseType(allParams.get(key));
                    licenses.add(license.get());
                }
            }
            applyAddressToPerson(dob, addressLine1, addressLine2, city, county, country, postcode, person);
            person.setLicenses(licenses);
            personRepo.save(person);

        }
        else if (action==1){//DELETE
            //Record to Audit Trail
            String eventDescription= "Deleted person with PersonID= "+person.getPersonId()+" in Person table";
            recordEvent(Event.DELETE, eventDescription, principal);

            personRepo.deleteById(person.getPersonId());
        }
        return "search/person_search";
    }

    /**
     * POST Mapping for Person search by first name.
     * @param firstName Used to identify the Person.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/personSearchByFirstName")
    public String personSearchByFirstName(@Valid @Param("firstName") String firstName, Model model, Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched Person table for First name: "+firstName;
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<Collection<Person>> optionalPersonCollection= personRepo.findByFirstName(firstName);
        if(optionalPersonCollection.isPresent()){
            HashMap<Integer, ArrayList<String>> personAddresses= new HashMap<>();
            HashMap<Integer, Boolean> policeList= new HashMap<>(); //used in the front end to identify which Persons are also police
            ArrayList<Person> personList=(ArrayList) optionalPersonCollection.get();
            personSearchResults(model, personList, personAddresses, policeList);
        }
        else{
            String msg= "No Results";
            model.addAttribute("msg", msg);
        }
        return "search/person_search";
    }

    /**
     * POST Mapping for Person search by last name.
     * @param lastName Used to identify the Person.
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/personSearchByLastName")
    public String personSearchByLastName(@Valid @Param("lastName")String lastName, Model model, Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched Person table for Last name: "+lastName;
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<Collection<Person>> optionalPersonCollection= personRepo.findByLastname(lastName);
        if(optionalPersonCollection.isPresent()){
            ArrayList<Person> personList=(ArrayList) optionalPersonCollection.get();
            HashMap<Integer, ArrayList<String>> personAddresses= new HashMap<>();
            HashMap<Integer, Boolean> policeList= new HashMap<>(); //used in the front end to identify which Persons are also police
            personSearchResults(model, personList, personAddresses, policeList);
        }
        else{
            String msg= "No Results";
            model.addAttribute("msg", msg);
        }
        return "search/person_search";
    }

    /**
     * Method used by the Person search mappings for:
     * <p><ul>
     *     <li>Adding all license types to the model
     *     <li>Splitting the address string for each Person in the results
     *         into an ArrayList and adding it to the model
     *     <li>flags Persons that are also Police by using a policeList which is
     *          used to prevent the user from deleting them on the person_search page.
     * </ul></p>
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param personList List of Persons in the results
     * @param personAddresses Empty Hashmap to add addressLines to
     * @param policeList List of Persons that are also Police
     */
    private void personSearchResults(Model model, ArrayList<Person> personList, HashMap<Integer, ArrayList<String>> personAddresses, HashMap<Integer, Boolean> policeList) {
        ArrayList<Licenses> licenseList= new ArrayList<>(licenseRepo.findAll());
        for (Person person: personList){
            if (person.getAddressPerson()!=null){
                ArrayList<String> addressLines= addressSeperator(person.getAddressPerson());
                personAddresses.put(person.getPersonId(), addressLines);
            }

            if (policeRepo.findByPersonId(person.getPersonId()).isPresent()){
                policeList.put(person.getPersonId(), true);
            }
            else{
                policeList.put(person.getPersonId(), false);
            }
        }
        model.addAttribute("licenseList", licenseList);
        model.addAttribute("policeList", policeList);
        model.addAttribute("personAddresses", personAddresses);
        model.addAttribute("personList", personList);
    }

    //==================================================================================================================
    //Police Search
    @GetMapping("/police-search")
    public String policeSearch(){
        return "search/police_search";
    }


    /**
     * POST Mapping for modifying a Police.
     * @param action Whether the user wishes to DELETE or UPDATE
     * @param dob Date of Birth
     * @param addressLine1 Address Line 1
     * @param addressLine2 Address Line 2
     * @param city City
     * @param county County
     * @param country Country
     * @param postcode Postcode
     * @param police The Police in question
     * @param person The Person object of Police
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping("/modifyPolice")
    public String modifyPolice(@Valid
                                   @Param("action") int action,
                               @RequestParam("dateOfBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dob,
                               @Param("addressLine1") String addressLine1,
                               @Param("addressLine2") String addressLine2,
                               @Param("city") String city,
                               @Param("county") String county,
                               @Param("country") String country,
                               @Param("postcode") String postcode,
                               Police police, Person person, Model model, Principal principal){
        if (action==0){//UPDATE
            //Record to Audit Trail
            String eventDescription= "Updated police with Badge Number= "+police.getBadgeNumber()+" in Police table";
            recordEvent(Event.UPDATE, eventDescription, principal);

            applyAddressToPerson(dob, addressLine1, addressLine2, city, county, country, postcode, person);
            police.setPerson(person);
            personRepo.save(person);
            //Custom update queries must be performed to stop the Police's passwordHash being overwritten
            policeRepo.updateRankByBadgeNumber(police.getBadgeNumber(), police.getRank_l());
            policeRepo.updateRoleByBadgeNumber(police.getBadgeNumber(), police.getRole());
            policeRepo.updateSecurityAccessLevelByBadgeNumber(police.getBadgeNumber(), police.getSecurityAccessLevel());
        }
        else if (action==1){//DELETE
            //Record to Audit Trail
            String eventDescription= "Deleted police with Badge Number= "+police.getBadgeNumber()+" in Police table";
            recordEvent(Event.DELETE, eventDescription, principal);

            policeRepo.deleteById(police.getBadgeNumber());
        }
        return "search/police_search";
    }

    /**
     * Method that takes address lines and applies them to the Person.
     * @param dob Date of Birth
     * @param addressLine1 Address Line 1
     * @param addressLine2 Address Line 2
     * @param city City
     * @param county County
     * @param country Country
     * @param postcode Postcode
     * @param person Person in question
     */
    private void applyAddressToPerson(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("dateOfBirth") Date dob, @Param("addressLine1") String addressLine1, @Param("addressLine2") String addressLine2, @Param("city") String city, @Param("county") String county, @Param("country") String country, @Param("postcode") String postcode, Person person) {
        person.setAddressLine1(addressLine1);
        person.setAddressLine2(addressLine2);
        person.setCity(city);
        person.setCounty(county);
        person.setCountry(country);
        person.setPostCode(postcode);
        person.setAddressPerson();
        person.setDateOfBirth(dob);
    }

    /**
     * POST Mapping for Police search by first name.
     * @param firstName Used to identify the Police
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     * @throws ParseException
     */
    @PostMapping(value="/policeSearchByName")
    public String policeSearchByName(@Valid @Param("firstName") String firstName, Model model, Principal principal) throws ParseException {
        //Record to Audit Trail
        String eventDescription= "Searched Police table for First name: "+firstName;
        recordEvent(Event.SEARCH, eventDescription, principal);

        ArrayList<Police> policeList= new ArrayList<Police>();
        HashMap<Integer, Person> personMap= new HashMap<Integer, Person>();
        //Retrieve a list of Persons with that firstname
        Optional<Collection<Person>> optionalPersonCollection= personRepo.findByFirstName(firstName);
        if (optionalPersonCollection.isPresent()){
            ArrayList<Person> personList= (ArrayList) optionalPersonCollection.get();
            HashMap<Integer, ArrayList<String>> personAddresses= new HashMap<>();
            for (Person person : personList) {
                Integer personId= person.getPersonId();
                //Check if this Person is a Police
                Optional<Police> optionalPolice= policeRepo.findByPersonId(personId);
                if (optionalPolice.isPresent()){
                    ArrayList<String> addressLines= addressSeperator(person.getAddressPerson());
                    personAddresses.put(person.getPersonId(), addressLines);
                    policeList.add(optionalPolice.get());
                    personMap.put(personId, person);
                }
                else{
                    String msg= "No results";
                    model.addAttribute("msg", msg);
                }
            }
            model.addAttribute("personAddresses", personAddresses);
            model.addAttribute("policeList", policeList);
            model.addAttribute("personMap", personMap);
        }
        return "search/police_search";
    }

    /**
     * POST Mapping for Police search by BadgeNumber.
     * @param badgeNumber Used to identify the Police
     * @param model Attributes are applied to this which are used by thymeleaf to add dynamic content to the templates.
     * @param principal Contains security information about the client. Used for identification purposes
     * @return
     */
    @PostMapping(value="/policeSearchByBadgeNumber")
    public String policeSearchByBadgeNumber(@Param("badgeNumber") int badgeNumber, Model model, Principal principal){
        //Record to Audit Trail
        String eventDescription= "Searched Police table for BadgeNumber: "+badgeNumber;
        recordEvent(Event.SEARCH, eventDescription, principal);

        Optional<Police> optionalPolice= policeRepo.findByBadgeNumber(badgeNumber);
        ArrayList<Police> policeList= new ArrayList<Police>();
        HashMap<Integer, Person> personMap= new HashMap<Integer, Person>();
        HashMap<Integer, ArrayList<String>> personAddresses= new HashMap<>();
        if (optionalPolice.isPresent()){
            Police police= optionalPolice.get();
            policeList.add(police);
            Person person= police.getPerson();

            ArrayList<String> addressLines= addressSeperator(person.getAddressPerson());
            personAddresses.put(person.getPersonId(), addressLines);

            Integer personId= person.getPersonId();
            personMap.put(personId, person);
            model.addAttribute("personAddresses", personAddresses);
            model.addAttribute("policeList", policeList);
            model.addAttribute("personMap", personMap);
        }
        else{
            String msg= "No results";
            model.addAttribute("msg", msg);
        }
        return "search/police_search";
    }
}
