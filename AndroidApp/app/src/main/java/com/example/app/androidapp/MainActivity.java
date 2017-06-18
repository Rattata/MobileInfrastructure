package com.example.app.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView barcodeText;
    Button barcodeButton;
    Button button;
    private Retrofit retrofit;
    //    CallbackManager callbackManager;
    //LoginButton loginButton;
    BackendService service = new BackendService();

    //private SensorManager sensor_manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setContentView(R.layout.activity_main);
        barcodeText = (TextView) findViewById(R.id.barcode_text);
        barcodeButton = (Button) findViewById(R.id.barcode_button);
        try {
            service.AuthenticationService();
        } catch (IOException except) {
            Log.e("authenticationrequest", "could not reach authenticationservice");
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(ScanActivity.DISCOGS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void scanBarcodeClick(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, 0);
    }

    public void SpotifyLogin(View view) {

        BackendService.AuthenticationRequest authrequest = BackendService.requestData;
        if (authrequest == null) {
            return;
        }
        final AuthenticationRequest request = new AuthenticationRequest.Builder(authrequest.client_id, AuthenticationResponse.Type.CODE, authrequest.redirect_uri)
                .setScopes(new String[]{authrequest.scope})
                .build();

        AuthenticationClient.openLoginActivity(this, 1337, request);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1337) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            switch (response.getType()) {
                case CODE:
                    service.Redirect(response.getCode(), response.getState());
                    break;
            }

            //barcode result
        } else if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeText.setText(barcode.displayValue);
                    postBarcode(barcode.displayValue);
                    ConnectionApi connectionApi = retrofit.create(ConnectionApi.class);
                    //final DiscogsRequest discogsRequest = new DiscogsRequest();
                    Call<DiscogsResults> leBarcode = connectionApi.BarcodeToAlbumName(barcode.rawValue, ScanActivity.DISCOGS_KEY, ScanActivity.DISCOGS_SECRET);
                    leBarcode.enqueue(new Callback<DiscogsResults>() {
                        @Override
                        public void onResponse(Call<DiscogsResults> call, Response<DiscogsResults> response) {

                            //Log.i("Meep", response.body().getTitle());


                            Log.i("Meep", response.body().getPagination().toString());
                            Log.i("Meep", response.body().results.get(0).title);
                            service.BarcodeQuery(response.body().results.get(0).title);

                            Log.i("BarcodetoAlbum su6: ",String.valueOf(response.isSuccessful()));
                            Log.i("Barcode to Album code: ",String.valueOf(response.code()));
                            Log.i("BarcodeAlbum message : ",response.message());
                            Log.i("Barcode to Album", response.body().toString());
                        }

                        @Override
                        public void onFailure(Call<DiscogsResults> call, Throwable t) {
                            Log.i("Failed","Failed");
                            Log.i("Failed",t.toString());
                            Log.i("Failed",t.getMessage());
                        }
                    });
                } else {
                    barcodeText.setText("No barcode found");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void postBarcode(String barcode) {

    }


    private int userId;

    private void openLoginWindow() {

    }
}
