package com.example.mufarooq.deeppose;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class utils  {

    public static  File bitmapToFile(Bitmap bitmap,AppCompatActivity Activ) {
        ContextWrapper cw = new ContextWrapper(Activ.getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory, "temp");
        if (f.exists())
            f.delete();

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


    public static byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return byteArray;
    }

    public static Bitmap convertByteArrayToBitmap(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }
    public static File createFolder(Context context,String directory) throws IOException {
        File rootfolder=context.getFilesDir();
        File subdir= new File(rootfolder, directory);
        if (!subdir.exists()){
            subdir.mkdir();
        }
//        File file = new File(subdir, filename);
        return subdir;


    }
    public static File createTempImageFile(Context context,String directory) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = createFolder(context,directory);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        if (image.exists()){
            System.out.println("Image file created ");
            System.out.println(image.getAbsolutePath());
        }
        else{
            System.out.println("Image file not created ");
        }
        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap getBitmapFromImageFile(String filename){
        return BitmapFactory.decodeFile(filename);
    }

//    public static Bitmap getBitmapFromURI(Uri photoURI){
//        File imageFile= new File(photoURI.getPath());
//        return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//    }
    public static Bitmap getBitmapFromAssets(AssetManager assetManager, String fileName){
        try {
            InputStream istr = assetManager.open(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(istr);
            return bitmap;
        }
        catch(IOException ex)
        {
            return null;
        }

    }

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                System.out.println("File Copied");
            }
            finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static File createFile(Context context,String directory,String filename)
            throws IOException
    {
        File storageDir = createFolder(context,directory);
        File newfile=new File(storageDir, filename);
        if (newfile.exists()){
            System.out.println("New file created ");
            System.out.println(newfile.getAbsolutePath());
        }
        else{
            System.out.println("New file not created ");
        }
        return newfile;

    }

    public static String generateRandomFilename(){
        return UUID.randomUUID().toString();
    }

    public static File[] getAllFilesInDir(Context context,String directory) throws IOException {
        File storageDir = createFolder(context,directory);
        return storageDir.listFiles();
    }
}
