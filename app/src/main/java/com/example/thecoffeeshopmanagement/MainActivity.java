package com.example.thecoffeeshopmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.ownFragment, R.id.bossFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}