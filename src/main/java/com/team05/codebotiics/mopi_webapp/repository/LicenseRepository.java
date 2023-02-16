package com.team05.codebotiics.mopi_webapp.repository;

import com.team05.codebotiics.mopi_webapp.model.beans.Licenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository used to interact with the "License" table
 */

@Repository
public interface LicenseRepository extends JpaRepository<Licenses, Integer> {

	Optional<Licenses> findByLicenseId(int licenseID);
	//void save(@Valid Licenses license);

	Optional<Licenses> findByLicenseType(String licenseType);

}