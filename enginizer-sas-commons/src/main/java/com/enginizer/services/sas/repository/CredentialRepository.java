package com.enginizer.services.sas.repository;

import com.enginizer.services.sas.model.entities.Credential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link CrudRepository} that manages {@link Credential} entities.
 */
public interface CredentialRepository extends CrudRepository<Credential, Long> {

}
