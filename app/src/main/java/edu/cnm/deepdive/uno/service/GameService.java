package edu.cnm.deepdive.uno.service;

import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Game;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manages the different game operations that can happen in a game of UNO.
 *
 * <p>The service class handles creating new games, starting existing games, retrieving game state,
 * and handling player actions. It uses {@link UnoServiceProxy} and {@link UnoServiceLongProxy} for
 * communicating with the webserver and {@link GoogleSignInService} for authentication.</p>
 */
@Singleton
public class GameService {

  private final UnoServiceProxy proxy;
  private final UnoServiceLongProxy longProxy;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;

  /**
   * Constructs an instance of the GameService using the provided proxies and GoogleSignInService.
   *
   * @param proxy         The proxy used to send game operation requests to application's web service.
   * @param longProxy     The proxy used to send long-polling request to the application's web service for game
   *                      updates.
   * @param signInService Google sign-in service used to authenticate users.
   */
  @Inject
  public GameService(UnoServiceProxy proxy, UnoServiceLongProxy longProxy,
      GoogleSignInService signInService) {
    this.proxy = proxy;
    this.longProxy = longProxy;
    this.signInService = signInService;
    this.scheduler = Schedulers.single();
  }

  /**
   * Creates a new game of UNO with the specified min and max player count.
   *
   * @param minPlayerCount int representing the minimum number of players that need to join the UNO
   *                       game for it to start.
   * @param maxPlayerCount int representing the maximum number of players that can join the UNO
   *                       game.
   * @return An RxJava Single of a Game.
   */
  public Single<Game> createGame(int minPlayerCount, int maxPlayerCount) {
    return Single.fromSupplier(() -> new Game(minPlayerCount, maxPlayerCount))
        .flatMap((game) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.createGame(game, token)))
        .subscribeOn(scheduler);
  }

  /**
   * Starts an existing game of UNO.
   *
   * @param game The game to be started.
   * @return An RxJava Single of the Game which is started.
   */
  public Single<Game> startGame(Game game) {
    return Single.fromSupplier(() -> game.getId())
        .flatMap((gameId) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.startGame(gameId, token)))
        .subscribeOn(scheduler);
  }

  /**
   * Submits a player's move in a Game of UNO.
   *
   * @param game The game which the move is being submitted to.
   * @param card The card which is being submitted as a player's move.
   * @return An RxJava Single of the Game which is getting a move submitted to.
   */
  public Single<Game> submitMove(Game game, Card card) {
    return Single.fromSupplier(() -> card)
        .flatMap((c) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.submitMove(game.getId(), token, c)))
        .subscribeOn(scheduler);
  }

  /**
   * Draws a card for a user.
   *
   * @param game The game which the user is drawing a card for.
   * @return An RxJava Single of the Game which gets updated after the user's draw.
   */
  public Single<Game> drawCard(Game game) {
    return Single.fromSupplier(() -> game)
        .flatMap((g) -> signInService
            .refreshToken()
            .flatMap((token) -> proxy.drawCard(game.getId(), token)))
        .subscribeOn(scheduler);
  }

  /**
   * Retrieves the current state of the specified game.
   *
   * @param game The game to retrieve.
   * @return An RxJava Single of the Game which was retrieved.
   */
  public Single<Game> getGame(Game game) {
    return signInService.refreshToken()
        .flatMap((token) -> longProxy.getGame(game.getId(), token))
        .subscribeOn(scheduler);
  }

}
