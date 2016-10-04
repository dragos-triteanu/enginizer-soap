package com.enginizer.services.sas.client.converter.response;

import com.enginizer.services.sas.client.spi.LoginResponseDTO;
import com.enginizer.services.sas.client.spi.UserRoleDTO;
import com.enginizer.services.sas.v1.LoginResponse;
import com.enginizer.services.sas.v1.UserRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Converter.
 */
public class LoginResponseConverter implements ResponseConverter<LoginResponse, LoginResponseDTO> {

    /**
     * Converts a {@link LoginResponseDTO} into a {@link LoginResponse}.
     *
     * @param loginResponse {@link LoginResponse}.
     * @return {@link LoginResponseDTO}.
     */
    @Override
    public LoginResponseDTO from(LoginResponse loginResponse) {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setReturnCode(loginResponse.getReturnCode());
        dto.setAttemptsLeft(loginResponse.getAttemptsLeft());
        dto.setSessionToken(loginResponse.getSessionToken());
        dto.setErrorCounter(loginResponse.getErrorCounter());
        dto.setReasonCode(loginResponse.getReasonCode());
        dto.setErrorMessage(loginResponse.getErrorMessage());
        dto.setSessionTimeout(loginResponse.getSessionTimeout());
        dto.setCorrelationId(loginResponse.getCorrelationId());

        if (null != loginResponse.getRoles()) {
            Set<UserRoleDTO> userRoleDTO = new HashSet<>();
            loginResponse.getRoles().getRole().stream().forEach(userRole -> userRoleDTO.add(from(userRole)));
            dto.setRoles(userRoleDTO);
        }

        return dto;
    }

    private UserRoleDTO from(final UserRole userRole) {
        UserRoleDTO roleDTO = new UserRoleDTO();
        roleDTO.setId(userRole.getId());
        return roleDTO;
    }
}
