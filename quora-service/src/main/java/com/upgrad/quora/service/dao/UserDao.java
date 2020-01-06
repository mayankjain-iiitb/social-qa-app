package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {


  @PersistenceContext
  private EntityManager entityManager;

  /**create a user.
   * @param userEntity userEntity
   * @return userEntity
   */
  public UserEntity createUser(UserEntity userEntity) {
    entityManager.persist(userEntity);
    return userEntity;
  }

  /**Delete auser.
   * @param userEntity userEntity
   * @return userEntity
   */
  @OnDelete(action = OnDeleteAction.CASCADE)
  public UserEntity deleteUser(UserEntity userEntity) {
    entityManager.remove(userEntity);
    return userEntity;
  }

  /**Get User by email.
   * @param email email
   * @return userEntity
   */
  public UserEntity getUserByEmail(final String email) {
    try {
      return entityManager.createNamedQuery("userByEmail", UserEntity.class)
          .setParameter("email", email).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**Get user by uuid.
   * @param uuid uuid
   * @return userEntity
   */
  public UserEntity getuserbyuuid(final String uuid) {
    try {
      return entityManager.createNamedQuery("userByUUID", UserEntity.class)
          .setParameter("uuid", uuid).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**Get user by User name.
   * @param username username
   * @return userEntity
   */
  public UserEntity getUserByUsername(final String username) {
    try {
      return entityManager.createNamedQuery("userByUsername", UserEntity.class)
          .setParameter("username", username).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**create authentication access token.
   * @param userAuthEntity userAuthEntity
   * @return userAuthEntity
   */
  public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
    entityManager.persist(userAuthEntity);
    return userAuthEntity;
  }

  /**Get user by access token.
   * @param accessToken accessToken
   * @return userAuthEntity
   */
  public UserAuthEntity getUserByToken(final String accessToken) {
    try {
      return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class)
          .setParameter("accessToken", accessToken).getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**update User.
   * @param updatedUserEntity userEntity
   */
  public void updateUser(final UserEntity updatedUserEntity) {
    entityManager.merge(updatedUserEntity);
  }

  /**update UserAuthEntity.
   * @param updatedUserAuthEntity userAuthEntity
   */
  public void updateUserAuthEntity(final UserAuthEntity updatedUserAuthEntity) {
    entityManager.merge(updatedUserAuthEntity);
  }

}
