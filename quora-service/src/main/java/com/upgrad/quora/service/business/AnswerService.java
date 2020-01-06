package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

  @Autowired
  private AnswerDao answerDao;

  @Autowired
  private QuestionDao questionDao;

  /**
   * Create an answer.
   *
   * @param answerEntity AnswerEntity
   * @return AnswerEntity
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity createAnswer(AnswerEntity answerEntity) {
    return answerDao.createAnswer(answerEntity);
  }

  /**
   * edit Answer.
   *
   * @param content    answer
   * @param user       user uuid
   * @param answerUuid answer uuid
   * @return AnswerEntity
   * @throws AuthorizationFailedException AuthorizationFailedException
   * @throws AnswerNotFoundException      AnswerNotFoundException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity editAnswer(
      String content,
      UserEntity user,
      String answerUuid
  ) throws AuthorizationFailedException,
      AnswerNotFoundException {
    AnswerEntity answerEntity = answerDao.getAnswer(answerUuid);
    if (answerEntity == null) {
      throw new AnswerNotFoundException(
          "ANS-001", "Entered answer uuid does not exist"
      );
    }

    if (!answerEntity.getUserEntity().getUuid().equals(user.getUuid())) {
      throw new AuthorizationFailedException(
          "ATHR-003",
          "Only the answer owner can edit the answer"
      );
    }

    answerEntity.setAns(content);
    return answerDao.updateAnswer(answerEntity);
  }

  /**get question by uuid.
   * @param questionuuid questionuuid
   * @return questionEntity
   * @throws InvalidQuestionException InvalidQuestionException
   */
  public QuestionEntity getQuestionById(String questionuuid) throws InvalidQuestionException {
    QuestionEntity questionEntity = questionDao.getQuestion(questionuuid);
    if (questionEntity == null) {
      throw new InvalidQuestionException("QUES-001",
          "The question with entered uuid whose details are to be seen does not exist");
    }
    return questionEntity;
  }

  /**get list of all answers to question.
   * @param questionEntity questionEntity
   * @return List of AnswerEntity
   */
  public List<AnswerEntity> getAllAnswersToQuestion(QuestionEntity questionEntity) {
    List<AnswerEntity> answerEntitieList = new ArrayList<>();
    answerEntitieList = answerDao.answerEntityByQuestionEntity(questionEntity);

    return answerEntitieList;
  }

  /**delete an answer.
   * @param userId user uuid
   * @param answeruuid answer uuid
   * @return answerEntity
   * @throws AnswerNotFoundException AnswerNotFoundException
   * @throws AuthorizationFailedException AuthorizationFailedException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity deleteAnswer(String userId, String answeruuid)
      throws AnswerNotFoundException, AuthorizationFailedException {
    AnswerEntity answerEntity = answerDao.getAnswer(answeruuid);
    if (answerEntity == null) {
      throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
    }
    if (!userId.equals(answerEntity.getUserEntity().getUuid()) && !answerEntity.getUserEntity()
        .getRole().equalsIgnoreCase("admin")) {
      throw new AuthorizationFailedException("ATHR-003",
          "Only the answer owner or admin can delete the answer");
    }

    return answerDao.deleteAnswer(answerEntity);
  }

}
