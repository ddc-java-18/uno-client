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
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.adapter.HandAdapter;
import edu.cnm.deepdive.uno.databinding.FragmentGameBinding;
import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Card.Rank;
import edu.cnm.deepdive.uno.model.domain.Card.Suit;
import edu.cnm.deepdive.uno.model.domain.Game;
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


public class GameFragment extends Fragment {

  private static final String TAG = GameFragment.class.getSimpleName();

  private GameViewModel gameViewModel;
  private UserViewModel userViewModel;
  private LoginViewModel loginViewModel;
  private FragmentGameBinding binding;
  private RecyclerView recyclerView;
  private Map<Rank, Integer> rankDrawables;
  private Map<Suit, Integer> suitColors;

  private Game game;
  private User user;
  private Card selectedCard;

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
            showHand();
          }
        });

    userViewModel = new ViewModelProvider(context).get(UserViewModel.class);
    getLifecycle().addObserver(userViewModel);
    userViewModel.getUser()
        .observe(lifecycleOwner, (user) -> {
          if (user != null) {
            this.user = user;
            gameViewModel.pollForUpdates();
            showHand();
          }
        });

    binding.createGameBtn.setOnClickListener((v) -> gameViewModel.createGame());
    binding.getGameBtn.setOnClickListener((v) -> gameViewModel.getGame());
    binding.startGameBtn.setOnClickListener((v) -> gameViewModel.startGame());
    binding.drawCardBtn.setOnClickListener((v) -> gameViewModel.drawCard());
    binding.submitMoveBtn.setOnClickListener(
        (v) -> {
//          // TODO: 7/31/24 Get the card the user has selected.
//          Card testCard = new Card(Suit.BLUE, Rank.EIGHT);
//          testCard.setId("3494ab0e-1f03-4b33-bd54-e5e06dac61e1");
          gameViewModel.makeMove(selectedCard, user);
        }
    );

  }

  private void showHand() {
    if (game != null && user != null) {
      for (Hand hand : game.getHands()) {
        if (hand.getUser().getId().equals(user.getId())) {
          HandAdapter adapter =
              new HandAdapter(requireContext(), hand.getCards(), rankDrawables, suitColors,
                  (position, card) -> {
                    this.selectedCard = card;
                    // TODO: 8/1/24 : do something with the clicked card!!
                    Log.d(TAG, "Position: " + position + ", Rank: " + card.getRank() + ", Suit: " + card.getSuit());
                  });
          binding.recyclerViewHand.setAdapter(adapter);
          break;
        }
      }
    }
  }

  private void showTopDiscard() {
    if (game != null) {
      Card topDiscard = game.getTopDiscard();
      if (topDiscard != null) {
        int drawableId = rankDrawables.get(topDiscard.getRank());

        binding.discardTopCard.setImageResource(drawableId);
        binding.discardTopCard.setColorFilter(suitColors.get(topDiscard.getSuit()));
      } else {
        binding.discardTopCard.setImageResource(R.drawable.card_default);
      }
    }
  }

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