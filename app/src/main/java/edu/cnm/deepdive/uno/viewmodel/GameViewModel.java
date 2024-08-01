package edu.cnm.deepdive.uno.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Game;
import edu.cnm.deepdive.uno.model.domain.Hand;
import edu.cnm.deepdive.uno.model.domain.User;
import edu.cnm.deepdive.uno.service.GameService;
import edu.cnm.deepdive.uno.service.UserRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class GameViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = GameViewModel.class.getSimpleName();

  private final GameService gameService;
  private final UserRepository userRepository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<User> user;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  public GameViewModel(@ApplicationContext Context context, GameService gameService,
      UserRepository userRepository) {
    this.userRepository = userRepository;
    this.gameService = gameService;
    game = new MutableLiveData<>(null);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    user = new MutableLiveData<>(null);
    pollForUpdates();
    userRepository.getCurrentUser()
        .subscribe(user::postValue);
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<User> getUser() {
    return user;
  }

  public void createGame() {
    // TODO: 7/30/24 Get the min and max players from settings/preferences
    gameService
        .createGame(2, 6)
        .subscribe(
            game::postValue,
            this::postThrowable,
            pending
        );
  }

  public void startGame() {
    gameService
        .startGame(game.getValue())
        .subscribe(
            game::postValue,
            this::postThrowable,
            pending
        );
  }

  public void makeMove(Card card, User user) {
    Game game = this.game.getValue();
    gameService.submitMove(game, card, user)
        .subscribe(
            this.game::postValue,
            this::postThrowable,
            pending
        );
  }

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
