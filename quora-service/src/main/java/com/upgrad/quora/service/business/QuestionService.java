package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

  @Autowired
  private QuestionDao questionDao;

  @Autowired
  private UserDao userDao;

  /**create a question.
   * @param questionEntity questionEntity
   * @return questionEntity
   * @throws InvalidQuestionException InvalidQuestionException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity createQuestion(QuestionEntity questionEntity)
      throws InvalidQuestionException {
    String content = questionEntity.getContent();
    if (content == null || content.isEmpty() || content.trim().isEmpty()) {
      throw new InvalidQuestionException("QUE-888", "Content can't be null or empty");
    }

    if (questionDao.getQuestionByContent(content.trim()) != null) {
      throw new InvalidQuestionException("QUE-999",
          "Question already exists. Duplicate question not allowed");
    }

    return questionDao.createQuestion(questionEntity);
  }

  /**edit a question.
   * @param content question content
   * @param userUuid user uuid
   * @param questionuuid question uuid
   * @return QuestionEntity
   * @throws AuthorizationFailedException AuthorizationFailedException
   * @throws InvalidQuestionException InvalidQuestionException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity editQuestion(String content, String userUuid, String questionuuid)
      throws AuthorizationFailedException, InvalidQuestionException {
    QuestionEntity questionEntity = questionDao.getQuestion(questionuuid);
    if (questionEntity == null) {
      throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
    if (userUuid != null && !userUuid.equals(questionEntity.getUser().getUuid())) {
      throw new AuthorizationFailedException("ATHR-003",
          "Only the question owner can edit the question");
    }
    if (content == null || content.isEmpty() || content.trim().isEmpty() || content
        .equalsIgnoreCase(questionEntity.getContent())) {
      throw new InvalidQuestionException("QUE-888",
          "Content can't be null or empty or equal to existing content");
    }
    questionEntity.setContent(content);
    questionDao.updateQuestion(questionEntity);
    return questionDao.getQuestion(questionuuid);
  }

  public List<QuestionEntity> getAllQuestions() {
    return questionDao.findAll();
  }

  /**get question by uuid.
   * @param questionuuid questionuuid
   * @return QuestionEntity
   * @throws InvalidQuestionException InvalidQuestionException
   */
  public QuestionEntity getQuestionById(String questionuuid) throws InvalidQuestionException {
    QuestionEntity questionEntity = questionDao.getQuestion(questionuuid);
    if (questionEntity == null) {
      throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
    }
    return questionEntity;
  }

  /**get all questions by user.
   * @param uuid user uuid
   * @return List of all QuestionEntity
   * @throws UserNotFoundException UserNotFoundException
   */
  public List<QuestionEntity> getAllQuestionsByUser(String uuid) throws UserNotFoundException {
    UserEntity user = userDao.getuserbyuuid(uuid);
    if (user == null) {
      throw new UserNotFoundException("USR-001",
          "User with entered uuid whose question details are to be seen does not exist");
    }
    return questionDao.findAllByUser(user);
  }

  /**delete a question.
   * @param user userEntity
   * @param questionuuid question uuid
   * @return QuestionEntity
   * @throws AuthorizationFailedException AuthorizationFailedException
   * @throws InvalidQuestionException InvalidQuestionException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity deleteQuestion(UserEntity user, String questionuuid)
      throws AuthorizationFailedException, InvalidQuestionException {
    QuestionEntity questionEntity = questionDao.getQuestion(questionuuid);
    if (questionEntity == null) {
      throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }

    if (!user.getUuid().equals(questionEntity.getUser().getUuid()) && !user.getRole()
        .equalsIgnoreCase("admin")) {
      throw new AuthorizationFailedException("ATHR-003",
          "Only the question owner or admin can delete the question");
    }
    return questionDao.deleteQuestion(questionEntity);
  }
}
