package edu.cnm.deepdive.uno.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Game;
import edu.cnm.deepdive.uno.model.domain.Hand;
import edu.cnm.deepdive.uno.model.domain.User;
import edu.cnm.deepdive.uno.service.GameService;
import edu.cnm.deepdive.uno.service.UserRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

/**
 * ViewModel for managing game state and player operations/actions in the Uno application.
 */
@HiltViewModel
public class GameViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = GameViewModel.class.getSimpleName();

  private final GameService gameService;
  private final UserRepository userRepository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<User> user;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final MutableLiveData<Card> selectedCard;
  private final SharedPreferences prefs;
  private final String maxPlayerPrefKey;
  private final int maxPlayerPrefDefault;

  /**
   * Constructor for GameViewModel.
   *
   * @param context        The application context.
   * @param gameService    The service used to handle game operations.
   * @param userRepository The repository used for managing user data.
   */
  @Inject
  public GameViewModel(@ApplicationContext Context context, GameService gameService,
      UserRepository userRepository) {
    this.userRepository = userRepository;
    this.gameService = gameService;
    game = new MutableLiveData<>(null);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    user = new MutableLiveData<>(null);
    selectedCard = new MutableLiveData<>();
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
    maxPlayerPrefKey = context.getString(R.string.player_max_pref_key);
    maxPlayerPrefDefault = context.getResources().getInteger(R.integer.max_player_pref_default);
    pollForUpdates();
    userRepository.getCurrentUser()
        .subscribe(user::postValue);
  }

  /**
   * Returns LiveData containing the current Game (state the game is currently in).
   *
   * @return LiveData containing the current game.
   */
  public LiveData<Game> getGame() {
    return game;
  }

  /**
   * Returns LiveData containing the current logged-in user.
   *
   * @return LiveData containing the current logged-in user
   */
  public LiveData<User> getUser() {
    return user;
  }

  /**
   * Returns LiveData containing the currently selected card by the user.
   *
   * @return LiveData containing the currently selected card by the user.
   */
  public LiveData<Card> getSelectedCard() {
    return selectedCard;
  }

  /**
   * Posts the provided {@link Card} object to LiveData.
   *
   * @param card the {@link Card} being posted to LiveData.
   */
  public void setSelectedCard(Card card) {
    selectedCard.postValue(card);
  }

  /**
   * Creates a new UNO game using min and max player values from setting shared preferences.
   */
  public void createGame() {
    // TODO: 7/30/24 Get the min and max players from settings/preferences
    int maxPlayer = prefs.getInt(maxPlayerPrefKey, maxPlayerPrefDefault);
    gameService
        .createGame(2, maxPlayer)
        .subscribe(
            game::postValue,
            this::postThrowable,
            pending
        );
  }

  /**
   * Starts an existing UNO game from the server.
   */
  public void startGame() {
    gameService
        .startGame(game.getValue())
        .subscribe(
            game::postValue,
            this::postThrowable,
            pending
        );
  }

  /**
   * Submits an UNO move (plays a card) for a player.
   *
   * @param card The UNO card being submitted for game play.
   */
  public void makeMove(Card card) {
    Game game = this.game.getValue();
    gameService.submitMove(game, card)
        .subscribe(
            (g) -> {
              this.game.postValue(g);
              pollForUpdates();
            },
            this::postThrowable,
            pending
        );
  }

  /**
   * Draws a card for a player.
   */
  public void drawCard() {
    Game game = this.game.getValue();
    gameService.drawCard(game)
        .subscribe(
            (g) -> {
              this.game.postValue(g);
              pollForUpdates();
            },
            this::postThrowable,
            pending
        );
  }

  /**
   * Polls the UNO webservice for Game updates.
   */
  public void pollForUpdates() {
    User currentUser = user.getValue();
    Game currentGame = game.getValue();
    if (currentUser != null && currentGame != null) {
      gameService.getGame(currentGame)
          .subscribe(
              (g) -> {
                if (isCurrentPlayersTurn(g, currentUser)) {
                  game.postValue(g);
                } else {
                  if (!currentGame.getLastUpdatedOn().equals(g.getLastUpdatedOn())) {
                    game.postValue(g);
                  }
                  pollForUpdates();
                }
              },
              this::postThrowable,
              pending
          );
    }
  }

  private boolean isCurrentPlayersTurn(Game g, User currentUser) {
    boolean isTurn = false;
    List<Hand> hands = g.getHands();
    for (Hand hand : hands) {
      if (hand.getUser().getId().equals(currentUser.getId())) {
        isTurn = hand.isTurn();
      }
    }
    return isTurn;
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
