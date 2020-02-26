package com.example.naocontroller.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.naocontroller.network.BroadcastClient;
import com.example.naocontroller.R;

import java.net.InetAddress;


public class ConnectNAOActivity extends AppCompatActivity {

    private BroadcastClient broadcastClient;
    private static InetAddress serverIP;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_nao);
        b = findViewById(R.id.naoConnect);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            this.broadcastClient = new BroadcastClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        b.setOnClickListener(v -> {
            try {
                serverIP = this.broadcastClient.sendBroadcast();

                if(serverIP != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else {
                    System.out.println("Something went wrong!");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static InetAddress getIP() {
        return(serverIP);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
