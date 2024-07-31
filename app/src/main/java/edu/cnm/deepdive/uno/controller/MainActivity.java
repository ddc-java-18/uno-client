package edu.cnm.deepdive.uno.controller;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.ActivityMainBinding;
import edu.cnm.deepdive.uno.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private NavController navController;
  private LoginViewModel loginViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setupNavigation();
    setupViewModel();
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

  private void setupNavigation() {
    //noinspection DataFlowIssue
    navController = ((NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment))
        .getNavController();
    AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
        R.id.landing_fragment, R.id.game_fragment, R.id.results_fragment)
        .build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
    NavigationUI.setupWithNavController(binding.navigator, navController);
  }

  private void setupViewModel () {
    loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    loginViewModel.getAccount()
        .observe(this, (account) -> {
          if (account == null) {
            Intent intent = new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
          }
        });
    loginViewModel.getUser();
  }
}