/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.treehouse.realtimemessaging.messages;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple POJO class for representing a Message that a user can send.
 */
@IgnoreExtraProperties
public class Message {

    private String username;
    private String message;
    private long timestamp;

    /**
     * Default constructor that takes no arguments is required for Firebase deserialization. When
     * you want to get an object from a DataSnapshot in Firebase by calling
     * DataSnapshot#getValue, the class that you want an object of needs to have this default
     * constructor. Perhaps an example will help:
     *
     *     ValueEventListener messageListener = new ValueEventListener() {
     *         @Override
     *         public void onDataChange(DataSnapshot dataSnapshot) {
     *             // Get Message object and use the values to update the UI
     *             Message messageFromFirebase = dataSnapshot.getValue(Message.class);
     *             // ...
     *         }
     *
     *         @Override
     *         public void onCancelled(DatabaseError databaseError) {
     *             // Getting Message failed, log a message
     *         }
     *     };
     *
     * In order to use dataSnapthost.getValue and pass in Message.class, we need a default
     * constructor for the Message class. We use this functionality in the {@link MessagingActivity}
     * in the {@link MessagingActivity#onChildAdded(DataSnapshot, String)} method.
     */
    public Message() {
    }

    /**
     * Basic constructor for a message object.
     * @param username Author of the message
     * @param message Message text contents
     * @param timestamp When the message was sent (UNIX time)
     */
    public Message(String username, String message, Long timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Public getters are required by Firebase for all properties of the object we want to assign
     * when the object is deserialized.
     * @return The author of the message
     */
    public String getUsername() {
        return username;
    }

    /**
     * Public getters are required by Firebase for all properties of the object we want to assign
     * when the object is deserialized.
     * @return The contents of the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Public getters are required by Firebase for all properties of the object we want to assign
     * when the object is deserialized.
     * @return The timestamp when the message was sent
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a nicely formatted timestamp that is human readable. This method is NOT required
     * by Firbase, but is used in the {@link MessageViewHolder} to display a timestamp that makes
     * sense to humans.
     * @return A human readable timestamp when this message was sent
     */
    public String getFormattedTimestamp() {
        String datePattern = "EEE, MMM d, h:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        Date messageCreationDate = new Date(timestamp);
        return dateFormat.format(messageCreationDate);
    }
}
