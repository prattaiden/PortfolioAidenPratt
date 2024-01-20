package com.example.hw12androidtwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ChatRoom2Activity extends AppCompatActivity {

    public static String userInfo_;
    public static String roomInfo_;

    public static String message_;

    public static ListView lv_;
    public static ArrayList<String> chatMessages = new ArrayList<>();

    public static ArrayAdapter<String> adapter_;

    private static final String WS_URL = "ws://10.0.2.2:8080/endpoint";
    public static WebSocket ws_ = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        userInfo_ = bundle.getString("userNameKey");
        roomInfo_ = bundle.getString("roomNameKey");


        //----------------------------WEBSOCKETS------------------------------------

        try {
            ws_ = new WebSocketFactory().createSocket(WS_URL);


            //listen for event and will use this class to implement them
            ws_.addListener(new myWebSocketHandler());
            ws_.connectAsynchronously();



        } catch (IOException e) {
            //AlertDialog alert = new AlertDialog("Server failed");
            Log.d("chatactivity", "some error");
        }

        //-------------------------setting layout----------------------------------
        setContentView(R.layout.activity_chat_room2);

        TextView RoomView =findViewById(R.id.roomNameField);
        TextView UserView =findViewById(R.id.UserNameField);
        lv_ = findViewById(R.id.chatListView);

        adapter_ = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                                      chatMessages);

        lv_.setAdapter(adapter_);



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            roomInfo_ = extras.getString(MainActivity.roomNameKey);
            userInfo_ = extras.getString(MainActivity.userNameKey);

        }

        //sending the join message to the web socket
       // ws_.sendText("join " + userInfo_ + " " + roomInfo_);

        //updating the app page
        RoomView.setText("Room: " + roomInfo_);
        UserView.setText("User: " + userInfo_);
    }


    //-----------------button methods---------------------------------------
    public void handleSendClick(View view) {
        EditText messageET = findViewById(R.id.ChatEdit);

        JSONObject JSONMsg = new JSONObject();
        message_ = messageET.getText().toString();
        try {
            JSONMsg.put("type", "message");
            JSONMsg.put("user", userInfo_);
            JSONMsg.put("room", roomInfo_);
            JSONMsg.put("message", message_);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageET.getText().clear();
        if (!message_.isEmpty()) {
            ws_.sendText(String.valueOf(JSONMsg));
        }
        Log.d("chatactivity", "msg was sent");

    }

    public void handleLeaveButton(View view) {
        Log.d("chatactivity", "button pressed to leave");

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    public void handleLeave(View view) {
        EditText messageET = findViewById(R.id.ChatEdit);

        JSONObject JSONMsg = new JSONObject();
        message_ = messageET.getText().toString();
        try {
            JSONMsg.put("type", "leave");
            JSONMsg.put("user", userInfo_);
            JSONMsg.put("room", roomInfo_);
            //JSONMsg.put("message", message_);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageET.getText().clear();
        if (!message_.isEmpty()) {
            ws_.sendText(String.valueOf(JSONMsg));
        }
        Log.d("chatactivity", "user left");

    }
}
