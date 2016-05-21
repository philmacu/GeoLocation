package com.macuait.phil.geolocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public TextView textLong, textLat;
    public CheckBox checkBox;
    public boolean gotAfix;
    public LocationManager locationManager; // across the class so after a fix we can go quite!
    protected LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotAfix = false;
        textLat = (TextView) findViewById(R.id.textLat);
        textLong = (TextView) findViewById(R.id.textLong);
        checkBox = (CheckBox)findViewById(R.id.gpsActive);
        checkBox.setChecked(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // create the listener, this references an inner class that WE must create
        locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

    }


    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double plong = location.getLongitude();
                double plat = location.getLatitude();
                textLong.setText(Double.toString(plong));
                textLat.setText(Double.toString(plat));
                gotAfix = true;
            }
            if (gotAfix){
                MainActivity.killGPS();
                locationManager.removeUpdates(locationListener);
                checkBox.setChecked(false);
            }
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
    }

    private static void killGPS() {
        // battery saver - one fix then GPS is over

    }
}
