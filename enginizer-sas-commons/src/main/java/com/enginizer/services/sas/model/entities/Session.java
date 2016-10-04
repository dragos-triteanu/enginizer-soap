package com.enginizer.services.sas.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "SAS_SESSION")
public class Session {

    @Id
    @Column(name = "SESSION_ID")
    private String sessionID;

    @Column(name = "SESSION_DATA")
    private Long sessionData;

    @Column(name = "ROLE_ID")
    private String roleID;

    @Column(name = "SESSION_CLOSED")
    private Date sessionClosed;

    @Column(name = "SESSION_CREATED")
    private Date sessionCreated;

    @Column(name = "SESSION_LAST_USED")
    private Date sessionLastUsed;

    @Column(name = "USER_ID")
    private Long userID;

    @Column(name = "PRINCIPAL_TYPE_ID")
    private String principalTypeID;

    @Column(name = "PRINCIPAL_ID")
    private String principalID;

    @Column(name = "FRONTEND_ID")
    private String frontendID;

    public Long getSessionData() {
        return sessionData;
    }

    public void setSessionData(Long sessionData) {
        this.sessionData = sessionData;
    }

    public Date getSessionClosed() {
        return sessionClosed;
    }

    public void setSessionClosed(Date sessionClosed) {
        this.sessionClosed = sessionClosed;
    }

    public Date getSessionCreated() {
        return sessionCreated;
    }

    public void setSessionCreated(Date sessionCreated) {
        this.sessionCreated = sessionCreated;
    }

    public Date getSessionLastUsed() {
        return sessionLastUsed;
    }

    public void setSessionLastUsed(Date sessionLastUsed) {
        this.sessionLastUsed = sessionLastUsed;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getPrincipalTypeID() {
        return principalTypeID;
    }

    public void setPrincipalTypeID(String principalTypeID) {
        this.principalTypeID = principalTypeID;
    }

    public String getPrincipalID() {
        return principalID;
    }

    public void setPrincipalID(String principalID) {
        this.principalID = principalID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getFrontendID() {
        return frontendID;
    }

    public void setFrontendID(String frontendID) {
        this.frontendID = frontendID;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }
}
