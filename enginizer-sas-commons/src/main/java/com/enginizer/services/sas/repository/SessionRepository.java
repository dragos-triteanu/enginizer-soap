package com.enginizer.services.sas.repository;

import com.enginizer.services.sas.model.entities.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

/**
 * {@link CrudRepository} that also defines some custom methods for managing {@link Session} entities.
 */
@Transactional
public interface SessionRepository extends CrudRepository<Session, String> {

    @Modifying
    @Query("update Session session set session.sessionLastUsed =:sessionLastUsed where session.sessionID =:sessionID")
    void updateSessionLastUsed(@Param("sessionID") final String sessionID,
                               @Param("sessionLastUsed") final Date sessionLastUsed);

    @Modifying
    @Query("update Session session set session.sessionClosed =:sessionClosed where session.sessionID =:sessionID")
    void updateSessionClosed(@Param("sessionID") final String sessionID, @Param("sessionClosed") Date sessionClosed);
}
