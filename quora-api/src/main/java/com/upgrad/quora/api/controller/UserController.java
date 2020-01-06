package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationService authenticationService;

  /**
   * Handler to signup for any prospective user to get registered.
   *
   * @param signupUserRequest SignupUserRequest
   * @return SingupUserResponse
   * @throws SignUpRestrictedException SignUpRestrictedException
   */
  @RequestMapping(method = RequestMethod.POST, path = "/user/signup",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupUserResponse> userSignup(final SignupUserRequest signupUserRequest)
      throws SignUpRestrictedException {

    //Fetch details from signupUserRequest and set in UserEntity instance
    final UserEntity userEntity = new UserEntity();
    userEntity.setUuid(UUID.randomUUID().toString());
    userEntity.setFirstName(signupUserRequest.getFirstName());
    userEntity.setLastName(signupUserRequest.getLastName());
    userEntity.setUsername(signupUserRequest.getUserName());
    userEntity.setEmail(signupUserRequest.getEmailAddress());
    userEntity.setPassword(signupUserRequest.getPassword());
    userEntity.setContactnumber(signupUserRequest.getContactNumber());
    userEntity.setAboutme(signupUserRequest.getAboutMe());
    userEntity.setCountry(signupUserRequest.getCountry());
    userEntity.setDob(signupUserRequest.getDob());
    userEntity.setSalt("1234abc"); // will get overwritten in service class
    userEntity.setRole("nonadmin"); // by default every user will be nonadmin.
    // will be admin only those who had pgadmin access

    //Invoke business Service to signup & return SignupUserResponse
    final UserEntity createdUserEntity = userService.signup(userEntity);
    SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid())
        .status("USER SUCCESSFULLY REGISTERED");
    return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
  }

  /**
   * Handler to login to social quora.
   *
   * @param authorization access token
   * @return SigninResponse
   * @throws AuthenticationFailedException AuthenticationFailedException
   */
  @RequestMapping(method = RequestMethod.POST, path = "/user/signin",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SigninResponse> login(
      @RequestHeader("authorization") final String authorization)
      throws AuthenticationFailedException {

    //Split authorization header to get username and password
    byte[] decode = null;
    String[] tokens = authorization.split("Basic ");

    try {
      decode = Base64.getDecoder().decode(tokens[1]);
    } catch (IllegalArgumentException ile) {
      throw new AuthenticationFailedException("ATH-004",
          "Unable to decode. Malformed authorization.");
    } catch (IndexOutOfBoundsException ie) {
      throw new AuthenticationFailedException("ATH-005",
          "Use format 'Basic (Base64decoded)Username:password'");
    }
    String decodedText = new String(decode);
    String[] decodedArray = decodedText.split(":");

    //Invoke Authentication Service
    UserAuthEntity userAuthEntity = authenticationService
        .authenticate(decodedArray[0], decodedArray[1]);

    //Get User details
    UserEntity user = userAuthEntity.getUser();

    //Fill SigninResponse and return
    SigninResponse signinResponse = new SigninResponse().id(user.getUuid())
        .message("SIGNED IN SUCCESSFULLY");
    HttpHeaders headers = new HttpHeaders();
    headers.add("access-token", userAuthEntity.getAccessToken());
    return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);
  }

  /**
   * Handler to signout.
   *
   * @param authorization access token
   * @return SignoutResponse
   * @throws SignOutRestrictedException    SignOutRestrictedException
   * @throws AuthenticationFailedException AuthenticationFailedException
   */
  @RequestMapping(method = RequestMethod.POST, path = "/user/signout",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignoutResponse> logout(
      @RequestHeader("authorization") final String authorization)
      throws SignOutRestrictedException, AuthenticationFailedException {

    //Get access token from authorization header
    String jwtToken = authenticationService.getBearerAccessToken(authorization);

    //Invoke business service to logoff
    UserAuthEntity userAuthEntity = authenticationService.logoff(jwtToken);
    //Get User details who had logged off
    UserEntity user = userAuthEntity.getUser();

    //Fill in Signout Response and return
    SignoutResponse signoutResponse = new SignoutResponse().id(user.getUuid())
        .message("SIGNED OUT SUCCESSFULLY");
    HttpHeaders headers = new HttpHeaders();
    headers.add("access-token", userAuthEntity.getAccessToken());
    return new ResponseEntity<SignoutResponse>(signoutResponse, headers, HttpStatus.OK);
  }
}
