package com.example.hikerslocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
        LocationManager locationManager;
        LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location);
            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation != null){
               updateLocation(lastKnownLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }
    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void updateLocation(Location location){
        TextView latTextView = findViewById(R.id.latTextView);
        TextView lonTextView = findViewById(R.id.lonTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView accTextView = findViewById(R.id.accTextView);

        latTextView.setText("Latitiude : "  + Double.toString((float)location.getLatitude()));
        lonTextView.setText("Longitude : "  + Double.toString((float)location.getLongitude()));
        altTextView.setText("Altitude : " + Double.toString((float)location.getAltitude()));
        accTextView.setText("Accuracy : " + Double.toString((float)location.getAccuracy()));

        String notFoundaddress = "Could not find address :(";

        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        try{
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addressList != null && addressList.size() > 0){
               String address = "";
                address += "Address \n";

                 if(addressList.get(0).getThoroughfare()!=null){
                     address +=addressList.get(0).getThoroughfare() ;
                 }
                 if(addressList.get(0).getLocality() != null){
                     address +=  addressList.get(0).getLocality();
                 }
                 if (addressList.get(0).getAdminArea() != null){
                     address += "\n" + addressList.get(0).getAdminArea();
                 }
                 if(addressList.get(0).getPostalCode() != null){
                     address += "-" + addressList.get(0).getPostalCode();
                 }
                 if(addressList.get(0).getCountryName() != null){
                     address += "\n" + addressList.get(0).getCountryName();
                 }
                 if(addressList.get(0).getCountryCode() != null){
                     address += "-" + addressList.get(0).getCountryCode();
                 }

                if(address == null){
                    addressTextView.setText(notFoundaddress);
                }else{
                    addressTextView.setText(address);
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}