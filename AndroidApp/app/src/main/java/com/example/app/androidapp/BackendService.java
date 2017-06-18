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
import retrofit2.http.Query;

public class BackendService {
    public static final String API_URL = "http://192.168.0.19:8080";
    public static AuthenticationRequest requestData;
    public static Account account;
    public static Retrofit retrofit;

    public BackendService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

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

        @Override
        public String toString() {
            return String.format("code:%s%naccess_token:%s%nrefresh_token:%s%naccess_token_expires:%s%nemail:%s%nstate:%s%nID:%d%n", code,access_token,refresh_token,access_token_expires.toString(),email,state,ID);
        }
    }

    public interface Spoterfy {
        @GET("/auth/request")
        Call<AuthenticationRequest> AuthenticationRequest();

        @GET("/auth/redirect")
        Call<Account> Redirect(@Query("code") String code, @Query("state") String state);

        @GET("/album/")
        Call<String> AlbumQuery(@Query("barcode") String barcode, @Query("userid") int userid);


        @GET("/album/artist/{artist}/album/{album}")
        Call<String> AlbumArtistQuery(@Path("artist") String artist, @Path("album") String album, @Query("userid") int userid);
    }


    public void AuthenticationService() throws IOException{

        Spoterfy spotify = retrofit.create(Spoterfy.class);

        Call<AuthenticationRequest> call = spotify.AuthenticationRequest();
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

        Spoterfy spotify = retrofit.create(Spoterfy.class);
        Call<Account> call = spotify.Redirect(code,state);
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

    public void BarcodeQuery(String barcode){
        Spoterfy spotify = retrofit.create(Spoterfy.class);
        Log.i("account", account.toString());
        Call<String> album =  spotify.AlbumQuery(barcode, account.ID);
        album.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int statusCode = response.code();
                Log.i("authrequest", response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("BackendService", t.getMessage());
            }
        });
    }

    public void AlbumArtistQuery(String albumTitle, String artistTitle, int userId){
        Spoterfy spotify = retrofit.create(Spoterfy.class);
        Log.i("account", account.toString());
        Call<String> album =  spotify.AlbumArtistQuery(artistTitle, albumTitle, account.ID);
        album.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("authrequest", response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("BackendService", t.getMessage());
            }
        });
    }


}
