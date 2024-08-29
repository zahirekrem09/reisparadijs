package com.reisparadijs.reisparadijs.business.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 07 August Wednesday 2024 - 19:35
 */

public class AppUser  {

    public AppUser(Integer userId) {
        this.id = userId;
    }

    public enum  Gender {
        MALE("male"),
        FEMALE("female"),
        UNDETERMINED("undisclosed");
        private String value;
        Gender(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static Gender fromString(String gender) {
            for (Gender g : Gender.values()) {
                if (g.value.equalsIgnoreCase(gender)) {
                    return g;
                }
            }
            return UNDETERMINED;
        }
    }

    //TODO :  implement Spring Security UserDetails
    private Integer id;
    private String userName;
    private String password;
    private String firstName;
    private String infix;
    private String lastName;
    private String email;
    private  Gender gender;
    private byte[] profileImage;
    private Date joinedAt;
    private Boolean enabled = false;
    private List<Role> roles;

    public AppUser() {
        this.roles = new ArrayList<>();
        this.gender = Gender.UNDETERMINED;
    }
    public AppUser(Integer id, String userName, String password, String firstName, String infix,
                   String lastName, String email, Gender gender, byte[] profileImage, Date joinedAt, Boolean enabled) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.infix = infix;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.profileImage = profileImage;
        this.joinedAt = joinedAt;
        this.enabled = enabled;
        this.roles = new ArrayList<>();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getInfix() {
        return infix;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + infix + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender.getValue();
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", infix='" + infix + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppUser user = (AppUser) o;

        if (!Objects.equals(id, user.id)) return false;
        if (!email.equals(user.email)) return false;
        if (!userName.equals(user.userName)) return false;
        return password.equals(user.password);
    }
// fixme :  fix hashCode with Objects.hash
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + email.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
