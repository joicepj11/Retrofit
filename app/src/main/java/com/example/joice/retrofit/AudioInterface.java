package com.example.joice.retrofit;


import okhttp3.MultipartBody;
import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AudioInterface {
    @Multipart
    @POST("audio.php")
    Call<ResultObject> uploadVideoToServer(@Part MultipartBody.Part audio);
}