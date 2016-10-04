package com.enginizer.services.sas.repository;

import com.enginizer.services.sas.model.entities.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository class for managing entities in the ZAAS_TBL_ROLE db table.
 */
public interface RoleRepository extends CrudRepository<Role, String> {

    Role findByRoleName(final String roleName);
}
