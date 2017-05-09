package com.general.mediaplayer.csr;

import android.os.Bundle;

public class AppsManagerSettings extends SettingsPreferenceFragment {

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.addPreferencesFromResource(R.xml.apps_manager_settings);
   }
}
