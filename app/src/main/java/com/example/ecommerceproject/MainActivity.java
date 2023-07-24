package com.example.ecommerceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    Animation splash_screen_trans;
    LottieAnimationView shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        shop=findViewById(R.id.lottie_shop_splash);
        splash_screen_trans= AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shop.setVisibility(View.VISIBLE);
                shop.setAnimation(splash_screen_trans);

            }
        },100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in=new Intent(MainActivity.this, Login_Page.class);
                startActivity(in);
                finish();


            }
        },1700);
    }
}