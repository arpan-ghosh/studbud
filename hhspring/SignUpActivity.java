package com.hophacks.hhspring;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SignUpActivity extends Activity {

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;

    private TextInputLayout nameError;
    private TextInputLayout emailError;
    private TextInputLayout passwordError;
    private TextInputLayout confirmPasswordError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameField = (EditText) findViewById(R.id.nameField);
        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        confirmPasswordField = (EditText) findViewById(R.id.confirmPasswordField);

        nameError = (TextInputLayout) findViewById(R.id.nameFieldError);
        emailError = (TextInputLayout) findViewById(R.id.emailFieldError);
        passwordError = (TextInputLayout) findViewById(R.id.passwordFieldError);
        confirmPasswordError = (TextInputLayout) findViewById(R.id.confirmPasswordFieldError);

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            user.logOutInBackground();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signUp(View view) {
        boolean pass = true;
        clearErrors();

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (name.equals("")) {
            nameError.setErrorEnabled(true);
            nameError.setError("Name cannot be blank");
            pass = false;
        }

        if (email.equals("")) {
            emailError.setErrorEnabled(true);
            emailError.setError("Email cannot be blank");
            pass = false;
        }

        if (password.equals("")) {
            passwordError.setErrorEnabled(true);
            passwordError.setError("Password cannot be blank");
            pass = false;
        }

        if (confirmPassword.equals("")) {
            confirmPasswordError.setErrorEnabled(true);
            confirmPasswordError.setError("Password cannot be blank");
            pass = false;
        }

        if (password.equals(confirmPassword) && pass) {
            final ParseUser user = new ParseUser();

            user.setEmail(email);
            user.setUsername(email);
            user.setPassword(password);
            user.put("name", name);
            user.put("classes", new ArrayList<String>());
            user.put("major", "Computer Science");
            user.put("buddies", new ArrayList<ParseUser>());

            /**********************************/
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_person_white_48dp);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();

            final ParseFile file = new ParseFile("default.jpg", image);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    user.put("pic", file);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                TextInputLayout error = (TextInputLayout) findViewById(R.id.emailFieldError);
                                error.setErrorEnabled(true);
                                Log.d("Error", e.toString());
                                error.setError("User already exists");
                            }
                        }
                    });
                }
            });

            /**********************************/

        } else if (!password.equals(confirmPassword) && !pass) {
            confirmPasswordError.setErrorEnabled(true);
            confirmPasswordError.setError("Passwords must match");
        }
    }

    public void clearErrors() {
        nameError.setErrorEnabled(false);
        emailError.setErrorEnabled(false);
        passwordError.setErrorEnabled(false);
        confirmPasswordError.setErrorEnabled(false);
    }
}

