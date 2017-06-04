package com.treehouse.realtimemessaging.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treehouse.realtimemessaging.R;
import com.treehouse.realtimemessaging.login.LoginActivity;
import com.treehouse.realtimemessaging.login.RegisterActivity;
import com.treehouse.realtimemessaging.login.UserUtility;

/**
 * Responsible for displaying a list of messages from Firebase, listening for new messages, and
 * sending messages to Firebase.
 * <p>
 * The key functionality we implement from Firebase in this particular Activity is the
 * ChildEventListener. That interface has five methods that we need to override, including
 * onChildAdded, onChildChanged, onChildRemoved, onChildMoved, and onCancelled. In this workshop,
 * we are only going to override onChildAdded to add messages to our RecyclerView as they are
 * added in Firebase.
 */
public class MessagingActivity extends AppCompatActivity implements
        ChildEventListener {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * We use this key to reference the list of messages in Firebase.
     */
    public static final String MESSAGES_FIREBASE_KEY = "messages";

    /**
     * This is a reference to the root of our Firebase. With this object, we can access any child
     * information in the database.
     */
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    /**
     * Using the key, "messages", we can access a reference to the list of messages. We will be
     * listening to changes to the children of this reference in this Activity.
     */
    private DatabaseReference messagesReference = firebase.getReference(MESSAGES_FIREBASE_KEY);

    private RecyclerView messagesList;
    private MessageAdapter messageAdapter;

    private EditText messageEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(MessagingActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        messagesList = (RecyclerView) findViewById(R.id.messages_list);
        messagesList.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));

        messageEntry = (EditText) findViewById(R.id.message_entry);
        messageEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean enterWasPressed = event == null
                                || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN);
                if (enterWasPressed) {
                    sendMessage(null);
                    return true;
                }

                return false;
            }
        });

        messageAdapter = new MessageAdapter();
        messagesList.setAdapter(messageAdapter);

        /*
         * Since MessagingActivity implement the ChildEventListener interface, we need to set the
         * ChildEventListener to this instance of the MessagingActivity. If we don't do this, we
         * won't be notified by Firebase when children of our messaging reference in the database
         * have changed.
         */
        messagesReference.addChildEventListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings){

            startActivity(new Intent(MessagingActivity.this, LoginActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message message = dataSnapshot.getValue(Message.class);
        messageAdapter.addMessage(message);

        scrollToMostRecentMessage();
    }

    /**
     * Called when the "Send Message" button is clicked.
     *
     * @param view The button that was clicked.
     */
    public void sendMessage(View view) {
        pushMessageToFirebase();

        resetMessageEntry();

        hideKeyboard();
    }

    /**
     * Sends a message to Firebase
     */
    private void pushMessageToFirebase() {
        String messageContent = messageEntry.getText().toString();
        String username = UserUtility.getUsername(this);
        long now = System.currentTimeMillis();
        Message messageToSend = new Message(username, messageContent, now);
        messagesReference.push().setValue(messageToSend);
    }

    /**
     * Hides the soft keyboard. Used after a message is sent.
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(messageEntry.getWindowToken(), 0);
    }

    /**
     * Clears the entered text. Used after a message is sent.
     */
    private void resetMessageEntry() {
        messageEntry.setText("");
    }

    /**
     * To give a nice effect when entering into this Activity, we can use this method to scroll
     * to the bottom of the list of messages. It is also used when a new message is sent.
     */
    private void scrollToMostRecentMessage() {
        int mostRecentMessageIndex = messageAdapter.getItemCount() - 1;
        messagesList.smoothScrollToPosition(mostRecentMessageIndex);
    }

    /*
     * Handy mehtod for starting this Activity.
     * @param context Used to start Activity
     */

    public static void start(Context context) {
        Intent startMessagingActivity = new Intent(context, MessagingActivity.class);
        context.startActivity(startMessagingActivity);
    }


    /**
     * These are methods from the ChildEventListener interface that we will not handle in this
     * workshop. For more information, see the documentation here
     * https://developers.google.com/android/reference/com/google/firebase/database/ChildEventListener
     * @param dataSnapshot An immutable snapshot of the data at the new data at the child location
     * @param s The key name of sibling location ordered before the child. This will be null for
     *          the first child node of a location.
     */
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    }

    /**
     * These are methods from the ChildEventListener interface that we will not handle in this
     * workshop. For more information, see the documentation here
     * https://developers.google.com/android/reference/com/google/firebase/database/ChildEventListener
     * @param dataSnapshot An immutable snapshot of the data at the child that was removed.
     */
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
    }

    /**
     * These are methods from the ChildEventListener interface that we will not handle in this
     * workshop. For more information, see the documentation here
     * https://developers.google.com/android/reference/com/google/firebase/database/ChildEventListener
     * @param dataSnapshot An immutable snapshot of the data at the location that moved.
     * @param s The key name of the sibling location ordered before the child location. This will
     *          be null if this location is ordered first.
     */
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    }

    /**
     * These are methods from the ChildEventListener interface that we will not handle in this
     * workshop. For more information, see the documentation here
     * https://developers.google.com/android/reference/com/google/firebase/database/ChildEventListener
     * @param databaseError A description of the error that occurred
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
    }


    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}