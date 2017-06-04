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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.treehouse.realtimemessaging.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView usernameDisplay;
    private TextView messageDisplay;
    private TextView timestampDisplay;

    public MessageViewHolder(View itemView) {
        super(itemView);

        usernameDisplay = (TextView) itemView.findViewById(R.id.username);
        messageDisplay = (TextView) itemView.findViewById(R.id.message);
        timestampDisplay = (TextView) itemView.findViewById(R.id.timestamp);
    }

    public void bind(Message message) {
        messageDisplay.setText(message.getMessage());
        usernameDisplay.setText(message.getUsername());
        timestampDisplay.setText(message.getFormattedTimestamp());
    }
}