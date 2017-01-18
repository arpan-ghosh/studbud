package com.hophacks.hhspring;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new LoadUser().execute("");
    }

    private class LoadUser extends AsyncTask<String, Void, String> {

        private ParseUser user;

        @Override
        protected String doInBackground(String... params) {
            String result = "null";
            user = ParseUser.getCurrentUser();
            if (user != null) {
                result = "";
                Log.d("User?","yes");
            }

            try {
                Thread.sleep(SPLASH_TIME_OUT);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent;
            if (result.equals("")) {
                intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
            intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
