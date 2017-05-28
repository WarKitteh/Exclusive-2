package com.treehouse.realtimemessaging.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.treehouse.realtimemessaging.R;
import com.treehouse.realtimemessaging.messages.MessagingActivity;

public class LoginActivity extends AppCompatActivity implements OnCompleteListener<AuthResult> {

    public Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent window = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(window);

            }
        });
        /*
         * If a user is currently signed in, we want to launch the MessagingActivity and close
         * this Activity.
         */
        if (UserUtility.isUserSignedIn(this)) {
            MessagingActivity.start(this);
            finish();
            return;
        }

        final EditText usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        /*
         * Just for user's convenience, we will login the user with the currently typed in username
         * when they press the enter key.
         */
        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String username = usernameEditText.getText().toString();
                    loginUser(username);
                }
                return false;
            }
        });

        /*
         * If the user wants to click the button rather than tapping enter, they may do so as well.
         */


        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                loginUser(username);
            }
        });
    }

    /**
     * Saves the username using UserUtility, starts the messaging Activity and finishes the
     * current instance of LoginActivity.
     * @param username Username that the user has typed to be used when sending messages
     */
    private void loginUser(String username) {
        UserUtility.setUsername(this, username);
        MessagingActivity.start(this);
        finish();
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

    }
}
