package com.chefswithoutlimits.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by db on 10/20/2017.
 */
@SuppressWarnings("ALL")
public class LocationService extends Service {

    public LocationManager locationManager;
    public MyLocationListener listener;
   // public Location previousBestLocation = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
       // Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();


        try {

            /*locationDialog=new ProgressDialog(OfflineSearchKitchen.this);
            locationDialog.setMessage("loading...");
            locationDialog.show();
            locationDialog.setCancelable(false);
            locationDialog.setCanceledOnTouchOutside(false);*/

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            listener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        }catch (Exception e){
            Log.d("Exception","Exception.....................");
            /*if(locationDialog!=null)
                locationDialog.dismiss();*/
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.d("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }


    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location loc)
        {
            Log.d("******", "Location changed");
            //if(isBetterLocation(loc, previousBestLocation)) {
            loc.getLatitude();
            loc.getLongitude();
            System.out.println(loc.getLatitude()+"current location"+loc.getLongitude());
            // sendBroadcast(intent);
            // sendtoserver(loc.getLatitude(),loc.getLongitude());

            String stringLatitude = String.valueOf(loc.getLatitude());
            String stringLongitude = String.valueOf(loc.getLongitude());


            ConstantUtils.Latitude=String.valueOf(loc.getLatitude());
            ConstantUtils.Longitude=String.valueOf(loc.getLongitude());

            Log.e("stringLatitude ",stringLatitude);
            Log.e("stringLongitude ",stringLongitude);

           /* if(locationDialog!=null) {
                locationDialog.dismiss();
            }*/

           // locationManager.removeUpdates(listener);
           // GetLocation(stringLatitude,stringLongitude);



            Log.d("Location Post : ",loc.getLatitude()+" current location "+loc.getLongitude());

            // }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
            /*if(locationDialog!=null) {
                locationDialog.dismiss();
            }*/
            //doCountryList();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
            /*if(locationDialog!=null) {
                locationDialog.dismiss();
            }*/
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            /*if(locationDialog!=null) {
                locationDialog.dismiss();
            }*/
        }


    }
}