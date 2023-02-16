package com.team05.codebotiics.mopi_webapp.repository;

import com.team05.codebotiics.mopi_webapp.model.beans.Evidence;
import com.team05.codebotiics.mopi_webapp.model.beans.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, Integer> {
    Optional<Evidence> findByEvidenceId(Integer evidenceId);
}
