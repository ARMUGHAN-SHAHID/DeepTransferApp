package com.example.mufarooq.deeppose;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mufarooq.deeppose.Services.RetrofitClient;
import com.example.mufarooq.deeppose.utils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayResult extends AppCompatActivity {

    ImageView imageView = null;
    ImageView loadingView = null;
    Bitmap capturedPhoto=null;
//    AnimationDrawable stickAnimation;
    LinearLayout loader=null;
    String selectedTextureFilename=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_result);



        imageView= findViewById(R.id.resultImageView);
        Bundle extras = getIntent().getExtras();
        String capturedPhotoPath = extras.getString("captured_photo_path");
        selectedTextureFilename=extras.getString("selected_texture_name");
        capturedPhoto=utils.getBitmapFromImageFile(capturedPhotoPath);

        loader=(LinearLayout) findViewById(R.id.loaderDisplayResult);
        loader.setVisibility(View.VISIBLE);

        this.callAPI();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void callAPI(){
        RequestBody texture_filename = RequestBody.create(okhttp3.MultipartBody.FORM, selectedTextureFilename);


        Bitmap bitImage=capturedPhoto;
        File imageFile= utils.bitmapToFile(bitImage,this);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part image =
                MultipartBody.Part.createFormData("transfer", imageFile.getName()  , reqFile);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .transferTexture(texture_filename, image);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("image received from server");
                Bitmap bitImage = BitmapFactory.decodeStream(response.body().byteStream());

//                stickAnimation.stop();
                loader.setVisibility(View.INVISIBLE);
//                loadingView.setImageAlpha(0);
                imageView.setImageBitmap(bitImage);
                imageView.setImageAlpha(255);
                imageView.setBackgroundColor(getResources().getColor(android.R.color.black));


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }



}
