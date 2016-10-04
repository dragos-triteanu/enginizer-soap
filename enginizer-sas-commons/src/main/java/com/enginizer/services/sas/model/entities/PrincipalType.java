package com.enginizer.services.sas.model.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "SAS_PRINCIPAL_TYPES")
public class PrincipalType implements Serializable {

    @Id
    @Column(name = "PRINCIPAL_TYPE_ID", nullable = false)
    private String principalTypeID;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getPrincipalTypeID() {
        return principalTypeID;
    }

    public void setPrincipalTypeID(String principalTypeID) {
        this.principalTypeID = principalTypeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
