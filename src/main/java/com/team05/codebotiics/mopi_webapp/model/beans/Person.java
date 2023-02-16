package com.team05.codebotiics.mopi_webapp.model.beans;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the "Person" table.
 */
@Entity
@Table(name="Person")
public class Person {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "Person_ID")
    private int personId;

    @Basic
    @Column(name = "First_name")
    @NotBlank(message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z\\s]*", message = "Field to contain alphabetical characters only")
    private String firstname;

    @Basic
    @Column(name = "Middle_name")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z\\s]*", message = "Field to contain alphabetical characters only")
    private String middleName;

    @Basic
    @Column(name = "Last_name")
    @NotBlank(message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z\\s]*", message = "Field to contain alphabetical characters only")
    private String lastname;

    @Basic
    @Column(name = "Address_person")
    private String addressPerson;

    @Basic
    @Column(name = "Alias")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z\\s]*", message = "Field to contain alphabetical characters only")
    private String alias;

    @Basic
    @Column(name = "Date_Of_Birth")
    @NotNull( message = "Please enter birth date")
    @Past(message="Birth date should be less than current date")
    @DateTimeFormat( pattern="yyyy-MM-dd")
    private Date dateOfBirth;

    @Basic
    @Column(name = "Sex")
    @NotBlank( message = "No option has been selected")
    private String sex;

    @Basic
    @Column(name = "Ethnic_Origin")
    @NotBlank(message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z\\s]*", message = "Field to contain alphabetical characters only")
    private String ethnicOrigin;

    @Basic
    @Column(name = "Height")
    @NotNull(message = "Field cannot be empty")
    @Min(value = 0, message = "Field cannot be less than 0 ${value < 0 ? '' : ''}")
    @Max(value = 300, message = "Field cannot be greater than 300 ${value > 8 ? '8' : ''}cm")
    private Integer height;

    @Basic
    @Column(name = "Weight")
    @NotNull(message = "Field cannot be empty")
    @Min(value = 0, message = "Field cannot be less than 0 ${value < 0 ? '' : ''}")
    @Max(value = 150, message = "Field cannot be greater than 150 ${value > 220 ? '220' : ''}kgs")
    private Integer weight;

    public Set<IncidentReport> getIncidentReports() {
        return incidentReports;
    }

    public void setIncidentReports(Set<IncidentReport> incidentReports) {
        this.incidentReports = incidentReports;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL, mappedBy = "suspects")
    private Set<IncidentReport> incidentReports= new HashSet<>();

    @ManyToMany
    @JoinTable(name="License_Link_Table",
            joinColumns = {@JoinColumn(name="Person_ID", referencedColumnName = "Person_ID")}, 
            inverseJoinColumns = {@JoinColumn (name= "License_ID", referencedColumnName = "License_ID")}
            )
    private Collection<Licenses> licenses = new HashSet<>();

    // MODIFICATION DONE BY ME

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z0-9\\s]*", message = "Field to contain alphabetical and numeric characters only")
    private String addressLine1;

    @Transient
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z0-9\\s]*", message = "Field to contain alphabetical and numeric characters only")
    private String addressLine2;

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z0-9\\s]*", message = "Field to contain alphabetical and numeric characters only")
    private String country;

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z0-9\\s]*", message = "Field to contain alphabetical and numeric characters only")
    private String city;

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z0-9\\s]*", message = "Field to contain alphabetical and numeric characters only")
    private String town;

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z0-9\\s]*", message = "Field to contain alphabetical and numeric characters only")
    private String county;

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    @Size(max=20, message = "Field must contain less than 20 characters")
    @Pattern( regexp = "[a-zA-Z0-9\\s]*", message = "Field to contain alphabetical and numeric characters only")
    private String postCode;

    public Person(){}

    public Person(Person person) {
        this.personId = person.getPersonId();
        this.firstname = person.getFirstname();
        this.middleName = person.getMiddleName();
        this.lastname = person.getLastname();
        this.addressPerson = person.getAddressPerson();
        this.alias = person.getAlias();
        this.dateOfBirth = person.getDateOfBirth();
        this.sex = person.getSex();
        this.ethnicOrigin = person.getEthnicOrigin();
        this.height = person.getHeight();
        this.weight = person.getWeight();
        this.licenses = person.getLicense();
        this.personId = person.getPersonId();
    }

    public Collection<Licenses> getLicense() {
        return licenses;
    }

    public void setLicenses(Collection<Licenses> licenses) {
        this.licenses = licenses;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddressPerson() {
        return addressPerson;
    }

    public void setAddressPerson(String addressPerson) {
        this.addressPerson = addressPerson;
    }
    public void setAddressPerson(){
        this.addressPerson = this.getAddressLine1() + "," + this.getAddressLine2() + "," + this.getCountry() + "," +
                this.getCity() + "," + this.getTown() + "," + this.getCounty() + "," + this.getPostCode();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if(sex.equals("-1")){
            this.sex = null;            
        }
        else{
            this.sex = sex;
        }
    }

    public String getEthnicOrigin() {
        return ethnicOrigin;
    }

    public void setEthnicOrigin(String ethnicOrigin) {
        this.ethnicOrigin = ethnicOrigin;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personId == person.personId &&
                Objects.equals(firstname, person.firstname) &&
                Objects.equals(middleName, person.middleName) &&
                Objects.equals(lastname, person.lastname) &&
                Objects.equals(addressPerson, person.addressPerson) &&
                Objects.equals(alias, person.alias) &&
                Objects.equals(dateOfBirth, person.dateOfBirth) &&
                Objects.equals(sex, person.sex) &&
                Objects.equals(ethnicOrigin, person.ethnicOrigin) &&
                Objects.equals(height, person.height) &&
                Objects.equals(weight, person.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, firstname, middleName, lastname, addressPerson, alias, dateOfBirth, sex, ethnicOrigin, height, weight);
    }

    // MODIFICATIONS ADDED BY ME
    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

}
