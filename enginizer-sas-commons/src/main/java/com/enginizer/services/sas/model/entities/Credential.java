package com.enginizer.services.sas.model.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SAS_CREDENTIALS")
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CREDENTIAL_ID", nullable = false)
    private Long credentialID;

    @Column(name = "CREDENTIAL_TYPE_ID")
    private String credentialTypeID;
    @Column(name = "CREDENTIAL_DATA")
    private String credentialData;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "VALID_UNTIL")
    private Date validUntil;

    @OneToOne
    @JoinColumn(name = "CREDENTIAL_TYPE_ID", insertable = false, updatable = false)
    private CredentialType credentialType;

    public Credential() {}

    private Credential(Builder builder) {
        this.credentialID = builder.credentialID;
        this.credentialTypeID = builder.credentialTypeID;
        this.credentialData = builder.credentialData;
        this.status = builder.status;
        this.validUntil = builder.validUntil;
    }

    public static class Builder {

        private Long credentialID;
        private String credentialTypeID;
        private String credentialData;
        private String status;
        private Date validUntil;

        public Builder withCredentialID(Long credentialID) {
            this.credentialID = credentialID;
            return this;
        }

        public Builder withCredentialTypeID(String credentialTypeID) {
            this.credentialTypeID = credentialTypeID;
            return this;
        }

        public Builder withCredentialData(String credentialData) {
            this.credentialData = credentialData;
            return this;
        }

        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder withValidUntil(Date validUntil) {
            this.validUntil = validUntil;
            return this;
        }

        public Credential build() {
            return new Credential(this);
        }
    }

    public Long getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(Long credentialID) {
        this.credentialID = credentialID;
    }

    public String getCredentialTypeID() {
        return credentialTypeID;
    }

    public void setCredentialTypeID(String credentialTypeID) {
        this.credentialTypeID = credentialTypeID;
    }

    public String getCredentialData() {
        return credentialData;
    }

    public void setCredentialData(String credentialData) {
        this.credentialData = credentialData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public CredentialType getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(CredentialType credentialType) {
        this.credentialType = credentialType;
    }
}
