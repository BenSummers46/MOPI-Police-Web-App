package com.team05.codebotiics.mopi_webapp.repository;

import com.team05.codebotiics.mopi_webapp.model.beans.Licenses;
import com.team05.codebotiics.mopi_webapp.model.beans.Person;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Repository used to interact with the "Person" table
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query(value = "SELECT * FROM person p WHERE p.first_name= ?1", nativeQuery = true)
    Optional<Collection<Person>> findByFirstName(String firstName);

    Optional<Collection<Person>> findByMiddleName(String middleName);

    Optional<Collection<Person>> findByLastname(String lastname);

    @Transactional
    @Modifying
    @Query("UPDATE Person p set p.licenses=?2 WHERE p.personId=?1")
    void updateLicensesById(int personId, Collection<Licenses> licenses);

    @Query("SELECT DISTINCT p FROM IncidentReport i JOIN i.suspects p WHERE p.firstname= ?1")
    Optional<Collection<Person>> findBySuspectFirstName(String firstName);

    @Query("SELECT p FROM IncidentReport i JOIN i.suspects p WHERE i.incidentURN= ?1")
    Optional<Collection<Person>> findBySuspectIncidentURN(Integer incidentURN);
}
