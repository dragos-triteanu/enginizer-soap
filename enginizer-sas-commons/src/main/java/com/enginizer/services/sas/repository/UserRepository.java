package com.enginizer.services.sas.repository;

import com.enginizer.services.sas.model.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

/**
 * {@link CrudRepository} that also defines some custom named queries for managing {@link User} entities.
 */
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    @Modifying
    @Query("update User user " +
            "set user.loginFailures =:loginFailures, user.loginFailureDate =:loginFailureDate " +
            "where user.userID =:userID")
    void updateLoginFailuresAndDate(@Param("userID") Long userID,
                                    @Param("loginFailures") Integer loginFailures,
                                    @Param("loginFailureDate") Date loginFailureDate);

    @Modifying
    @Query("update User user " +
            "set user.lastLoginDate =:lastLoginDate, user.loginFailures =:loginFailures, " +
            "user.loginFailureDate =:loginFailureDate " +
            "where user.userID =:userID")
    void updateLastLoginDateAndResetLoginFailures(@Param("userID") Long userID,
                                                  @Param("lastLoginDate") Date lastLoginDate,
                                                  @Param("loginFailures") Integer loginFailures,
                                                  @Param("loginFailureDate") Date loginFailureDate);

    @Modifying
    @Query("update User user set user.status =:status where user.userID =:userID")
    void updateUserStatus(@Param("userID") Long userID, @Param("status") String status);

}
