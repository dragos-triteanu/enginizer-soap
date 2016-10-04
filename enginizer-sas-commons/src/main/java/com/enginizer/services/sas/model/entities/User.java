package com.enginizer.services.sas.model.entities;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class containing user data.
 */
@Entity
@Table(name = "SAS_USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID", nullable = false)
    private Long userID;

    @Column(name = "STATUS")
    private String status;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "SMS_NUMBER")
    private String smsNumber;
    @Column(name = "BIRTHDAY")
    private Date birthday;
    @Column(name = "LOGIN_FAILURES")
    private Integer loginFailures;
    @Column(name = "LOGIN_FAILURE_DATE")
    private Date loginFailureDate;
    @Column(name = "LAST_LOGIN_DATE")
    private Date lastLoginDate;
    @Column(name = "SMS_STATUS")
    private String smsStatus;
    @Column(name = "SMS_STATUS_CHANGE_TIMESTAMP")
    private Date smsStatusChangeTimestamp;


    @OneToMany(mappedBy = "userID", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Principal> principals;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SAS_USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Role> roles;

    public static class Builder {

        private Long userID;
        private Set<Principal> principals;
        private String firstName;
        private String lastName;
        private String isEmployee;
        private String status;
        private Integer loginFailures;
        private Date loginFailureDate;
        private String crmCustomerNo;
        private Date lastLoginDate;
        private Integer mtanErrorCtr;
        private Date mtanRegistrationTimestamp;
        private Date smsRegistrationTimestamp;
        private String smsNumber;
        private String smsStatus;
        private Date smsStatusChangeTimestamp;
        private Date birthday;
        private String email;
        private Set<Role> roles;

        public Builder withUserID(Long userID) {
            this.userID = userID;
            return this;
        }
        public Builder withPrincipals(Set<Principal> principals) {
            this.principals = principals;
            return this;
        }

        /**
         * Adds the received principal to {@link User}'s principals.
         *
         * @param principal the principal to be added
         * @return builder with the received principal set
         */
        public Builder withPrincipal(Principal principal) {
            Set<Principal> principals;
            if (this.principals == null) {
                principals = new HashSet<>();
                this.principals = principals;
            } else {
                principals = this.principals;
            }
            principals.add(principal);

            return this;
        }
        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder isEmployee(String isEmployee) {
            this.isEmployee = isEmployee;
            return this;
        }
        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }
        public Builder withCrmCustomerNo(String crmCustomerNo) {
            this.crmCustomerNo = crmCustomerNo;
            return this;
        }
        public Builder withMtanRegistrationTimestamp(Date mtanRegistrationTimestamp) {
            this.mtanRegistrationTimestamp = mtanRegistrationTimestamp;
            return this;
        }
        public Builder withSmsRegistrationTimestamp(Date smsRegistrationTimestamp) {
            this.smsRegistrationTimestamp = smsRegistrationTimestamp;
            return this;
        }
        public Builder withSmsNumber(String smsNumber){
            this.smsNumber = smsNumber;
            return this;
        }
        public Builder withSmsStatus(String smsStatus) {
            this.smsStatus = smsStatus;
            return this;
        }
        public Builder withSmsStatusChangeTimestamp(Date smsStatusChangeTimestamp) {
            this.smsStatusChangeTimestamp = smsStatusChangeTimestamp;
            return this;
        }
        public Builder withBirthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        /**
         * Adds a {@link Role} to the builder's roles.
         * @param role the role to be added
         * @return the Builder with the role set
         */
        public Builder withRole(Role role) {
            Set<Role> roles;
            if (this.roles == null) {
                roles = new HashSet<>();
                this.roles = roles;
            } else {
                roles = this.roles;
            }
            roles.add(role);

            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        this.userID = builder.userID;
        this.principals = builder.principals;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.status = builder.status;
        this.smsNumber = builder.smsNumber;
        this.smsStatus = builder.smsStatus;
        this.birthday = builder.birthday;
        this.email = builder.email;
        this.roles = builder.roles;
    }

    public User() {}

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLoginFailures() {
        return loginFailures;
    }

    public void setLoginFailures(Integer loginFailures) {
        this.loginFailures = loginFailures;
    }

    public Date getLoginFailureDate() {
        return loginFailureDate;
    }

    public void setLoginFailureDate(Date loginFailureDate) {
        this.loginFailureDate = loginFailureDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public Date getSmsStatusChangeTimestamp() {
        return smsStatusChangeTimestamp;
    }

    public void setSmsStatusChangeTimestamp(Date smsStatusChangeTimestamp) {
        this.smsStatusChangeTimestamp = smsStatusChangeTimestamp;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Set<Role> getRoles() {
        if(null == roles){
            roles = new HashSet<>();
            return roles;
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Principal> getPrincipals() {
        if (null == principals) {
            principals = new HashSet<>();
            return principals;
        }
        return principals;
    }

    public void setPrincipals(Set<Principal> principals) {
        this.principals = principals;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
