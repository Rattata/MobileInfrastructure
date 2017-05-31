package com.example.app.androidapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ryno on 31-5-2017.
 */

public interface ConnectionApi {

    /*@GET("https://accounts.spotify.com/authorize/?client_id=97b2e28c509d450ba4be16b6a02c048c&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fmyapp%2Fspotifycallback&scope=user-read-private%20user-read-email&state=34fFs29kd09")
    Call<List<String>> listData();*/

    @POST("http://localhost:8080/myapp/auth/redirect")
    void sendRequest();

    @POST()
    void sendAccessToken();

    @POST()
    void sendBarcode();
}
