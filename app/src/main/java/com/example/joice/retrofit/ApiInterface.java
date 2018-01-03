package com.example.joice.retrofit;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("getUser.php")
    Call<List<Contacts>> getUserInfo();
    @POST("addUser.php")
    Call<String> addUserInfoToDatabase(@Body Contacts body);
    @POST("getUser.php")
    Observable<List<Contacts>> getUserInfo1();
    @POST("addUser.php")
    Observable<String> addUserInfoToDatabase1(@Body Contacts body);
}