package com.example.app.androidapp;

/**
 * Created by siege on 6/10/2017.
 */

import android.util.Log;

import java.io.IOException;
import java.util.Date;
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
    public static Account account;

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

    public static class Account {

        public String code;
        public String access_token;
        public String refresh_token;
        public Date access_token_expires;
        public String email;
        public String state;
        public int ID;

        public Account(String code, String access_token, String refresh_token, Date access_token_expires, String email, String state, int ID) {
            this.code = code;
            this.access_token = access_token;
            this.refresh_token = refresh_token;
            this.access_token_expires = access_token_expires;
            this.email = email;
            this.state = state;
            this.ID = ID;
        }
    }

    public interface Spoterfy {
        @GET("/auth/request")
        Call<AuthenticationRequest> AuthenticationRequest();

        @GET("/auth/redirect")
        Call<Account> Redirect(String code, String state);
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

    public void Redirect(String code, String state){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Spoterfy github = retrofit.create(Spoterfy.class);
        Call<Account> call = github.Redirect(code,state);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                int statusCode = response.code();
                BackendService.account = response.body();
                Log.i("authrequest", response.body().toString());
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.e("BackendService", t.getMessage());
            }
        });
    }
}