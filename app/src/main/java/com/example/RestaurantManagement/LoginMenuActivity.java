package com.example.RestaurantManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.RestaurantManagement.R;

public class LoginMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HideActionBar();
        CreateButtonLoginNormal();
        CreateButtonSignUp();
    }

    private void HideActionBar()
    {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void CreateButtonLoginNormal()
    {
        Button btnLoginNormal = (Button)findViewById(R.id.btnLoginNormal);
        btnLoginNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMenuActivity.this, LoginNormalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CreateButtonSignUp()
    {
        TextView btnSignUp = (TextView)findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMenuActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}