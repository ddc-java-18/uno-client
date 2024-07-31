package edu.cnm.deepdive.uno.controller;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.ActivityLobbyBinding;
import edu.cnm.deepdive.uno.viewmodel.LoginViewModel;



@AndroidEntryPoint
public class LobbyActivity extends AppCompatActivity {

  private LoginViewModel loginViewModel;
  private ActivityLobbyBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLobbyBinding.inflate(getLayoutInflater());
    binding.navigateToSettings.setOnClickListener((v) -> {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
    } );
    loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    binding.startANewGame.setOnClickListener((v) -> {
      // TODO: 7/30/24 : invoke method in ViewModel to send start game request to server
    });
    // TODO: 7/30/24 : connect to ViewModel that can join a game, and observe when we have been added to a game.
    setContentView(binding.getRoot());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main_options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    super.onOptionsItemSelected(item);
    boolean handled = true;
    if (item.getItemId() == R.id.settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
    } else if (item.getItemId() == R.id.sign_out) {
      loginViewModel.signOut();
    } else {
      handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  @Override
  public boolean onSupportNavigateUp() {
    return super.onSupportNavigateUp() || super.onSupportNavigateUp();
  }

  private void navigateToGame () {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }
}

