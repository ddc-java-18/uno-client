package edu.cnm.deepdive.uno.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.databinding.FragmentLobbyBinding;
import edu.cnm.deepdive.uno.viewmodel.GameViewModel;

/**
 * A {@link Fragment} subclass used to display the Lobby "screen" that is displayed when a user
 * is about to join/create a new game of UNO.
 */
@AndroidEntryPoint
public class LobbyFragment extends Fragment {

  private FragmentLobbyBinding binding;
  private GameViewModel gameViewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentLobbyBinding.inflate(inflater, container, false);
    binding.startGameBtn.setOnClickListener((v) -> {
      gameViewModel.createGame();
    });
    binding.gameSettingsBtn.setOnClickListener((v) -> {
      Intent intent = new Intent(getContext(), SettingsActivity.class);
      startActivity(intent);
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViewModel();
  }

  @Override
  public void onDestroy() {
    binding = null;
    super.onDestroy();
  }

  private void setupViewModel() {
    ViewModelProvider provider = new ViewModelProvider(requireActivity());
    gameViewModel = provider.get(GameViewModel.class);
    gameViewModel.getGame().observe(getViewLifecycleOwner(), (game) -> {
      if (game != null) {
        Navigation.findNavController(binding.getRoot())
            .navigate(LobbyFragmentDirections.navigateToGameFragment());
      }
    });
  }

}
