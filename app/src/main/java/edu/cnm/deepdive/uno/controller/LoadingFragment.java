package edu.cnm.deepdive.uno.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.databinding.FragmentLoadingBinding;

/**
 * A {@link Fragment} subclass used to display and manage a loading view while a game is waiting
 * for more players to join.
 */
@AndroidEntryPoint
public class LoadingFragment extends Fragment {

  private FragmentLoadingBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentLoadingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}