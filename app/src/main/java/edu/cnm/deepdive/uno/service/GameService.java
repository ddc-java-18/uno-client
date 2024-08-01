package edu.cnm.deepdive.uno.service;

import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Game;
import edu.cnm.deepdive.uno.model.domain.User;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameService {

  private final UnoServiceProxy proxy;
  private final UnoServiceLongProxy longProxy;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;

  @Inject
  public GameService(UnoServiceProxy proxy, UnoServiceLongProxy longProxy, GoogleSignInService signInService) {
    this.proxy = proxy;
    this.longProxy = longProxy;
    this.signInService = signInService;
    this.scheduler = Schedulers.single();
  }

  public Single<Game> createGame(int minPlayerCount, int maxPlayerCount) {
    return Single.fromSupplier(() -> new Game(minPlayerCount, maxPlayerCount))
        .flatMap((game) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.createGame(game, token)))
        .subscribeOn(scheduler);
  }

  public Single<Game> startGame(Game game) {
    return Single.fromSupplier(() -> game.getId())
        .flatMap((gameId) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.startGame(gameId, token)))
        .subscribeOn(scheduler);
  }

  public Single<Game> submitMove(Game game, Card card, User user) {
    // 1. I need to get the current user/player
    // 2. I need to validate that the card submitted by the player is valid
    // 3. Get the users bearer token
    return Single.fromSupplier(() -> game.validateMove(card))
        .flatMap((validCard) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.submitMove(game.getId(), token, validCard)))
        .subscribeOn(scheduler);
  }

  public Single<Game> drawCard(Game game) {
    // TODO: 8/1/24 Make sure that the user is allowed to draw. 
    return Single.fromSupplier(() -> game)
        .flatMap((g) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.drawCard(game.getId(), token)))
        .subscribeOn(scheduler);
  }

  public Single<Game> getGame(Game game) {
    return signInService.refreshToken()
        .flatMap((token) -> longProxy.getGame(game.getId(), token))
        .subscribeOn(scheduler);
  }

}
