package com.example.RestaurantManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.RestaurantManagement.R;

import java.util.Objects;

public class LoginNormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_normal);
        InitActionBar();
        CreateButtonLogin();
        CreateImangeButtonInfo();
    }

    private void InitActionBar()
    {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đăng nhập");
    }

    private void HideActionBar()
    {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void CreateImangeButtonInfo()
    {
        ImageButton imageButton = (ImageButton)findViewById(R.id.ibInfo);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginNormalActivity.this, ProductInformationActivity.class);
                        startActivity(intent);
                    }
                }, 500);
            }
        });
    }
    private void CreateButtonLogin()
    {
        Button btnLogin = (Button)findViewById(R.id.btnLoginOfNormalLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginNormalActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
