package br.com.pelisoli.realm.main;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by pelisoli on 04/01/16.
 */
public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
		Realm.setDefaultConfiguration(config);
	}
}
