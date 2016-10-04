package com.enginizer.services.sas.repository;

import com.enginizer.services.sas.model.entities.Frontend;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository class for managing entities in the ZAAS_TBL_FRONTENDS db table.
 */
public interface FrontendRepository extends CrudRepository<Frontend, String> {

}
