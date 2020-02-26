package com.example.naocontroller;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naocontroller.fragments.GeneralFragment;
import com.google.android.material.navigation.NavigationView;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private Toolbar tb;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dl = findViewById(R.id.drawer_layout);
        t = setupDrawerToggle();

        t.setDrawerIndicatorEnabled(true);
        t.syncState();

        dl.addDrawerListener(t);

        nv = findViewById(R.id.nvView);
        setupDrawerContent(nv);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return(new ActionBarDrawerToggle(this, dl, tb, R.string.drawer_open, R.string.drawer_close));
    }

    private void setupDrawerContent(NavigationView view) {
        view.setNavigationItemSelectedListener(menuItem -> {
            selectDrawerItem(menuItem);
            return(true);
        });
    }

    public void selectDrawerItem(MenuItem item) {
        // Create a new fragment and specify the fragment to show based on nav item clicked.
        Fragment fragment = null;
        Class fragmentClass;

        switch(item.getItemId()) {
            case R.id.general:
                fragmentClass = GeneralFragment.class;
                break;
            default:
                fragmentClass = GeneralFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        dl.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        t.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        t.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item)) {
            return(true);
        }
        return(super.onOptionsItemSelected(item));
    }
}
