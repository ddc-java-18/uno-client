package edu.cnm.deepdive.uno.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.ActivityMainBinding;
import edu.cnm.deepdive.uno.viewmodel.GameViewModel;
import edu.cnm.deepdive.uno.viewmodel.LoginViewModel;

/**
 * Main activity of the application.
 *
 * <p>Handles creating the MainActivity menu options for the settings and sign-out menu. It also
 * handles/sets up navigation for this activity.</p>
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private LoginViewModel loginViewModel;
  private GameViewModel gameViewModel;
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setupNavigation();
    setupViewModel();
    observeGameState();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main_options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
    return navController.navigateUp() || super.onSupportNavigateUp();
  }

  private void setupNavigation() {
    //noinspection DataFlowIssue
    navController = ((NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment))
        .getNavController();
  }

  private void setupViewModel () {
    ViewModelProvider provider = new ViewModelProvider(this);
    loginViewModel = provider.get(LoginViewModel.class);
    gameViewModel = provider.get(GameViewModel.class);
    loginViewModel.getAccount()
        .observe(this, (account) -> {
          if (account == null) {
            Intent intent = new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
          }
        });

  }
  private void observeGameState() {
    gameViewModel.getGame().observe(this, (game) -> {
      if (game == null) {
        navController.navigate(R.id.lobby_fragment); //
      }
    });
  }
}