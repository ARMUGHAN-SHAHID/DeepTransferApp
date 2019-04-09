package com.example.mufarooq.deeppose;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mufarooq.deeppose.Services.RetrofitClient;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTexture extends AppCompatActivity {

    int num_photos_to_capture=4;
    final int takePictureRequestCode =1;
    String[] CapturedTexturePaths=new String[num_photos_to_capture];
    String[] ImageDescription= new String[] {"Front","Right","Rear","Left"};
    boolean[] isfilled={false,false,false,false};
    ImageView[] imageviews = {null,null,null,null};
    int selectedImage=0;
    ImageView mainView=null;
    Button mainbtn=null;
    Button proceedbtn=null;
    ConstraintLayout[] texture_items_views={null,null,null,null};
    public void takePhoto(View view){
//        start activity for capturing photo
//        returns file path

        //calling camera api to take photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile=null;


        try {
            photoFile = utils.createTempImageFile(getApplicationContext(),"captured_photos");
        }
        catch (IOException ex) {
            // Error occurred while creating the File
            System.out.println("IO exception occured");
        }
        if (photoFile!=null) {
            CapturedTexturePaths[selectedImage]=photoFile.getAbsolutePath();
            Uri photoURI = FileProvider.getUriForFile
                    (this, "com.example.mufarooq.deeppose.fileprovider", photoFile);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, takePictureRequestCode);
            }
        }

    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_texture);


        mainView=findViewById(R.id.mainViewaddTexture);
        mainbtn=findViewById(R.id.addTextureBtn);
        mainbtn.setText("Capture");
        proceedbtn=findViewById(R.id.proceedButtonAddTexture);
        proceedbtn.setAlpha(0);
        proceedbtn.setClickable(false);

//        mainbtn.setOnClickListener();
        LinearLayout scroller =findViewById(R.id.add_texture_Horizontalscrollview);
        LayoutInflater inflater =LayoutInflater.from(this);

        for (int i=0;i<num_photos_to_capture;i++){
            View view= inflater.inflate((R.layout.add_texture_item),scroller,false);
            TextView textView= ((View) view).findViewById(R.id.addtextureitemtext);
            textView.setText(ImageDescription[i]);

            ImageView imageView = view.findViewById(R.id.addtextureitemimage);
            imageView.setImageResource(R.drawable.avatar);
//            imageView.setId(i);
            imageviews[i]=imageView;

            texture_items_views[i]=((View) view).findViewById(R.id.addtextureConstraintLayout);

            final int imageid=i;
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
//                    texture_items_views[selectedImage].setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    selectedImage=imageid;
                    if (isfilled[imageid]) {
                        mainView.setImageBitmap(utils.getBitmapFromImageFile(CapturedTexturePaths[imageid]));
                        mainbtn.setText("Change");
                    }
                    else{
                        mainView.setImageResource(R.drawable.avatar);
                        mainbtn.setText("Capture");
                    }
                    for (int j=0;j<4;j++){
                        if (j==imageid){
                            texture_items_views[j].setBackgroundColor(getResources().getColor(android.R.color.black));
                        }
                        else{
                            texture_items_views[j].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        }
                    }
                }
            });
            scroller.addView(view);

            AssetManager assetManager = getAssets();

            mainView.setImageResource(R.drawable.avatar);
            mainbtn.setText("Capture");
            texture_items_views[0].setBackgroundColor(getResources().getColor(android.R.color.black));
//            proceedbtn.setImageBitmap(utils.getBitmapFromAssets(assetManager,"go.png"));

        }
    }

//    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == takePictureRequestCode && resultCode == RESULT_OK){
            imageviews[selectedImage].setImageBitmap(utils.getBitmapFromImageFile(CapturedTexturePaths[selectedImage]));
            isfilled[selectedImage]=true;

            if (selectedImage==num_photos_to_capture-1){
                System.out.println("all images taken");
                proceedbtn.setAlpha((float)1.0);
                proceedbtn.setClickable(true);
            }
            texture_items_views[selectedImage].setBackgroundColor(getResources().getColor(android.R.color.transparent));

            selectedImage=(selectedImage+1)%num_photos_to_capture;
            texture_items_views[selectedImage].setBackgroundColor(getResources().getColor(android.R.color.black));

            if (isfilled[selectedImage]) {
                mainView.setImageBitmap(utils.getBitmapFromImageFile(CapturedTexturePaths[selectedImage]));
                mainbtn.setText("Change");
            }
            else{
                mainView.setImageResource(R.drawable.avatar);
                mainbtn.setText("Capture");
            }


        }
    }

    public void extract_texture(View view) throws IOException{
        File frontimage=new File(CapturedTexturePaths[0]);
        final String filename=utils.generateRandomFilename()+"_texture_map_.jpg";
        final File newfile=utils.createFile(this,"textures",filename);
        utils.copyFile(frontimage,newfile);





        RequestBody texture_filename = RequestBody.create(okhttp3.MultipartBody.FORM, filename.substring(0,filename.length()-4));

        File imageFile1=frontimage;
        File imageFile2=new File(CapturedTexturePaths[1]);
        File imageFile3=new File(CapturedTexturePaths[2]);
        File imageFile4=new File(CapturedTexturePaths[3]);

        RequestBody reqFile1 = RequestBody.create(MediaType.parse("image/*"), imageFile1);
        MultipartBody.Part image1 =
                MultipartBody.Part.createFormData("img1", imageFile1.getName()  , reqFile1);

        RequestBody reqFile2 = RequestBody.create(MediaType.parse("image/*"), imageFile2);
        MultipartBody.Part image2 =
                MultipartBody.Part.createFormData("img2", imageFile2.getName()  , reqFile2);

        RequestBody reqFile3 = RequestBody.create(MediaType.parse("image/*"), imageFile3);
        MultipartBody.Part image3 =
                MultipartBody.Part.createFormData("img3", imageFile3.getName()  , reqFile3);

        RequestBody reqFile4 = RequestBody.create(MediaType.parse("image/*"), imageFile4);
        MultipartBody.Part image4 =
                MultipartBody.Part.createFormData("img4", imageFile4.getName()  , reqFile4);

        setContentView(R.layout.busy_screen);
        TextView textView=findViewById(R.id.BusytextView);
        textView.setText("Constructing Texture");

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .retreiveTexture(texture_filename, image1,image2,image3,image4);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Texture stored on server with filename");
                System.out.println(filename);

                Intent intent = new Intent(getApplicationContext(), SelectTexture.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Texture not stored on server with filename");
                Intent intent = new Intent(getApplicationContext(), SelectTexture.class);
                startActivity(intent);
                newfile.delete();

            }

        });

    }


}
