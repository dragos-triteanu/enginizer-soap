package com.enginizer.services.sas.repository;

import com.enginizer.services.sas.model.entities.Principal;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link CrudRepository} for managing {@link Principal} entities.
 */
@Transactional
public interface PrincipalRepository extends CrudRepository<Principal, Long> {

    Principal findByPrincipalDataAndPrincipalTypeID(String principalData,String principalTypeId);

}
