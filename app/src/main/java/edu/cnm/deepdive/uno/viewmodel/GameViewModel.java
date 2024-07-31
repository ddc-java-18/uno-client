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
import edu.cnm.deepdive.uno.model.domain.User;
import edu.cnm.deepdive.uno.service.GameService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;

@HiltViewModel
public class GameViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = GameViewModel.class.getSimpleName();

  private final GameService gameService;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  @Inject
  public GameViewModel(@ApplicationContext Context context, GameService gameService) {
    this.gameService = gameService;
    game = new MutableLiveData<>(null);
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();

  }

  public LiveData<Game> getGame() {
    return game;
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

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
