package com.example.hw12androidtwo;


import static com.example.hw12androidtwo.ChatRoom2Activity.adapter_;
import static com.example.hw12androidtwo.ChatRoom2Activity.lv_;
import static com.example.hw12androidtwo.ChatRoom2Activity.roomInfo_;
import static com.example.hw12androidtwo.ChatRoom2Activity.userInfo_;
import static com.example.hw12androidtwo.ChatRoom2Activity.ws_;

import android.util.Log;

import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class myWebSocketHandler extends WebSocketAdapter{


    boolean WSOpen = false;
    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception
    {
        JSONObject JSONMsg = new JSONObject();

        try {
            JSONMsg.put("type", "join");
            JSONMsg.put("user", userInfo_);
            JSONMsg.put("room", roomInfo_);
            JSONMsg.put("message", "");
        } catch (JSONException e) {
            throw new RuntimeException(e);

        }

        ws_.sendText(String.valueOf(JSONMsg));

        Log.d("chatactivity", "user joined");

        WSOpen = true;
        Log.d("chatacivity", "web socket is open.");

    }
    //
    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception
    {
        super.onConnectError(websocket, exception);
        Log.d("chatacitvity" , "web socket failed to open");
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception
    {
        super.onError(websocket, cause);
        Log.d("chat", "error occured");
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception
    {
        if(WSOpen) {

            String displayMsg = "display msg";
            JSONObject jsonObject = new JSONObject(text);

            String type = jsonObject.getString("type");
            String user = jsonObject.getString("user");
            String room = jsonObject.getString("room");
            Log.d("chatactivity", "type: " + type);


            if (type.equals("join")) {
                displayMsg = user + " has entered " + room;
            }  if (type.equals("message")) {
                String message = jsonObject.getString("message");
                displayMsg = user + ": " + message;
            } if (type.equals("leave")) {
                displayMsg = user + " left " + room;
            }
            String finalDisplayMsg = displayMsg;
            Log.d("chatactivity", "Final display message" + finalDisplayMsg);

            lv_.post(new Runnable() {
                @Override
                public void run() {
                    // Handle incoming messages and update UI
                    ChatRoom2Activity.chatMessages.add(finalDisplayMsg);
                    //Update the list on the UI
                    adapter_.notifyDataSetChanged();
                    lv_.smoothScrollToPosition(adapter_.getCount());
                }
            });

        }
        else {
            Log.d("chatactivity", "WS NOT OPEN, NO MESSAGE");
        }

    }

}



