package com.general.mediaplayer.csr;

import android.os.Bundle;

import com.general.mediaplayer.csr.wifi.WifiSettingsFragment;

public class SubSettings extends Settings
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return  AppsManagerSettings.class.getName().equals(fragmentName) ||
                AppsAutoRunFragement.class.getName().equals(fragmentName) ||
                AppsUninstallFragement.class.getName().equals(fragmentName) ||
                AppsInstallFragement.class.getName().equals(fragmentName) ||
                NetworkSettings.class.getName().equals(fragmentName) ||
                WifiSettingsFragment.class.getName().equals(fragmentName);
    }

}
