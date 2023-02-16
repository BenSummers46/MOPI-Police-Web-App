package com.team05.codebotiics.mopi_webapp.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.team05.codebotiics.mopi_webapp.model.beans.IncidentReport;
import com.team05.codebotiics.mopi_webapp.model.enums.CrimeType;
import com.team05.codebotiics.mopi_webapp.model.enums.SecurityAccessLevel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository used to interact with the "Incident Report" table
 */
@Repository
public interface IncidentReportRepository extends JpaRepository<IncidentReport, Integer> {

    /**
     * Searches the Incident Report table for a record by Incident URN.
     * @param incidentURN
     * @return An Optional<IncidentReport>. The Optional will contain an IncidentReport instance if found or it will be empty if it isn't found
     */
    @Query("SELECT i FROM IncidentReport i WHERE i.incidentURN= ?1")
    Optional<IncidentReport> findByIncidentURN(Integer incidentURN);

    @Query("SELECT i FROM IncidentReport i WHERE (i.dateAndTime<=?1 AND i.crimeType=?2)")
    Optional<Collection<IncidentReport>> findByExpiryDate(LocalDateTime expiry, CrimeType crimeType);

    @Query("SELECT i FROM IncidentReport i WHERE (i.dateAndTime>=?1 AND i.dateAndTime<=?2)")
    Optional<Collection<IncidentReport>> findByDateRange(LocalDateTime from, LocalDateTime to);

    @Query("SELECT i FROM IncidentReport i WHERE (i.latitude>=?1 AND i.latitude<=?2 AND i.longitude>=?3 AND i.longitude<=?4)")
    Optional<Collection<IncidentReport>> findByBoundingBox(double minLatitude,double maxLatitude, double minLongitude,double maxLongitude);

    Optional<List<IncidentReport>> findBysecurityAccessLevelIn(Collection<SecurityAccessLevel> security_access_level );

    Optional<Collection<IncidentReport>> findByBadgeNumber(Integer badgeNumber);

    // Search by Crime Type
    Optional<List<IncidentReport>> findBycrimeType(CrimeType crimeType);

    // Search by Crime Type based on Access Level
    Optional<List<IncidentReport>> findBycrimeTypeAndSecurityAccessLevelIn(CrimeType crimeType, Collection<SecurityAccessLevel> accessLevels);

    @Transactional
    Long deleteByIncidentURN(int incidentURN);

}
