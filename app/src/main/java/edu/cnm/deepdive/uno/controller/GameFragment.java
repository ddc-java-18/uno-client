package edu.cnm.deepdive.uno.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.adapter.HandAdapter;
import edu.cnm.deepdive.uno.adapter.UsersAdapter;
import edu.cnm.deepdive.uno.databinding.FragmentGameBinding;
import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Card.Rank;
import edu.cnm.deepdive.uno.model.domain.Card.Suit;
import edu.cnm.deepdive.uno.model.domain.Game;
import edu.cnm.deepdive.uno.model.domain.Game.GameState;
import edu.cnm.deepdive.uno.model.domain.Game.MoveState;
import edu.cnm.deepdive.uno.model.domain.Hand;
import edu.cnm.deepdive.uno.model.domain.User;
import edu.cnm.deepdive.uno.viewmodel.GameViewModel;
import edu.cnm.deepdive.uno.viewmodel.LoginViewModel;
import edu.cnm.deepdive.uno.viewmodel.UserViewModel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link Fragment} subclass used to display and manage the application Game screen.
 */
public class GameFragment extends Fragment {

  private static final String TAG = GameFragment.class.getSimpleName();

  private GameViewModel gameViewModel;
  private UserViewModel userViewModel;
  private FragmentGameBinding binding;
  private Map<Rank, Integer> rankDrawables;
  private Map<Suit, Integer> suitColors;
  private Game game;
  private User user;
  private Card selectedCard;
  private HandAdapter adapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    rankDrawables = getRankDrawables();
    suitColors = getSuitColors();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentGameBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity context = requireActivity();

    gameViewModel = new ViewModelProvider(context).get(GameViewModel.class);
    getLifecycle().addObserver(gameViewModel);
    LifecycleOwner lifecycleOwner = getViewLifecycleOwner();

    gameViewModel.getGame()
        .observe(lifecycleOwner, (game) -> {
          if (game != null) {
            this.game = game;
            showTopDiscard();
            showUsers();
            if (game.getGameState() == GameState.COMPLETED) {
              Snackbar.make(
                  binding
                      .getRoot(), getString(R.string.game_over_title) + game
                      .getWinner()
                      .getDisplayName()
                      + getString(R.string.game_over_winner_name), Snackbar.LENGTH_LONG).show();
            }
          }
          showHand();
        });

    userViewModel = new ViewModelProvider(context).get(UserViewModel.class);
    getLifecycle().addObserver(userViewModel);
    userViewModel.getUser()
        .observe(lifecycleOwner, (user) -> {
          if (user != null) {
            this.user = user;
            gameViewModel.pollForUpdates(); // TODO: 8/6/24 Check to see if polling is necessary
            gameViewModel.setSelectedCard(null);
          }
          showHand();
        });

    gameViewModel
        .getSelectedCard()
        .observe(lifecycleOwner, (card) -> {
          this.selectedCard = card;
          if (card != null) {
            // TODO: 8/2/24 Enable control to play selected card.
          } else {
            // TODO: 8/2/24 Disable controls to play selected card.
          }
        });

    binding.startGameBtn.setOnClickListener((v) -> gameViewModel.startGame());
    binding.drawCardBtn.setOnClickListener((v) -> gameViewModel.drawCard());
    binding.submitMoveBtn.setOnClickListener((v) -> submitMove(view));

  }

  /**
   * Helper method used to submit a player move to the server. If, the user is trying to submit an
   * invalid move, rather than sending the request to the server, a toast with an error message is
   * displayed on the screen notifying the user.
   *
   * @param view of the GameFragment.
   */
  private void submitMove(View view) {
    MoveState moveState = game.validateMove(selectedCard, user);
    String message = switch (moveState) {
      case VALID -> "Your move has been submitted.";
      case OUT_OF_TURN -> "Invalid Move: It is not your turn.";
      case INVALID_MOVE -> "Invalid Move: The card you tried to submit isn't allowed.";
      case INVALID_CARD ->
          "Invalid Move: You are trying to submit a card that is not in your hand.";
    };
    if (moveState == MoveState.VALID) {
      gameViewModel.makeMove(selectedCard);
      // TODO: 8/2/24 Toast should only display if the move is actually submitted successfully on the serverside.
      Snackbar.make(view, message, Snackbar.LENGTH_LONG)
          .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.success_green))
          .show();
    } else {
      Snackbar.make(view, message, Snackbar.LENGTH_LONG)
          .setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.error_red))
          .show();
    }
  }

  /**
   * Helper method used to render a player's hand on the screen. This method, renders a player's
   * hand utilizing and instance of HandAdapter.
   */
  private void showHand() {
    if (game != null && user != null) {
      for (Hand hand : game.getHands()) {
        if (hand.getUser().getId().equals(user.getId())) {
          adapter = new HandAdapter(requireContext(), hand.getCards(), rankDrawables, suitColors,
              (position, card) -> {
                if (selectedCard != null) {
                  selectedCard.setSelectedByUser(false);
                }
                card.setSelectedByUser(true);
                gameViewModel.setSelectedCard(card);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Position: " + position + ", Rank: " + card.getRank() + ", Suit: "
                    + card.getSuit());
              });
          binding.recyclerViewHand.setAdapter(adapter);
          break;
        }
      }
    }
  }

  /**
   * Helper method used to render the current players who are a part of an UNO game.
   */
  private void showUsers() {
    if (game != null && user != null) {
      UsersAdapter adapter = new UsersAdapter(requireContext(), game.getHands());
      binding.recyclerViewUsers.setAdapter(adapter);
    }
  }

  /**
   * Helper method used to render the top discard card in a game of UNO. If there is no current top
   * discard, then a default card image is rendered.
   *
   * @noinspection DataFlowIssue
   */
  private void showTopDiscard() {
    if (game != null) {
      Card topDiscard = game.getTopDiscard();
      if (topDiscard != null) {
        int drawableId = rankDrawables.get(topDiscard.getRank());

        binding.discardTopCard.setImageResource(drawableId);
        // If top discard card is will we get a null pointer error.
        if (topDiscard.getSuit() != null) {
          binding.discardTopCard.setColorFilter(suitColors.get(topDiscard.getSuit()));
        }
      } else {
        binding.discardTopCard.setImageResource(R.drawable.card_default);
      }
    }
  }

  /**
   * Helper method used to map a {@code Rank} to a drawable resource representing the rank.
   *
   * @return Map whose key is a {@code Rank} and value is a corresponding drawable resource.
   */
  private Map<Rank, Integer> getRankDrawables() {
    Context context = requireContext();
    Resources resources = context.getResources();
    Map<Rank, Integer> cardGraphics = new HashMap<>();
    for (Rank rank : Rank.values()) {
      String name = "card_" + rank.name().toLowerCase();
      @SuppressLint("DiscouragedApi")
      int id = resources.getIdentifier(name, "drawable", context.getPackageName());
      cardGraphics.put(rank, id);
    }
    return cardGraphics;
  }

  /**
   * Helper method used to map a {@code Suit} to a color resource.
   *
   * @return Map whose key is a {@code Suit} and value is a corresponding color resource.
   */
  private Map<Suit, Integer> getSuitColors() {
    Context context = requireContext();
    Resources resources = context.getResources();
    return Arrays.stream(Suit.values())
        .collect(Collectors.toMap(
            Function.identity(),
            (suit) -> {
              String name = "card_" + suit.name().toLowerCase();
              @SuppressLint("DiscouragedApi")
              int id = resources.getIdentifier(name, "color", context.getPackageName());
              return ContextCompat.getColor(context, id);
            }
        ));
  }
}
