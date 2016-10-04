package com.enginizer.services.sas.model.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SAS_ROLE")
public class Role {

    @Id
    @Column(name = "ROLE_ID", nullable = false)
    private String roleID;

    @Column(name = "ROLE_NAME")
    private String roleName;
    @Column(name = "ROLE_TYPE_ID")

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SAS_ROLE_RIGHT",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "RIGHT_ID"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Right> rights;

    public static class Builder {

        private String roleID;
        private String roleName;
        private String roleTypeID;
        private String parentRoleID;

        public Builder withRoleID(String roleID) {
            this.roleID = roleID;
            return this;
        }

        public Builder withRoleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder withRoleTypeID(String roleTypeID) {
            this.roleTypeID = roleTypeID;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }

    private Role(Builder builder) {
        this.roleID = builder.roleID;
        this.roleName = builder.roleName;
    }

    public Role() {}

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Right> getRights() {
        return rights;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }
}
