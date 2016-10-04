package com.enginizer.services.sas.client.spi;

import java.util.Set;

/**
 * DTO for modeling the {@link } at domain level.
 */
public class LoginResponseDTO extends ReplyDTO {

    private Integer attemptsLeft;
    private String sessionToken;
    private Set<UserRoleDTO> roles;
    private Integer sessionTimeout;

    public Integer getAttemptsLeft() {
        return attemptsLeft;
    }

    public void setAttemptsLeft(Integer attemptsLeft) {
        this.attemptsLeft = attemptsLeft;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Set<UserRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRoleDTO> roles) {
        this.roles = roles;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
