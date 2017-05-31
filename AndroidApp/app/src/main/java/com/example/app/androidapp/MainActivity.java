package com.example.app.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView barcodeText;
    Button barcodeButton;
    CallbackManager callbackManager;
    LoginButton loginButton;
    public Retrofit retrofit;

    //private SensorManager sensor_manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setContentView(R.layout.activity_main);
        barcodeText = (TextView)findViewById(R.id.barcode_text);
        barcodeButton = (Button)findViewById(R.id.barcode_button);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/myapp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConnectionApi mApi = retrofit.create(ConnectionApi.class);

        //Call<List<String>> meep = mApi.listData();
        mApi.sendRequest();


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Debug", "login success");
                        barcodeText.setText("Login succes\n"+loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        //barcodeText.setText("Login cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //barcodeText.setText(exception.getMessage());
                    }
                });
    }

    public void scanBarcodeClick(View view){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == CommonStatusCodes.SUCCESS){
                    if (data != null){
                        Barcode barcode = data.getParcelableExtra("barcode");
                        barcodeText.setText(barcode.displayValue);
                        postBarcode(barcode.displayValue);
                    }
                    else{
                        barcodeText.setText("No barcode found");
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void postBarcode(String barcode) {
        
    }
}
