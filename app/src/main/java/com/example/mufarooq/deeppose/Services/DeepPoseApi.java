package com.example.mufarooq.deeppose.Services;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//this file implements end points for sending requests to server

public interface DeepPoseApi {
    @Multipart
    @POST("transfer_texture")//relative url for transferring texture
    Call<ResponseBody> transferTexture(
            @Part("texture_filename") RequestBody texture_filename,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("retreive_texture")//relative url for transferring texture
    Call<ResponseBody> retreiveTexture(
            @Part("texture_filename") RequestBody texture_filename,
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3,
            @Part MultipartBody.Part image4
            );
}
