package com.enginizer.services.sas.model.entities;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "SAS_FRONTENDS")
public class Frontend {

    @Id
    @Column(name = "FRONTEND_ID")
    private String frontendID;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID", insertable = false , updatable = false)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Role role;

    @ManyToMany
    @JoinTable(name = "SAS_CREDENTIAL_FRONTEND",
               joinColumns = @JoinColumn(name = "FRONTEND_ID"),
               inverseJoinColumns = @JoinColumn(name = "CREDENTIAL_TYPE_ID"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<CredentialType> credentialTypes;

    public String getFrontendID() {
        return frontendID;
    }

    public void setFrontendID(String frontendID) {
        this.frontendID = frontendID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<CredentialType> getCredentialTypes() {
        return credentialTypes;
    }

    public void setCredentialTypes(Set<CredentialType> credentialTypes) {
        this.credentialTypes = credentialTypes;
    }
}
