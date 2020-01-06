package com.upgrad.quora.api.controller;
import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AdminController {

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationService authenticationService;

  /**
   * Api to delete user.
   *
   * @param userId user uuid
   * @param authorization access token
   * @return UserDeleteResponse
   * @throws UserNotFoundException         UserNotFoundException
   * @throws AuthorizationFailedException  AuthorizationFailedException
   * @throws AuthenticationFailedException AuthenticationFailedException
   */
  @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userId,
             @RequestHeader("authorization") final String authorization)
      throws UserNotFoundException, AuthorizationFailedException, AuthenticationFailedException {

    //get jwtToken
    String accessToken = authenticationService.getBearerAccessToken(authorization);

    //validating token
    UserAuthEntity userAuthEntity = authenticationService
        .validateBearerAuthentication(accessToken, "to get user details");

    //check if user exists & delete if an admin. Don't allow to delete self.
    UserEntity userEntity = userService.deleteUser(userId, userAuthEntity);

    UserDeleteResponse userDeleteResponse = new UserDeleteResponse()
	    .id(userEntity.getUuid())
        .status("USER SUCCESSFULLY DELETED");
    
	return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
  }
}
