package com.example.app.androidapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ryno on 31-5-2017.
 */

public interface ConnectionApi {

    @GET("database/search?")
    Call<DiscogsResults> BarcodeToAlbumName(@Query("barcode") String barcode, @Query("key") String key,@Query("secret") String secret);

    @GET("database/{release}/{id}")
    Call<DiscogsAlbumResult> GetRelease(@Query("release") String releaseType, @Query("id") String id , @Query("key") String key, @Query("secret") String secret) ;

    class DiscogsAlbumResult {
        public List<Artist> artists;
        public String title;
    }

    class Artist {
        public String name;
    }
}
