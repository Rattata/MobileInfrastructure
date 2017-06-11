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

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView barcodeText;
    Button barcodeButton;
    Button button;
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

        //FacebookSdk.sdkInitialize(getApplicationContext());
        //callbackManager = CallbackManager.Factory.create();
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        Log.d("Debug", "login success");
//                        barcodeText.setText("Login succes\n"+loginResult.getAccessToken().getToken());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        //barcodeText.setText("Login cancel");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        //barcodeText.setText(exception.getMessage());
//                    }
//                });
//
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
