package edu.cnm.deepdive.uno.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.ActivityMainBinding;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setupNavigation();
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
}