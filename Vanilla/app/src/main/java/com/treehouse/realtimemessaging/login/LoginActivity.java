package com.treehouse.realtimemessaging.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treehouse.realtimemessaging.R;
import com.treehouse.realtimemessaging.messages.MessagingActivity;

import static com.treehouse.realtimemessaging.R.id.username;

public class LoginActivity extends AppCompatActivity {

    private Button LoginregisterButton;
    private Button LoginLoginButton;
    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private EditText mLoginUsernameField;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mLoginEmailField = (EditText) findViewById(R.id.email_edit_text);
        mLoginPasswordField = (EditText) findViewById(R.id.password_edit_text);


        LoginLoginButton = (Button) findViewById(R.id.loginButton);

        LoginLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });



        /*
         * If a user is currently signed in, we want to launch the MessagingActivity and close
         * this Activity.
         */
        /*
        if (UserUtility.isUserSignedIn(this)) {
            MessagingActivity.start(this);
            finish();
            return;
        }

        */

        /*
        final EditText usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        String username = usernameEditText.getText().toString();
        */

        /*
         * Just for user's convenience, we will login the user with the currently typed in username
         * when they press the enter key.
         */
        /*
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
        */

        /*
         * If the user wants to click the button rather than tapping enter, they may do so as well.
         */

        /*
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                loginUser(username);
            }
        });
        */

        //THIS IS THE REGISTER CODE. DNT!
        LoginregisterButton = (Button) findViewById(R.id.registerButton);
        LoginregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent window = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(window);
            }
        });
    }




    private void checkLogin() {

        String email = mLoginEmailField.getText().toString().trim();
        String password = mLoginPasswordField.getText().toString().trim();
        final EditText usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        final String username = usernameEditText.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

           mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       loginUser(username);
                       checkUserExist();

                   } else {
                       Toast.makeText(LoginActivity.this, "Error Login", Toast.LENGTH_LONG).show();
                   }
               }
           });
        }

    }

    private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){

                    Intent loginIntent = new Intent(LoginActivity.this, MessagingActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);


                }

                else{
                    Toast.makeText(LoginActivity.this, "You need to create an account", Toast.LENGTH_LONG).show();

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}
