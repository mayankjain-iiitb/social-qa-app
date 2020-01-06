package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

  /**
   * SignUpRestrictedException handler.
   *
   * @param exc     SignUpRestrictedException
   * @param request WebRequest
   * @return ErrorResponse
   */
  @ExceptionHandler(SignUpRestrictedException.class)
  public ResponseEntity<ErrorResponse> signupRestrictionException(SignUpRestrictedException exc,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.CONFLICT
    );
  }

  /**
   * AuthenticationFailedException handler.
   *
   * @param exc     AuthenticationFailedException
   * @param request WebRequest
   * @return ErrorResponse
   */
  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ErrorResponse> authenticationFailedException(
      AuthenticationFailedException exc, WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
        HttpStatus.UNAUTHORIZED
    );
  }

  /**
   * SignOutRestrictedException handler.
   *
   * @param exc     SignOutRestrictedException
   * @param request WebRequest
   * @return ErrorResponse
   */
  @ExceptionHandler(SignOutRestrictedException.class)
  public ResponseEntity<ErrorResponse> signoutRestrictedException(SignOutRestrictedException exc,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
        HttpStatus.UNAUTHORIZED
    );
  }

  /**
   * InvalidQuestionException handler.
   *
   * @param exc     InvalidQuestionException
   * @param request WebRequest
   * @return ErrorResponse
   */
  @ExceptionHandler(InvalidQuestionException.class)
  public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exc,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
    );
  }

  /**
   * UserNotFoundException handler.
   *
   * @param exc     UserNotFoundException
   * @param request WebRequest
   * @return ErrorResponse
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exc,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
    );
  }

  /**
   * AuthorizationFailedException handler.
   *
   * @param exc     AuthorizationFailedException
   * @param request WebRequest
   * @return ErrorResponse
   */
  @ExceptionHandler(AuthorizationFailedException.class)
  public ResponseEntity<ErrorResponse> authorizationFailedException(
      AuthorizationFailedException exc, WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.FORBIDDEN
    );
  }

  /**
   * AnswerNotFoundException handler.
   *
   * @param exc     AnswerNotFoundException
   * @param request WebRequest
   * @return ErrorResponse
   */
  @ExceptionHandler(AnswerNotFoundException.class)
  public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException exc,
      WebRequest request) {
    return new ResponseEntity<ErrorResponse>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.NOT_FOUND
    );
  }

}