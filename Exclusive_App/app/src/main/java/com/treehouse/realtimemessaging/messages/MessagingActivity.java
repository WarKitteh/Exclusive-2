package com.treehouse.realtimemessaging.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.treehouse.realtimemessaging.R;
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
public class MessagingActivity extends AppCompatActivity
    implements ChildEventListener {


//    TODO Add a constant field for the name of your messages node from Firebase
    public static final String MESSAGES_FIREBASE_KEY = "messages";
//    TODO Get a reference to your FirebaseDatabase
    FirebaseDatabase firebase = FirebaseDatabase.getInstance();

//    TODO Get a reference to the "messages" node of your Firebase
    DatabaseReference messages = firebase.getReference(MESSAGES_FIREBASE_KEY);
    //private DatabaseReference messagesReference = firebase.getReference(MESSAGES_FIREBASE_KEY);
    private DatabaseReference messagesReference = firebase.getReference(MESSAGES_FIREBASE_KEY);
    private RecyclerView messagesList;
    private MessageAdapter messageAdapter;

    private EditText messageEntry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

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
        messagesReference.addChildEventListener(this);
    }

    /**
     * Called when the "Send Message" button is clicked.
     *
     * @param view The button that was clicked.
     */
    private void pushMessageToFirebase() {
        String messageContent = messageEntry.getText().toString();
        String username = UserUtility.getUsername(this);
        long now = System.currentTimeMillis();
        Message messageToSend = new Message(messageContent, username, now);
        messages.push().setValue(messageToSend);
    }

    public void sendMessage(View view) {
//        TODO Call the method that pushes the message to Firebase when the send button is clicked

        pushMessageToFirebase();

        resetMessageEntry();

        hideKeyboard();
    }

//    TODO Write a method that pushes a message to Firebase


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

    /**
     * Handy mehtod for starting this Activity.
     * @param context Used to start Activity
     */
    public static void start(OnCompleteListener<AuthResult> context) {
        Intent startMessagingActivity = new Intent((Context) context, MessagingActivity.class);
        ((Context) context).startActivity(startMessagingActivity);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message recivedMessage = dataSnapshot.getValue(Message.class);
        messageAdapter.addMessage(recivedMessage);
        scrollToMostRecentMessage();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
