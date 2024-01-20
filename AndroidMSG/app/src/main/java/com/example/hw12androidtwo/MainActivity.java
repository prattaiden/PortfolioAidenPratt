package com.example.hw12androidtwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    final String msTag = "MainActivity:Dd";
    static final String roomNameKey = "roomNameKey";
    static final String userNameKey = "userNameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleButton(View view){
        Log.d(msTag, "button pressed");
        EditText RoomET = findViewById(R.id.RoomNameID);
        EditText UserET = findViewById(R.id.userNameID);

        String roomName = String.valueOf(RoomET.getText());
        String userName = String.valueOf(UserET.getText());

        Intent intent = new Intent(this, ChatRoom2Activity.class);
        intent.putExtra(roomNameKey, roomName);
        intent.putExtra(userNameKey, userName);
        startActivity(intent);

        //send a join request
        //ChatRoom2Activity.ws_.sendText;



    }
}