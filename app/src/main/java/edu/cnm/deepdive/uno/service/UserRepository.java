package edu.cnm.deepdive.uno.service;

import edu.cnm.deepdive.uno.model.domain.User;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

/**
 *  Repository class for getting User information in the application. It calls the configured
 *  GoogleSignInService which provides the relevant user data.
 */
public class UserRepository {

  private final GoogleSignInService signInService;
  private final UnoServiceProxy proxy;

  /**
   * Constructs and instance of the UserRepository.
   *
   * @param signInService GoogleSignInService used to retrieve user details
   * @param proxy The proxy used to send webservice requests to get user details.
   */
  @Inject
  public UserRepository(GoogleSignInService signInService, UnoServiceProxy proxy) {
    this.signInService = signInService;
    this.proxy = proxy;
  }

  /**
   * Returns the User associated with the saved bearer token in the GoogleSignInService.
   *
   * @return RxJava Single, which emits the current User logged on.
   */
  public Single<User> getCurrentUser() {
    return signInService.refreshToken()
        .flatMap(proxy::getCurrentUser)
        .subscribeOn(Schedulers.io());
  }

}
