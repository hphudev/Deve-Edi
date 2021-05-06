package com.example.thecoffeeshopmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    //region Khai báo
    ImageView imageViewBackground;
    ImageView imageViewTitle;
    LottieAnimationView lottieAnimationViewLogo;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //region Ẩn thanh ActionBar
        getSupportActionBar().hide();
        //endregion
        // region Tham chiếu đến các View
        imageViewBackground = (ImageView)findViewById(R.id.ivSplashBackground);
        imageViewTitle = (ImageView)findViewById(R.id.ivTitle);
        lottieAnimationViewLogo = (LottieAnimationView)findViewById(R.id.lottieAnimationView_logo);
        //endregion
        //region Chuyển động View
        imageViewBackground.animate().translationY(-2600).setDuration(900).setStartDelay(3000);
        imageViewTitle.animate().translationX(2600).setDuration(900).setStartDelay(3000);
        lottieAnimationViewLogo.animate().translationX(-2600).setDuration(900).setStartDelay(3000);
        //endregion
        //region Kết thúc Splash Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 4000);
        //endregion
    }
}