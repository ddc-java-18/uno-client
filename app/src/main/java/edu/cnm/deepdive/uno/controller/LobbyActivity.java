package edu.cnm.deepdive.uno.controller;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.ActivityLobbyBinding;

@AndroidEntryPoint
public class LobbyActivity extends AppCompatActivity {

  private ActivityLobbyBinding binding;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLobbyBinding.inflate(getLayoutInflater());
    binding.navigateToSettings.setOnClickListener((v) -> {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
    } );
    binding.startANewGame.setOnClickListener((v) -> {
      // TODO: 7/30/24 : invoke method in ViewModel to send start game request to server
    });
    // TODO: 7/30/24 : connect to ViewModel that can join a game, and observe when we have been added to a game.
    setContentView(binding.getRoot());
  }

  private void navigateToGame () {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }
}

