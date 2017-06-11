package com.example.app.androidapp;

/**
 * Created by siege on 6/10/2017.
 */

import android.util.Log;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class BackendService {
    public static final String API_URL = "http://192.168.0.100:8080";
    public static AuthenticationRequest requestData;
    public static class AuthenticationRequest {

        public final String client_id;
        public String response_type;
        public String redirect_uri;
        public String state;
        public String scope;

        public AuthenticationRequest(String client_id,String response_type, String redirect_uri, String state, String scope) {
            this.client_id = client_id;
            this.redirect_uri = redirect_uri;
            this.state = state;
            this.scope = scope;
            this.response_type = response_type;

        }

        @Override
        public String toString() {
            return "clientid: " + client_id + "    redirect_uri:" + redirect_uri + "    state:" + state + "     scope" + scope ;
        }
    }

    public interface Spoterfy {
        @GET("/auth/request")
        Call<AuthenticationRequest> AuthenticationRequest();
    }



    public void AuthenticationService() throws IOException{

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Spoterfy github = retrofit.create(Spoterfy.class);

        Call<AuthenticationRequest> call = github.AuthenticationRequest();
        call.enqueue(new Callback<AuthenticationRequest>() {
            @Override
            public void onResponse(Call<AuthenticationRequest> call, Response<AuthenticationRequest> response) {
                int statusCode = response.code();
                BackendService.requestData = response.body();
                Log.i("authrequest", response.body().toString());
            }

            @Override
            public void onFailure(Call<AuthenticationRequest> call, Throwable t) {
                Log.e("BackendService", t.getMessage());
            }
        });
    }
}
