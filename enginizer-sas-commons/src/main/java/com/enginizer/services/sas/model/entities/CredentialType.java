package com.enginizer.services.sas.model.entities;
import javax.persistence.*;

@Entity
@Table(name = "SAS_CREDENTIAL_TYPES")
public class CredentialType {

    @Id
    @Column(name = "CREDENTIAL_TYPE_ID")
    private String credentialTypeID;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getCredentialTypeID() {
        return credentialTypeID;
    }

    public void setCredentialTypeID(String credentialTypeID) {
        this.credentialTypeID = credentialTypeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
