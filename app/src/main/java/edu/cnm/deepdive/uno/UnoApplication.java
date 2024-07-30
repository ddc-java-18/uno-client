/*
 *  Copyright 2024 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.uno;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import dagger.hilt.android.HiltAndroidApp;

/**
 * Initializes (in the {@link #onCreate()} method) application-level resources that require
 * static (or singleton) initialization. This class <strong>must</strong> be referenced in
 * {@code AndroidManifest.xml}, or it will not be loaded and used by the Android system.
 */
@HiltAndroidApp
public class UnoApplication extends Application implements OnSharedPreferenceChangeListener {

  private String darkModePrefKey;
  private String darkModePrefDefault;
  private String[] darkModeValues;
  private String[] darkModeOptions;

  @Override
  public void onCreate() {
    super.onCreate();
    darkModePrefKey = getString(R.string.dark_mode_pref_key);
    darkModePrefDefault = getString(R.string.dark_mode_pref_default);

    Resources res = getResources();
    darkModeOptions = res.getStringArray(R.array.dark_mode_options);
    darkModeValues = res.getStringArray(R.array.dark_mode_values);

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    prefs.registerOnSharedPreferenceChangeListener(this);

    updateDarkMode(prefs);

  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
    if (key != null && key.equals(darkModePrefKey)) {
      updateDarkMode(sharedPreferences);
    }
  }

  private void updateDarkMode(SharedPreferences sharedPreferences) {
    String darkModePref = sharedPreferences.getString(darkModePrefKey, darkModePrefDefault);
    int mode = switch (darkModePref) {
      case "on" -> AppCompatDelegate.MODE_NIGHT_YES;
      case "off" -> AppCompatDelegate.MODE_NIGHT_NO;
      default -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    };
    AppCompatDelegate.setDefaultNightMode(mode);
  }
}
