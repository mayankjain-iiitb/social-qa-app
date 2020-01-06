package com.upgrad.quora.service.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "users", schema = "postgres")
@NamedQueries({
    @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email = :email"),
    @NamedQuery(name = "userByUsername",
        query = "select u from UserEntity u where u.username = :username"),
    @NamedQuery(name = "userByUUID", query = "select u from UserEntity u where u.uuid = :uuid")

})
public class UserEntity implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "uuid")
  @Size(max = 200)
  private String uuid;

  @Column(name = "firstname")
  @NotNull
  @Size(max = 30)
  private String firstName;

  @Column(name = "lastname")
  @NotNull
  @Size(max = 30)
  private String lastName;


  @Column(name = "username")
  @NotNull
  @Size(max = 30)
  private String username;

  @Column(name = "email")
  @Size(max = 50)
  private String email;

  @Column(name = "password")
  @NotNull
  @Size(max = 255)
  private String password;

  @Column(name = "salt")
  @NotNull
  @Size(max = 200)
  private String salt;

  @Column(name = "country")
  @Size(max = 30)
  private String country;

  @Column(name = "aboutme")
  @Size(max = 50)
  private String aboutme;

  @Column(name = "dob")
  @Size(max = 30)
  private String dob;

  @Column(name = "role")
  @NotNull
  @Size(max = 30)
  private String role;

  @Column(name = "contactnumber")
  @Size(max = 50)
  private String contactnumber;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAboutme() {
    return aboutme;
  }

  public void setAboutme(String aboutme) {
    this.aboutme = aboutme;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContactnumber() {
    return contactnumber;
  }

  public void setContactnumber(String contactnumber) {
    this.contactnumber = contactnumber;
  }

  @Override
  public boolean equals(Object obj) {
    return new EqualsBuilder().append(this, obj).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this).hashCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}