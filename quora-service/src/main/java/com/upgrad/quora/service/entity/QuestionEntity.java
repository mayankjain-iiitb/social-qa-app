package com.upgrad.quora.service.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "question", schema = "postgres")
@NamedQueries({
    @NamedQuery(name = "QuestionEntityByUuid",
        query = "select i from QuestionEntity i where i.uuid = :uuid"),
    @NamedQuery(name = "QuestionEntityByid",
        query = "select i from QuestionEntity i where i.id = :id"),
    @NamedQuery(name = "QuestionEntitiesByUser",
        query = "select i from QuestionEntity i where i.user = :user"),
    @NamedQuery(name = "QuestionEntityByContent",
        query = "select i from QuestionEntity i where i.content = :content")
})
public class QuestionEntity implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "uuid")
  @Size(max = 200)
  private String uuid;

  @Column(name = "content")
  @Size(max = 200)
  private String content;

  @Column(name = "date")
  private ZonedDateTime date;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public void setDate(ZonedDateTime date) {
    this.date = date;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
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
