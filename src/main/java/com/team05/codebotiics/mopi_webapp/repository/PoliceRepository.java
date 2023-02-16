package com.team05.codebotiics.mopi_webapp.repository;

import com.team05.codebotiics.mopi_webapp.model.beans.Person;
import com.team05.codebotiics.mopi_webapp.model.beans.Police;
import com.team05.codebotiics.mopi_webapp.model.enums.Rank;
import com.team05.codebotiics.mopi_webapp.model.enums.Role;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository used to interact with the "Police" table
 */
@Repository
public interface PoliceRepository extends JpaRepository<Police, Integer> {

    /**
     * Searches the Police table for a record by badge number.
     * @param badgeNumber
     * @return An Optional<Police>. The Optional will contain a Police instance if found or it will be empty if it isn't found
     */
    @Query("SELECT p FROM Police p WHERE p.badgeNumber= ?1")
    Optional<Police> findByBadgeNumber(Integer badgeNumber);

    @Query("SELECT p FROM Police p WHERE p.person.personId= ?1")
    Optional<Police> findByPersonId(Integer personId);

    @Transactional
    @Modifying
    @Query("UPDATE Police p set p.rank_l=?2 WHERE p.badgeNumber=?1")
    void updateRankByBadgeNumber(int badgeNumber, String rank);

    @Transactional
    @Modifying
    @Query("UPDATE Police p set p.role=?2 WHERE p.badgeNumber=?1")
    void updateRoleByBadgeNumber(int badgeNumber, Role role);

    @Transactional
    @Modifying
    @Query("UPDATE Police p set p.securityAccessLevel=?2 WHERE p.badgeNumber=?1")
    void updateSecurityAccessLevelByBadgeNumber(int badgeNumber, SecurityAccessLevel securityAccessLevel);

}
