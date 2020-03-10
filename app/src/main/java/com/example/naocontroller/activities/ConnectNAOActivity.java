package com.example.naocontroller.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            };
            int REQUEST_EXTERNAL_STORAGE = 1;

            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            broadcastClient = new BroadcastClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        b.setOnClickListener(v -> {
            try {
                serverIP = broadcastClient.sendBroadcast();

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
