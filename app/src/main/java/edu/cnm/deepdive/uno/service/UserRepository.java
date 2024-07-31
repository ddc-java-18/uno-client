package edu.cnm.deepdive.uno.service;

import edu.cnm.deepdive.uno.model.domain.User;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

public class UserRepository {

  private final GoogleSignInService signInService;
  private final UnoServiceProxy proxy;

  @Inject
  public UserRepository(GoogleSignInService signInService, UnoServiceProxy proxy) {
    this.signInService = signInService;
    this.proxy = proxy;
  }

  public Single<User> getCurrentUser() {
    return signInService.refreshToken()
        .flatMap(proxy::getCurrentUser)
        .subscribeOn(Schedulers.io());
  }

}
