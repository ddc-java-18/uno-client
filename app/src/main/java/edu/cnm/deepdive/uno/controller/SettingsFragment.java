package edu.cnm.deepdive.uno.controller;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.R;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(@Nullable Bundle bundle, @Nullable String rootKey) {
    setPreferencesFromResource(R.xml.settings, rootKey);
  }

}
