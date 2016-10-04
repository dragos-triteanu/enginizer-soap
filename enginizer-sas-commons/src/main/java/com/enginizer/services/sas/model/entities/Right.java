package com.enginizer.services.sas.model.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for mapping the ZAAS_TBL_RIGHT table, to an object.
 */
@Entity
@Table(name = "SAS_RIGHT")
public class Right {

    @Id
    @Column(name = "RIGHT_ID")
    private String rightID;

    @Column(name = "RIGHT_NAME")
    private String rightName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "RIGHT_TYPE")
    private String rightType;

    public String getRightID() {
        return rightID;
    }

    public void setRightID(String rightID) {
        this.rightID = rightID;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRightType() {
        return rightType;
    }

    public void setRightType(String rightType) {
        this.rightType = rightType;
    }
}
