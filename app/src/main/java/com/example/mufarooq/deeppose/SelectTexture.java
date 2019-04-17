package com.example.mufarooq.deeppose;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mufarooq.deeppose.Adapters.item;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SelectTexture extends AppCompatActivity {

    ImageView imageViewTextures = null;
    String[] texturemaps =new String[] {"SMPL"};
    String[] asset_textures_paths=new String[] {"surreal.png","adil.jpg","sohail.jpg","uzair.jpg"};
    String[] asset_textures_names=new String[] {"smpl","adil","sohail","uzair"};
    String capturedPhotoPath=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_texture_map);

        imageViewTextures = findViewById(R.id.ImageviewTextures);


        Bundle extras = getIntent().getExtras();
//        capturedPhotoByteArray = extras.getByteArray("captured_photo");
//        Bitmap bitmap=utils.convertByteArrayToBitmap(capturedPhotoByteArray);
        capturedPhotoPath=extras.getString("captured_photo_path");
        Bitmap bitmap=utils.getBitmapFromImageFile(capturedPhotoPath);
        imageViewTextures.setImageBitmap(bitmap);

//        scroller implemetation below

        LinearLayout scroller =findViewById(R.id.scrollbar_id);

        LayoutInflater inflater =LayoutInflater.from(this);
//        adding assets textures
        AssetManager assetManager = getAssets();

        for (int i=0;i<asset_textures_names.length;i++){
            View view= inflater.inflate((R.layout.texture_map),scroller,false);
//            TextView textView= ((View) view).findViewById(R.id.text_id);
//            textView.setText(asset_textures_names[i]);

            ImageView imageView = view.findViewById(R.id.image_id);
            imageView.setImageBitmap(utils.getBitmapFromAssets(assetManager,asset_textures_paths[i]));
            imageView.setId(i);
//            final int id_ = imageView.getId();
            final String Texture_Name=asset_textures_names[i];
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), DisplayResult.class);
                    intent.putExtra("captured_photo_path", capturedPhotoPath);
                    intent.putExtra("selected_texture_name",Texture_Name);
                    startActivity(intent);

                }
            });



            scroller.addView(view);

        }


//        adding  saved custom textures from internal directory
        try {
            File[] stored_textures = utils.getAllFilesInDir(this, "textures");
            for (File texture : stored_textures) {
                View view = inflater.inflate((R.layout.texture_map), scroller, false);

                String filename = texture.getName();
                if (filename.endsWith("_texture_map_.jpg")) {
                    String path = texture.getAbsolutePath();

                    ImageView imageView = view.findViewById(R.id.image_id);
                    imageView.setImageBitmap(utils.getBitmapFromImageFile(path));

                    final String Texture_Name = filename;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), DisplayResult.class);
                            intent.putExtra("captured_photo_path", capturedPhotoPath);
                            intent.putExtra("selected_texture_name", Texture_Name.substring(0,Texture_Name.length() - 4));
                            startActivity(intent);

                        }
                    });
                    scroller.addView(view);
                }


            }
        }
        catch (IOException e){
            System.out.println("IO exception occured while loading textures from directory");
        }

        View view= inflater.inflate((R.layout.texture_map),scroller,false);
        ImageView imageView = view.findViewById(R.id.image_id);
        imageView.setImageBitmap(utils.getBitmapFromAssets(assetManager,"_plus.png"));
//        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        TextView textView= ((View) view).findViewById(R.id.text_id);
//        textView.setText("New texture");

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddTexture.class);
                intent.putExtra("captured_photo_path", capturedPhotoPath);
                startActivity(intent);

            }
        });


        scroller.addView(view);
    }

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            Log.i("", "Long press!");
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());
        }
        if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP)) {
            handler.removeCallbacks(mLongPressed);
        }
        return super.onTouchEvent(event);
    }
}
