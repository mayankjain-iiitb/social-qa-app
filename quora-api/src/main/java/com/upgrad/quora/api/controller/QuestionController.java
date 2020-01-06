package com.upgrad.quora.api.controller;
import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
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
public class QuestionController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private QuestionService questionService;

  /**
   * New question creation Api.
   *
   * @param authorization   access token
   * @param questionRequest QuestionRequest
   * @return QuestionResponse
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws AuthenticationFailedException AuthenticationFailedException
   * @throws InvalidQuestionException      InvalidQuestionException
   */
  @RequestMapping(method = RequestMethod.POST, path = "/question/create",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> postQuestion(@RequestHeader("authorization") final String authorization,
      final QuestionRequest questionRequest)
      throws AuthorizationFailedException, AuthenticationFailedException, InvalidQuestionException {

    //fetch token
    String accessToken = authenticationService.getBearerAccessToken(authorization);

    //Validate token
    UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to post a question");

    //fetch user details
    UserEntity user = userAuthEntity.getUser();
    QuestionEntity questionEntity = new QuestionEntity();
	
    questionEntity.setUuid(UUID.randomUUID().toString());
    questionEntity.setContent(questionRequest.getContent());
    questionEntity.setDate(ZonedDateTime.now());
    questionEntity.setUser(user);

    //question creation by checking existence
    questionService.createQuestion(questionEntity);
	
    QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid()).status("QUESTION CREATED");
    
	return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
  }

  /**
   * Api to edit a question.
   *
   * @param authorization       access token
   * @param questionId          question uuid
   * @param questionEditRequest QuestionEditRequest
   * @return QuestionEditResponse
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws InvalidQuestionException      InvalidQuestionException
   * @throws AuthenticationFailedException AuthenticationFailedException
   */
  @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionEditResponse> editQuestion(@RequestHeader("authorization") final String authorization,
      @PathVariable("questionId") final String questionId, final QuestionEditRequest questionEditRequest)
      throws AuthorizationFailedException, InvalidQuestionException, AuthenticationFailedException {

    String accessToken = authenticationService.getBearerAccessToken(authorization);

    UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to edit the question");
    UserEntity user = userAuthEntity.getUser();

    QuestionEntity questionEntity = questionService.editQuestion(questionEditRequest.getContent(), user.getUuid(), questionId);
    
	QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");
    
	return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
  }

  /**
   * Api to fetch Questions.
   *
   * @param authorization access token
   * @return List of QuestionDetailsResponse
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws AuthenticationFailedException AuthenticationFailedException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/question/all",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization)
      throws AuthorizationFailedException, AuthenticationFailedException {

    String accessToken = authenticationService.getBearerAccessToken(authorization);

    UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to get all questions");

    List<QuestionEntity> questionEntityList = questionService.getAllQuestions();

    return getListResponseEntity(questionEntityList);
  }

  /**
   * User related questions.
   *
   * @param authorization access token
   * @param userId        user uuid
   * @return List of QuestionDetailsResponse
   * @throws AuthenticationFailedException AuthenticationFailedException
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws UserNotFoundException         UserNotFoundException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization,
      @PathVariable("userId") final String userId)
      throws AuthenticationFailedException, AuthorizationFailedException, UserNotFoundException {
		  
    String accessToken = authenticationService.getBearerAccessToken(authorization);
	
    UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to get all questions by user");
    UserEntity user = userAuthEntity.getUser();
    
	List<QuestionEntity> questionEntityList = questionService.getAllQuestionsByUser(userId);
    
	return getListResponseEntity(questionEntityList);
  }

  /**
   * Method to create QuestionDetailsResponse List.
   *
   * @param questionEntityList QuestionEntity List
   * @return List of QuestionDetailsResponse
   */
  private ResponseEntity<List<QuestionDetailsResponse>> getListResponseEntity(
      List<QuestionEntity> questionEntityList) {
    List<QuestionDetailsResponse> qdrList = new ArrayList<QuestionDetailsResponse>();
	
    for (QuestionEntity qe : questionEntityList) {
      QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
      questionDetailsResponse.id(qe.getUuid());
      questionDetailsResponse.content(qe.getContent());
	  
      qdrList.add(questionDetailsResponse);
    }

    return new ResponseEntity<List<QuestionDetailsResponse>>(qdrList, HttpStatus.OK);
  }

  /**
   * Api to delete a question.
   *
   * @param authorization access token
   * @param questionId    question uuid
   * @return QuestionDeleteResponse
   * @throws AuthenticationFailedException AuthenticationFailedException
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws InvalidQuestionException      InvalidQuestionException
   */
  @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization,
      @PathVariable("questionId") final String questionId)
      throws AuthenticationFailedException, AuthorizationFailedException, InvalidQuestionException {
		  
    String accessToken = authenticationService.getBearerAccessToken(authorization);
    UserAuthEntity userAuthEntity = authenticationService.validateBearerAuthentication(accessToken, "to delete the question");
    UserEntity user = userAuthEntity.getUser();
	
    QuestionEntity questionEntity = questionService.deleteQuestion(user, questionId);
    
	QuestionDeleteResponse deleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid()).status("QUESTION DELETED");
    
	return new ResponseEntity<QuestionDeleteResponse>(deleteResponse, HttpStatus.OK);
  }
}
