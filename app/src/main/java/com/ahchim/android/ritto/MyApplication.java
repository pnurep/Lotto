package com.ahchim.android.ritto;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Gold on 2017. 4. 9..
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

//        new MapLibraryConfigImpl().getLibraryNames().add("libDaumMapEngineApi");

    }
}
