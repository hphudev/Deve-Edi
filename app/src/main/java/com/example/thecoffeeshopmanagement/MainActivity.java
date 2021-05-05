package com.example.thecoffeeshopmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MeowBottomNavigation bottomNavigationView = (MeowBottomNavigation) findViewById(R.id.bottomNavigationView);
//        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.ownFragment, R.id.bossFragment).build();
//        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.add(new MeowBottomNavigation.Model(1, R.drawable.ic_person_24));
        bottomNavigationView.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home_24));
        bottomNavigationView.add(new MeowBottomNavigation.Model(3, R.drawable.ic_master_24));

        bottomNavigationView.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId())
                {
                    case 1:
                        fragment = new ownFragment();
                        break;
                    case 2:
                        fragment = new homeFragment();
                        break;
                    case 3:
                        fragment = new bossFragment();
                        break;
                }
                loadFragment(fragment);
            }
        });
        bottomNavigationView.setCount(1, "10");
        bottomNavigationView.show(2, true);
        bottomNavigationView.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigationView.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
    }
}