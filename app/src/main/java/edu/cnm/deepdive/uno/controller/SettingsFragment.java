package edu.cnm.deepdive.uno.controller;


import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.DropDownPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;

/**
 * A {@link Fragment} subclass used to display and manage the application's settings options.
 */
@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {

  private SharedPreferences prefs;

  @Override
  public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String key) {
    setPreferencesFromResource(R.xml.settings, key);
    String darkModePrefKey = getString(R.string.dark_mode_pref_key);
    DropDownPreference pref = findPreference(darkModePrefKey);
    prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
    String darkModePrefDefault = getString(R.string.dark_mode_pref_default);
    String darkModePrefValue = prefs.getString(darkModePrefKey, darkModePrefDefault);
    pref.setSummary(getDarkModeEntryForValue(darkModePrefValue));
    pref.setOnPreferenceChangeListener((preference, value) -> {
      pref.setSummary(getDarkModeEntryForValue(String.valueOf(value)));
      return true;
    });
  }

  private String getDarkModeEntryForValue(String value) {
    String[] entries = getResources().getStringArray(R.array.dark_mode_options);
    String[] values = getResources().getStringArray(R.array.dark_mode_values);
    for (int i = 0; i < values.length; i++) {
      if (value.equals(values[i])) {
        return entries[i];
      }
    }
    return null;
  }
}
