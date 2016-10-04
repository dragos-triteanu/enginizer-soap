package com.enginizer.services.sas.model.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SAS_PRINCIPALS")
public class Principal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRINCIPAL_ID", nullable = false)
    private Long principalID;
    @Column(name = "PRINCIPAL_TYPE_ID", nullable = false)
    private String principalTypeID;
    @Column(name = "PRINCIPAL_DATA", nullable = false)
    private String principalData;

    @Column(name = "USER_ID")
    private Long userID;
    @Column(name = "VALID_FROM")
    private Date validFrom;
    @Column(name = "VALID_UNTIL")
    private Date validUntil;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "SAS_PRINCIPAL_CREDENTIAL",
            joinColumns = {@JoinColumn(name = "PRINCIPAL_ID")},
            inverseJoinColumns = {@JoinColumn(name = "CREDENTIAL_ID")})
    @Cascade(CascadeType.ALL)
    public Set<Credential> credentials;

    @OneToOne
    @JoinColumn(name = "PRINCIPAL_TYPE_ID", insertable = false, updatable = false)
    private PrincipalType principalType;

    private Principal(Builder builder) {
        this.principalID = builder.principalId;
        this.principalData = builder.principalData;
        this.principalTypeID = builder.principalTypeID;
        this.userID = builder.userID;
        this.credentials = builder.credentials;
        this.validFrom = builder.validFrom;
        this.validUntil = builder.validUntil;
    }

    public Long getPrincipalID() {
        return principalID;
    }

    public void setPrincipalID(Long principalID) {
        this.principalID = principalID;
    }

    public void setPrincipalData(String principalData) {
        this.principalData = principalData;
    }

    public Principal() {}

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public PrincipalType getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(PrincipalType principalType) {
        this.principalType = principalType;
    }

    public Set<Credential> getCredentials() {
        if(null == credentials){
            credentials = new HashSet<>();
            return credentials;
        }
        return credentials;
    }

    public void setCredentials(Set<Credential> credentials) {
        this.credentials = credentials;
    }

    public String getPrincipalTypeID() {
        return principalTypeID;
    }

    public void setPrincipalTypeID(String principalTypeID) {
        this.principalTypeID = principalTypeID;
    }

    public String getPrincipalData() {
        return principalData;
    }

    public static class Builder {
        private Long userID;
        private Date validFrom;
        private Date validUntil;
        public Set<Credential> credentials;
        public Long principalId;
        public String principalData;
        public String principalTypeID;

        public Builder withPrincipalData(final String principalData) {
            this.principalData = principalData;
            return this;
        }

        public Builder withPrincipalTypeId(final String principalTypeID) {
            this.principalTypeID = principalTypeID;
            return this;
        }

        public Builder withUserID(Long userID) {
            this.userID = userID;
            return this;
        }

        public Builder withCredentials(Set<Credential> credentials) {
            this.credentials = credentials;
            return this;
        }

        public Builder withValidFrom(Date validFrom) {
            this.validFrom = validFrom;
            return this;
        }

        public Builder withValidUntil(Date validUntil) {
            this.validUntil = validUntil;
            return this;
        }

        /**
         * Adds a {@link Credential} to the principal's credentials.
         *
         * @param credential the credential to be added
         * @return the builder
         */
        public Builder withCredential(Credential credential) {
            Set<Credential> credentials;
            if (this.credentials == null) {
                credentials = new HashSet<>();
                this.credentials = credentials;
            } else {
                credentials = this.credentials;
            }
            credentials.add(credential);

            return this;
        }

        public Principal build() {
            return new Principal(this);
        }
    }
}
