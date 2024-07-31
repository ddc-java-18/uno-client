package edu.cnm.deepdive.uno.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.databinding.FragmentLoadingBinding;


@AndroidEntryPoint
public class LoadingFragment extends Fragment {

  private Button navigateToSettingsButton;
  private FragmentLoadingBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentLoadingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}