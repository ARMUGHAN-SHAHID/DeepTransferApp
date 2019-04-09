package com.example.mufarooq.deeppose;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class CaptureImage extends AppCompatActivity {
    static final int takePictureRequestCode =1;
    File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_processing_screen);

        //calling camera api to take photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            photoFile = utils.createTempImageFile(getApplicationContext(),"captured_photos");
        }
        catch (IOException ex) {
            // Error occurred while creating the File
            System.out.println("IO exception occured");
        }
        if (photoFile!=null){
//            Uri photoURI = Uri.fromFile(photoFile);
            System.out.println("Package name is :");
            System.out.println(getPackageName());
            Uri photoURI=FileProvider.getUriForFile
                    (this,"com.example.mufarooq.deeppose.fileprovider",photoFile);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, takePictureRequestCode);
            }
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == takePictureRequestCode && resultCode == RESULT_OK){

            Intent intent = new Intent(this, SelectTexture.class);
            intent.putExtra("captured_photo_path",photoFile.getAbsolutePath() );
            startActivity(intent);



        }
    }
}
