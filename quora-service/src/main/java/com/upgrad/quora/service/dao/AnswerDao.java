package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Repository;

@SuppressWarnings("JpaQueryApiInspection")
@Repository
public class AnswerDao {

  @PersistenceContext
  private EntityManager entityManager;

  /** create an answer.
   * @param answerEntity AnswerEntity
   * @return AnswerEntity
   */
  public AnswerEntity createAnswer(AnswerEntity answerEntity) {
    entityManager.persist(answerEntity);
    return answerEntity;
  }


  /** get answer by uuid.
   * @param answeruuid answer uuid
   * @return AnswerEntity
   */
  public AnswerEntity getAnswer(final String answeruuid) {
    try {
      return entityManager.createNamedQuery("AnswerEntityByUuid", AnswerEntity.class)
          .setParameter("uuid", answeruuid).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**Update an answer.
   * @param answerEntity  answerEntity
   * @return answerEntity
   */
  public AnswerEntity updateAnswer(final AnswerEntity answerEntity) {
    return entityManager.merge(answerEntity);
  }

  /**get all answers for a given question.
   * @param questionEntity questionEntity
   * @return list of answerEntity
   */
  public List<AnswerEntity> answerEntityByQuestionEntity(final QuestionEntity questionEntity) {
    return entityManager
        .createNamedQuery(
            "AnswerEntityByQuestionEntity",
            AnswerEntity.class)
        .setParameter(
            "questionEntity",
            questionEntity)
        .getResultList();
  }

  /** Delete an answer.
   * @param answerEntity answerEntity
   * @return answerEntity
   */
  @OnDelete(action = OnDeleteAction.CASCADE)
  public AnswerEntity deleteAnswer(final AnswerEntity answerEntity) {
    entityManager.remove(answerEntity);
    return answerEntity;
  }
}
