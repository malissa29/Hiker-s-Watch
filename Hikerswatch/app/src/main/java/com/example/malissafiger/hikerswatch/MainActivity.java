package com.example.malissafiger.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void onRequestPermissionsResult (int requestCode, @NonNull String [] permissions, @NonNull int [] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);


            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                StartListening();
            }

    }

    public void StartListening ()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
    }

    public void updateLocationInfo (Location location)
    {
        Log.i("loc:", location.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
                TextView LattextView= (TextView)findViewById(R.id.LattextView);

                TextView LongtextView= (TextView)findViewById(R.id.LongtextView);

                TextView AlttextView= (TextView)findViewById(R.id.AlttextView);

                TextView acctextView= (TextView)findViewById(R.id.acctextView);

                LattextView.setText("Latitude: " + location.getLatitude());

                LongtextView.setText("Longitude: " + location.getLongitude());

                AlttextView.setText("Altitude: " + location.getAltitude());

                acctextView.setText("Address: " + location.getAccuracy());

                Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    String address = "Could not find Address!";
                    List <Address> listaddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (listaddresses != null && listaddresses.size() > 0) {
                        Log.i("placeinfo", listaddresses.get(0).toString());

                        address="Address: \n";

                        if(listaddresses.get(0).getSubThoroughfare() != null){
                            address+= listaddresses.get(0).getSubThoroughfare();
                        }

                        if(listaddresses.get(0).getThoroughfare() != null){
                            address+= listaddresses.get(0).getThoroughfare()+ "\n";
                        }

                        if(listaddresses.get(0).getLocality() != null){
                            address+= listaddresses.get(0).getLocality()+ "\n";
                        }

                        if(listaddresses.get(0).getPostalCode() != null){
                            address+= listaddresses.get(0).getPostalCode()+ "\n";
                        }

                        if(listaddresses.get(0).getCountryName() != null){
                            address+= listaddresses.get(0).getCountryName()+ "\n";
                        }
                    }

                    TextView AddTextView =(TextView)findViewById(R.id.AddtextView);
                    AddTextView.setText(address);


                } catch (IOException e) {


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
        };

        if(Build.VERSION.SDK_INT < 23)
        {
            StartListening();

        }
        else
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }

    }
}
