package edu.cnm.deepdive.uno.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.databinding.FragmentWaitingBinding;


@AndroidEntryPoint
public class WaitingFragment extends Fragment {

  private Button navigateToSettingsButton;
  private FragmentWaitingBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentWaitingBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}