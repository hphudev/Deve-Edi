package com.example.thecoffeeshopmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Window;
import android.view.WindowManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout con = (ConstraintLayout)findViewById(R.id.login_layout);
        con.setBackgroundColor(Color.WHITE);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

}