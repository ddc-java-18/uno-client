package edu.cnm.deepdive.uno.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;

/**
 * Activity for displaying and managing application settings.
 */
@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
  }
}