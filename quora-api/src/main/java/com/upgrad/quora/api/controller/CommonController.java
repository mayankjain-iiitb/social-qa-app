package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
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
public class CommonController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private UserService userService;

  /**
   * Get a user profile.
   *
   * @param userId        user uuid
   * @param authorization access token
   * @return UserDetailsResponse
   * @throws UserNotFoundException         UserNotFoundException
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws AuthenticationFailedException AuthenticationFailedException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserDetailsResponse> userProfile(
      @PathVariable("userId") final String userId,
      @RequestHeader("authorization") final String authorization)
      throws UserNotFoundException, AuthorizationFailedException, AuthenticationFailedException {

    //Get the accessToken from RequestHeader by splitting it appropriately
    String accessToken = authenticationService.getBearerAccessToken(authorization);

    //Validate the bearer authentication providing context as "to get user details"
    authenticationService.validateBearerAuthentication(accessToken, "to get user details");

    //Invoke business service to search for the user in the database, throw UserNotFoundException
    // if no user is found
    UserEntity userEntity = userService.getUserProfile(userId);

    //If user found, fill ResponseEntity and return userDetailsResponse
    UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
    userDetailsResponse.setUserName(userEntity.getUsername());
    userDetailsResponse.setAboutMe(userEntity.getAboutme());
    userDetailsResponse.setContactNumber(userEntity.getContactnumber());
    userDetailsResponse.setCountry(userEntity.getCountry());
    userDetailsResponse.setDob(userEntity.getDob());
    userDetailsResponse.setEmailAddress(userEntity.getEmail());
    userDetailsResponse.setFirstName(userEntity.getFirstName());
    userDetailsResponse.setLastName(userEntity.getLastName());
    return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
  }

}
