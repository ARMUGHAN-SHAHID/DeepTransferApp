package com.example.mufarooq.deeppose;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class StartScreen extends AppCompatActivity {
    AnimationDrawable stickAnimation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

//        pl.droidsonroids.gif.GifImageView stickImage = findViewById(R.id.imageViewStartScreen);
//        stickImage.setBackgroundResource(R.drawable.stickman_animation);
//        stickAnimation = (AnimationDrawable) stickImage.getBackground();
//        stickImage.setVisibility(0);

    }


    public void StartScreenonClick(View view) {
        Intent intent = new Intent(getApplicationContext(), CaptureImage.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        stickAnimation.start();
    }



}



