package edu.cnm.deepdive.uno.controller;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.FragmentLandingBinding;


@AndroidEntryPoint
public class LandingFragment extends Fragment {

  private FragmentLandingBinding binding;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentLandingBinding.inflate(inflater, container, false);
    View view = binding.getRoot();
    binding.startANewGame.setOnClickListener(v -> navigateToGameFragment());
    return view;
  }

  private void navigateToGameFragment() {
    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
    navController.navigate(R.id.action_landingFragment_to_gameFragment);
  }
}