package com.mertogurcu.jsonisleme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.renderscript.Double2;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.nio.DoubleBuffer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    TextView tvMekan,mekangor;

    String x, y, z, w, mekanAdi;
    double en, boy,koordinatEn,koordinatBoy;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvMekan = (TextView) findViewById(R.id.mekanAditv);


        Bundle extras = getIntent().getExtras();
        Double x = extras.getDouble("bakilanEnlem");
        Double y = extras.getDouble("bakilanBoylam");
        String mekanAD = extras.getString("mekan");

        en = x;
        boy = y;
        mekanAdi = mekanAD;

        tvMekan.setText(mekanAdi);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);




    }


    @Override
    public void onLocationChanged(Location location) {
        double enlem = location.getLatitude();
        double boylam = location.getLongitude();
        koordinatEn = enlem;
        koordinatBoy = boylam;

        LatLng buradayim = new LatLng(koordinatEn,koordinatBoy);
        mMap.addMarker(new MarkerOptions().position(buradayim).title("Şuanda Buradayım").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        if(s=="gps"){
            Toast.makeText(getApplicationContext(), "GPS is off", Toast.LENGTH_LONG).show();

            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        Log.i("lm_disabled",s);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);


        // Add a marker in Sydney and move the camera

        LatLng mekan = new LatLng(en,boy);
        mMap.addMarker(new MarkerOptions().position(mekan).title("Mekan Burada !").snippet("Aradığınız mekan buradadır.").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mekan));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(en, boy)).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


      /*  Polyline polyline1 = googleMap.addPolyline(new PolylineOptions().add(koordinatEn,koordinatBoy).add(en,boy).width(5).color(3).geodesic(true);
        nMap.addPolyline(polyline1);*/

    }

}
