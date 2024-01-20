package com.example.hw12androidtwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class OLD extends AppCompatActivity {

    String userInfo_ = "nothing";
    String roomInfo = "nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        TextView RoomView =findViewById(R.id.roomNameField);
        TextView UserView =findViewById(R.id.UserNameField);




        Bundle extras = getIntent().getExtras();
        if(extras != null){
            roomInfo = extras.getString(MainActivity.roomNameKey);
            userInfo_ = extras.getString(MainActivity.userNameKey);

        }
        RoomView.setText("Room: " + roomInfo);
        UserView.setText("User: " + userInfo_);

    }
    public void handleSendClick(View view) {
        EditText messageET = findViewById(R.id.ChatEdit);
        TextView tv = findViewById(R.id.messageField);
        ScrollView scrollView = findViewById(R.id.scrollView2);


        String message = String.valueOf(messageET.getText());
        tv.append(userInfo_ + ": " + message + "\n\n");
        messageET.setText("");

        // Scroll to the bottom of the ScrollView
        //making it in the runnable class is necessary to execute scrolling operation
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    }

