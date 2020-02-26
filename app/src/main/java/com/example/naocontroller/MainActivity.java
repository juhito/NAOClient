package com.example.naocontroller;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private NAOClient client;
    private Button b;
    private Button message;
    private TextView naoCommand;
    private TextView naoMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl, R.string.drawer_open, R.string.drawer_close);

        dl.addDrawerListener(t);
        t.syncState();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nv = findViewById(R.id.nv);

        nv.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            switch(id) {
                case R.id.general:
                    Toast.makeText(MainActivity.this, "clicked general", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.behaviors:
                    Toast.makeText(MainActivity.this, "clicked behaviors", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings:
                    Toast.makeText(MainActivity.this, "clicked settings", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    return(true);
            }
            return(true);
        });

        b = findViewById(R.id.socketConn);
        message = findViewById(R.id.sendMessageButton);
        naoCommand = findViewById(R.id.naoCommand);
        naoMessage = findViewById(R.id.naoMessage);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            System.out.println(ConnectNAOActivity.getIP());
            this.client = new NAOClient(ConnectNAOActivity.getIP(), 8888);
        } catch (Exception e) {
            e.printStackTrace();
        }

        b.setOnClickListener(v -> {
            try {
                NAOClient.startThreadedConnection(ConnectNAOActivity.getIP(), 8888);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });

        message.setOnClickListener(v -> {
            try {
                String data = naoCommand.getText().toString();
                String message = naoMessage.getText().toString();

                if(message.isEmpty()) {
                    this.client.sendMessage(data);
                }
                else {
                    this.client.sendMessage(data, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(t.onOptionsItemSelected(item)) return(true);


        return super.onOptionsItemSelected(item);
    }
}
