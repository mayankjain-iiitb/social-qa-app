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
@Table(name = "answer", schema = "postgres")
@NamedQueries({
    @NamedQuery(name = "AnswerEntityByUuid",
        query = "select i from AnswerEntity i where i.uuid = :uuid"),
    @NamedQuery(name = "AnswerEntityByid",
        query = "select i from AnswerEntity i where i.id = :id"),
    @NamedQuery(name = "AnswerEntityByQuestionEntity",
        query = "select i from AnswerEntity i where i.questionEntity = :questionEntity")
})
public class AnswerEntity implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "UUID")
  @Size(max = 200)
  private String uuid;

  @Column(name = "ans")
  @Size(max = 255)
  private String ans;

  @Column(name = "date")
  private ZonedDateTime date;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private QuestionEntity questionEntity;

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

  public String getAns() {
    return ans;
  }

  public void setAns(String ans) {
    this.ans = ans;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public void setDate(ZonedDateTime date) {
    this.date = date;
  }

  public UserEntity getUserEntity() {
    return userEntity;
  }

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
  }

  public QuestionEntity getQuestionEntity() {
    return questionEntity;
  }

  public void setQuestionEntity(QuestionEntity questionEntity) {
    this.questionEntity = questionEntity;
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
