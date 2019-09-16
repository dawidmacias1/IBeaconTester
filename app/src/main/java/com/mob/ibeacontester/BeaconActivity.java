package com.mob.ibeacontester;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BeaconActivity extends AppCompatActivity {

    private ProximityManager proximityManager;
    private List<IBeaconDevice> mBeacon = new ArrayList<>();
    private BeaconAdapter beaconAdapter;
    private Toast discoveredToast;
    private Toast lostToast;
    private Button availableButtons;

    public static final int REQUEST_CODE_PERMISSIONS = 100;
    private final String DISCOVERED = "Beacon discovered!";
    private final String LOST = "Beacon lost!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        discoveredToast = Toast.makeText(getApplicationContext(), DISCOVERED, Toast.LENGTH_SHORT);
        lostToast = Toast.makeText(getApplicationContext(), LOST, Toast.LENGTH_SHORT);
        setContentView(R.layout.activity_available_beacons);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.beacon);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        beaconAdapter = new BeaconAdapter(mBeacon, recyclerView);
        recyclerView.setAdapter(beaconAdapter);
        KontaktSDK.initialize("TTMLKMtxDZnZTDymSmRdSgKOjFqBQmRB");

        proximityManager = ProximityManagerFactory.create(this);
        proximityManager.setIBeaconListener(createIBeaconListener());
        checkPermissions();
    }

    private void checkPermissions() {
        int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED != checkSelfPermissionResult) {
            //Permission not granted so we ask for it. Results are handled in onRequestPermissionsResult() callback.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_CODE_PERMISSIONS == requestCode) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location permissions are mandatory to use BLE features on Android 6.0 or higher", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScanning();
    }

    @Override
    protected void onStop() {
        proximityManager.stopScanning();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        proximityManager.disconnect();
        proximityManager = null;
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {

            @Override
            public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {
                discoveredToast.show();
            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                mBeacon.clear();
                mBeacon.addAll(iBeacons);
                sortArrayDistance();
                beaconAdapter.notifyDataSetChanged();
            }

            @Override
            public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {
                lostToast.show();
                if(mBeacon.size()<2){
                    mBeacon.clear();
                    beaconAdapter.notifyDataSetChanged();
                }
            }

            private void sortArrayDistance() {
                Collections.sort(mBeacon,new Comparator<IBeaconDevice>() {
                    @Override
                    public int compare(IBeaconDevice o1, IBeaconDevice o2) {
                        if(o1.getDistance()>o2.getDistance()){
                            return 1;
                        }
                        else if(o1.getDistance()<o2.getDistance()){
                            return -1;
                        }
                        return 0;
                    }
                });
            }
        };
    }
}