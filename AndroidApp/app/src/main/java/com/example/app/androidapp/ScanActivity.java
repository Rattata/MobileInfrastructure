package com.example.app.androidapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import com.spotify.sdk.android.authentication.*;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.*;
import retrofit2.*;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

/**
 * Created by Ryno on 30-5-2017.
 */

public class ScanActivity extends Activity {

    public final static String DISCOGS_URL = "https://api.discogs.com/";
    public final static String DISCOGS_KEY = "KeCqVCAcXfxOMuIeTUfD";
    public final static String DISCOGS_SECRET = "RywjgidAXnzfKNUDuXeIVPUMtKxJoJQj";

    private Retrofit retrofit;
    private SurfaceView cameraPreview;
    private BackendService service = new BackendService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(DISCOGS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        setContentView(R.layout.scan_activity);
        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        createCameraSource();
    }

    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();


        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(ScanActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
                Log.i("barcode", String.format("detections: %s", barcodeSparseArray.size()));
                if (barcodeSparseArray.size() > 0){
                    Intent intent = new Intent();
                    intent.putExtra("barcode",barcodeSparseArray.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS, intent);
                    finish();
                    //service.BarcodeQuery(barcodeSparseArray.valueAt(0).displayValue);
                }
            }
        });
    }
}
