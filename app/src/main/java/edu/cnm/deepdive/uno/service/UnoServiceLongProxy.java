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
public interface UnoServiceLongProxy {

  /**
   * Gets the Game from the UNO web service associated with the provided external gameKey.
   *
   * @param gameKey the external key associated with the game being retrieved.
   * @return a {@code Single<Game>} which emits the Game associated with the external gameKey.
   */
  @GET("games/{gameKey}")
  Single<Game> getGame(@Path("gameKey") String gameKey,
      @Header("Authorization") String bearerToken);

}
