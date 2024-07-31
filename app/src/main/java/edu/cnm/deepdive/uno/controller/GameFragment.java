package edu.cnm.deepdive.uno.controller;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.FragmentGameBinding;
import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Card.Rank;
import edu.cnm.deepdive.uno.model.domain.Card.Suit;
import edu.cnm.deepdive.uno.model.domain.Game;
import edu.cnm.deepdive.uno.model.domain.User;
import edu.cnm.deepdive.uno.viewmodel.GameViewModel;
import edu.cnm.deepdive.uno.viewmodel.LoginViewModel;
import edu.cnm.deepdive.uno.viewmodel.UserViewModel;


public class GameFragment extends Fragment {

  private GameViewModel gameViewModel;
  private UserViewModel userViewModel;
  private LoginViewModel loginViewModel;
  private FragmentGameBinding binding;

  private Game game;
  private User user;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
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
          // TODO: 7/30/24   update display based on game
          if (game != null) {
            this.game = game;
          }
        });

    userViewModel = new ViewModelProvider(context).get(UserViewModel.class);
    getLifecycle().addObserver(userViewModel);
    userViewModel.getUser()
        .observe(lifecycleOwner, (user) -> {
          this.user = user;
        });

    binding.createGameBtn.setOnClickListener((v) -> gameViewModel.createGame());
    binding.getGameBtn.setOnClickListener((v) -> gameViewModel.getGame());
    binding.startGameBtn.setOnClickListener((v) -> gameViewModel.startGame());
    binding.submitMoveBtn.setOnClickListener(
        (v) -> {
          // TODO: 7/31/24 Get the card the user has selected.
          Card testCard = new Card(Suit.BLUE, Rank.EIGHT);
          testCard.setId("3494ab0e-1f03-4b33-bd54-e5e06dac61e1");
          gameViewModel.makeMove(testCard, user);
        }
    );

  }
}