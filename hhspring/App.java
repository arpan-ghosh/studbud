package com.hophacks.hhspring;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Register any ParseObject subclass. Must be done before calling Parse.initialize()


        Parse.initialize(this, "pDlGVUrBYMNlgDM9dYnQYW9U8R9mgHY12veiZQWU", "vDkVVlGunWiW6gLBdvf7cR2vC4VKzCo1wB6cg0gK");
    }
}