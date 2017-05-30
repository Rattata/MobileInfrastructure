package com.example.app.androidapp;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {

    TextView barcodeText;
    Button barcodeButton;

    //private SensorManager sensor_manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setContentView(R.layout.activity_main);
        barcodeText = (TextView)findViewById(R.id.barcode_text);
        barcodeButton = (Button)findViewById(R.id.barcode_button);
    }

    public void scanBarcodeClick(View view){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
