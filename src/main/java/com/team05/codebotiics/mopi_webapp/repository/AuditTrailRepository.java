package com.team05.codebotiics.mopi_webapp.repository;

import com.team05.codebotiics.mopi_webapp.model.beans.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repository used to interact with the "Audit Trail" table
 */
@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Integer> {

    @Query("SELECT a FROM AuditTrail a WHERE a.police.badgeNumber=?1")
    Collection<AuditTrail> findByPoliceBadgeNumber(int badgeNumber);
}
