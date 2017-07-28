package com.general.mediaplayer.csr;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by mac on 28/07/2017.
 */

public class EnableMultiDex extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}