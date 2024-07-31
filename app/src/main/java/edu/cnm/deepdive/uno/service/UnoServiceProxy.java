package edu.cnm.deepdive.uno.service;

import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Game;
import edu.cnm.deepdive.uno.model.domain.User;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The {@code UnoServiceProxy} interface defines the API endpoints for interacting with the UNO web
 * game service.
 */
public interface UnoServiceProxy {

  /**
   * Creates a new game.
   * <p>
   * If a game exists on the web server matching the provided game parameters and is not yet at max
   * capacity, the server returns the existing game and add the user as a player rather than
   * creating a new game from scratch.
   * </p>
   *
   * @param game        the game object to be created.
   * @param bearerToken the user's authorization token.
   * @return a {@code Single<Game>} which emits the created Game.
   */
  @POST("games")
  Single<Game> createGame(@Body Game game, @Header("Authorization") String bearerToken);

  /**
   * Starts an existing game
   *
   * @param gameKey     the external key associated with the game to start.
   * @param bearerToken the user's authorization token.
   * @return a {@code Single<Game>} which emits the started Game.
   */
  @POST("games/{gameKey}/start")
  Single<Game> startGame(@Path("gameKey") String gameKey,
      @Header("Authorization") String bearerToken);

  /**
   * Submits a players move for validation to the UNO web service.
   *
   * @param gameKey     the external key associated with the game for which the move is being
   *                    submitted.
   * @param bearerToken the user's authorization token.
   * @param card        the Card being submitted as a player's move
   * @return a {@code Single<Game>} which emits the updated Game after the submitted move.
   */
  @POST("games/{gameKey}/moves")
  Single<Game> submitMove(@Path("gameKey") String gameKey,
      @Header("Authorization") String bearerToken,
      @Body Card card);

  /**
   * Submit a player's request to draw a Card from the UNO discard pile.
   *
   * @param gameKey     the external key associated with the game for which a card is being drawn
   *                    for.
   * @param bearerToken the user's authorization token.
   * @return a {@code Single<Game>} which emits the updated Game after the card is drawn.
   */
  @POST("games/{gameKey}/draw")
  Single<Game> drawCard(@Path("gameKey") String gameKey,
      @Header("Authorization") String bearerToken);

  /**
   * Gets the Game from the UNO web service associated with the provided external gameKey.
   *
   * @param gameKey the external key associated with the game being retrieved.
   * @return a {@code Single<Game>} which emits the Game associated with the external gameKey.
   */
  @GET("games/{gameKey}")
  Single<Game> getGame(@Path("gameKey") String gameKey,
      @Header("Authorization") String bearerToken);

  @GET("users/me")
  Single<User> getCurrentUser(@Header("Authorization") String bearerToken);

  // TODO: 7/30/24 Implement the PUT request to users/me

}
