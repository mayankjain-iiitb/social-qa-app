package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AnswerController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private AnswerService answerService;

  @Autowired
  private QuestionService questionService;

  /**
   * Create an answer.
   *
   * @param authorization access  token
   * @param questionId    Question uuid
   * @param answerRequest AnswerRequest
   * @return AnswerResponse
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws AuthenticationFailedException AuthenticationFailedException
   * @throws InvalidQuestionException      InvalidQuestionException
   */
  @RequestMapping(method = RequestMethod.POST,
      path = "/question/{questionId}/answer/create",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<AnswerResponse> postAnswer(
      @RequestHeader("authorization") final String authorization,
      @PathVariable("questionId") final String questionId,
      final AnswerRequest answerRequest
  ) throws AuthorizationFailedException, AuthenticationFailedException, InvalidQuestionException {
    String accessToken = authenticationService.getBearerAccessToken(authorization);
    //Check if the bearer authentication exists
    UserAuthEntity userAuthEntity = authenticationService
        .validateBearerAuthentication(accessToken, "to post a answer");
    QuestionEntity questionEntity = questionService.getQuestionById(questionId);

    UserEntity user = userAuthEntity.getUser();
    AnswerEntity answerEntity = new AnswerEntity();
    answerEntity.setUuid(UUID.randomUUID().toString());
    answerEntity.setAns(answerRequest.getAnswer());
    answerEntity.setDate(ZonedDateTime.now());
    answerEntity.setUserEntity(user);
    answerEntity.setQuestionEntity(questionEntity);

    // create answer
    answerService.createAnswer(answerEntity);
    AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid())
        .status("ANSWER CREATED");
    return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
  }

  /**
   * Edit an answer content.
   *
   * @param authorization     access token
   * @param answerId          Answer uuid
   * @param answerEditRequest AnswerEditRequest
   * @return AnswerEditResponse
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws InvalidQuestionException      InvalidQuestionException
   * @throws AuthenticationFailedException AuthenticationFailedException
   * @throws AnswerNotFoundException       AnswerNotFoundException
   */
  @RequestMapping(method = RequestMethod.PUT,
      path = "/answer/edit/{answerId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<AnswerEditResponse> editAnswer(
      @RequestHeader("authorization") final String authorization,
      @PathVariable("answerId") final String answerId,
      final AnswerEditRequest answerEditRequest
  ) throws AuthorizationFailedException,
      InvalidQuestionException,
      AuthenticationFailedException, AnswerNotFoundException {

    String accessToken =
        authenticationService.getBearerAccessToken(authorization);
    //Check if the bearer authentication exists
    UserAuthEntity userAuthEntity =
        authenticationService.validateBearerAuthentication(
            accessToken,
            "to edit the answer"
        );
    UserEntity user = userAuthEntity.getUser();
    // Edit question
    AnswerEntity answerEntity =
        answerService.editAnswer(
            answerEditRequest.getContent(),
            user,
            answerId
        );
    AnswerEditResponse answerEditResponse =
        new AnswerEditResponse()
            .id(answerEntity.getUuid())
            .status("ANSWER EDITED");
    return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
  }


  /**
   * Get all the answers for a given Question.
   *
   * @param authorization access token
   * @param questionId    Question uuid
   * @return List AnswerDetailsResponse list
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws AuthenticationFailedException AuthenticationFailedException
   * @throws InvalidQuestionException      InvalidQuestionException
   */
  @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersForQuestion(
      @RequestHeader("authorization") final String authorization,
      @PathVariable("questionId") final String questionId
  ) throws AuthorizationFailedException, AuthenticationFailedException, InvalidQuestionException {

    //Get bearer access token
    String accessToken = authenticationService.getBearerAccessToken(authorization);

    //Validate bearer authentication token
    UserAuthEntity userAuthEntity = authenticationService
        .validateBearerAuthentication(accessToken, "to get all answers for the question");
    QuestionEntity questionEntity = answerService.getQuestionById(questionId);

    List<AnswerEntity> answerEntityList = answerService.getAllAnswersToQuestion(questionEntity);
    List<AnswerDetailsResponse> answerDetailsResponsesList = new ArrayList<>();
    for (AnswerEntity answerEntity : answerEntityList) {
      AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
      answerDetailsResponse.setId(answerEntity.getUuid());
      answerDetailsResponse.setAnswerContent(answerEntity.getAns());
      answerDetailsResponse.setQuestionContent(answerEntity.getQuestionEntity().getContent());
      answerDetailsResponsesList.add(answerDetailsResponse);
    }
    return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponsesList,
        HttpStatus.OK);
  }

  /**
   * Delete an answer.
   *
   * @param authorization access token
   * @param answerId      Answer uuid
   * @return AnswerDeleteResponse
   * @throws AuthenticationFailedException AuthenticationFailedException
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws AnswerNotFoundException       AnswerNotFoundException
   */
  @RequestMapping(method = RequestMethod.DELETE, path = "answer/delete/{answerId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
      @RequestHeader("authorization") final String authorization,
      @PathVariable("answerId") final String answerId)
      throws AuthenticationFailedException, AuthorizationFailedException, AnswerNotFoundException {
    String accessToken = authenticationService.getBearerAccessToken(authorization);
    UserAuthEntity userAuthEntity = authenticationService
        .validateBearerAuthentication(accessToken, "to delete the answer");
    UserEntity user = userAuthEntity.getUser();
    AnswerEntity answerEntity = answerService.deleteAnswer(user.getUuid(), answerId);
    AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse()
        .id(answerEntity.getUuid()).status("ANSWER DELETED");
    return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
  }
}