package com.finalYearproject.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.finalYearproject.womensafetyapp.Adapter.HomeAdapter;
import com.finalYearproject.womensafetyapp.util.ItemClickSupport;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    private TextView title;
    private Context mContext;
    private float cornerRadius = (float) 15.0;
    private RecyclerView admin_home_list;
    private String fCMTokenSTring;
    private String android_id;
    SweetAlertDialog pDialog;
    private String latitude,longitude;
    private Double doubleLat,doubleLong;
    LocationManager locationManager;
    Boolean gpsEnabled = false;
    private Activity activity;

    List<String> name = new ArrayList<String>();
    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        activity = this;
        name = Arrays.asList("SOS","Medical Services","Police Stations","First Aid","Self Defence","Profile","Logout");
        title = findViewById(R.id.title_text);
        title.setText("Home");
        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        //registerDeviceToken();
        admin_home_list = (AnimatedRecyclerView) findViewById(R.id.watchman_menu_list);
        HomeAdapter watchManmenuList = new HomeAdapter(mContext,name);
        admin_home_list.setAdapter(watchManmenuList);
        watchManmenuList.notifyDataSetChanged();
        admin_home_list.scheduleLayoutAnimation();
        SharedPreferences shared = mContext.getSharedPreferences("MyPreferences", MODE_PRIVATE);

        ItemClickSupport.addTo(admin_home_list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                try
                {
                    if(position == 0){
                        String emergencyContact = shared.getString("user_emergency_contact","");
                        if (emergencyContact.equals("")){
                            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Alert")
                                    .setContentText("No Emergency Contact Added. Add Emergency Contact now?")
                                    .setCancelText("No")
                                    .setConfirmText("Yes")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.hide();
                                            Intent i = new Intent(mContext.getApplicationContext(), UserProfileActivity.class);
                                            mContext.startActivity(i);
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                        }
                                    })
                                    .show();
                        }
                        else{
                            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Alert")
                                    .setContentText("This will share your location via sms and via application to your emergency contact")
                                    .setCancelText("No")
                                    .setConfirmText("Yes")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.hide();
                                            getLocation();
                                            latitude = String.valueOf(doubleLat);
                                            longitude = String.valueOf(doubleLong);
                                            String url = "http://maps.google.com/maps?z=12&t=m&q=" + latitude + "," + longitude;


                                            sendSMS(emergencyContact,"The current location of the user is " + url);
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                        }
                                    })
                                    .show();
                        }

                    }
                    else if (position == 1){
                        getLocation();
                        String uri = "geo:"+ latitude + "," + longitude +"?q=hospitals+near+me";
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                    }
                    else if (position == 2){
                        getLocation();
                        String uri = "geo:"+ latitude + "," + longitude +"?q=police+stations+near+me";
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                    }
                    else if (position == 3){
                        Intent i = new Intent(mContext.getApplicationContext(), FirstAidActivity.class);
                        mContext.startActivity(i);
                    }
                    else if (position == 4){
                        Intent i = new Intent(mContext.getApplicationContext(), SelfDefenceActivity.class);
                        mContext.startActivity(i);
                    }
                    else if (position == 5){
                        Intent i = new Intent(mContext.getApplicationContext(), UserProfileActivity.class);
                        mContext.startActivity(i);
                    }
                    else{

                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Alert")
                                .setContentText("Are you sure you want to logout?")
                                .setCancelText("No")
                                .setConfirmText("Yes")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        SharedPreferences shared = mContext.getSharedPreferences("MyPreferences", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = shared.edit();
                                        editor.putBoolean("isLoggedIn",false);
                                        editor.commit();
                                        Intent i = new Intent(mContext.getApplicationContext(), LoginActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        mContext.startActivity(i);
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();

                    }

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else{
            statusCheck();
        }

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getAddress();
        }
    }

    private void getAddress() {
        Location locationGPS = getLastKnownLocation();
        if (locationGPS != null) {
            doubleLat= locationGPS.getLatitude();
            doubleLong = locationGPS.getLongitude();

        }
        else{
            getLastKnownLocation();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            getAddress();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                    locationManager.requestLocationUpdates(provider, 5000, 0, locationListener, Looper.getMainLooper());
                    continue;

                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    public void statusCheck() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        }
        else{
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    getLocation();
                }
            });
        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}