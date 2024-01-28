package com.example.websocketexample;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String WS_URL = "wss://devws.nepalpay.com.np/nqrws"; // your wobsocket url
    private StompClient mStompClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.call_Server);
        btn.setOnClickListener(this);
    }

    private void connectStompWebSocket(){
        //Open a Connection.
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL);
        mStompClient.connect();

        //subscribe to an endpoint
        Disposable topic = mStompClient.topic("put_subscribe_endpoint_here").subscribe(topicMessage -> {
            Log.d("Received Message:", topicMessage.getPayload());
        });

        // make request data that you want to send to the server
        //for example
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("merchant_id", "MER-980-APP-1");
            jsonMessage.put("request_id", "770342");
            jsonMessage.put("username", "username");
            jsonMessage.put("api_token", "WAxGtlKbhV3NE/fd3ci3zF2kLBOdY/RNC34t0LFJwuPpf6C0dlk4c+MciAth9dzxHenXZ7x0seSrxttcB+gRcg/V61c1x02AfyD7lQ==");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String jsonString = "";
        try {
            jsonString = jsonMessage.toString(4);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Send data to an endpoint
        mStompClient.send("put_data_send_endpoint_here", jsonString).subscribe();

        //Do not uncomment below line of code,
        //If uncommented you won't receive the message sent from Server after subscribing and sending
        /*mStompClient.disconnect();*/
    }

    @Override
    public void onClick(View v) {
        connectStompWebSocket();
    }
}