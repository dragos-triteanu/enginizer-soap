package com.enginizer.services.sas.model.entities;
import javax.persistence.*;

@Entity
@Table(name = "SAS_ROLE_TYPES")
public class RoleTypes {

    @Id
    @Column(name = "ROLE_TYPE_ID")
    private String roleTypeID;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getRoleTypeID() {
        return roleTypeID;
    }

    public void setRoleTypeID(String roleTypeID) {
        this.roleTypeID = roleTypeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
