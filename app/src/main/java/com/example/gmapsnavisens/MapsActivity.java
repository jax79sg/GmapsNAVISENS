package com.example.gmapsnavisens;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.hardware.SensorManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sg.gov.dh.trackers.Event;
import sg.gov.dh.trackers.NavisensLocalTracker;
import sg.gov.dh.trackers.TrackerListener;
import sg.gov.dh.utils.Coords;
import android.hardware.Sensor;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private NavisensLocalTracker tracker;
    private NavisensLocationSource navSource;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private CustomSensors customSensor;
    private static final String TAG = "MAIN MAPS APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        customSensor = new CustomSensors(this);
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
        mMap.setMyLocationEnabled(true);
        navSource = new NavisensLocationSource();
        mMap.setLocationSource(navSource);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(1.2799238070559396, 103.81641972249018);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getMaxZoomLevel()));



        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Coords coords = new Coords(latLng.latitude, latLng.longitude,0,0,0,0,0,0,0,"");
                setLocation(coords);
            }
        });
        initTracker();
    }

    /**
     * This is the 2/2 important part related to BFT.
     * This one will allow you to set a coordindate.
     * In the COVID use case, i understand global coordinate is used, so you can inject a LatLong under coords.
     * For the bearing, its hard coded to magnetic direction.
     */
    private void setLocation(Coords coords)
    {
        coords.setGlobalBearing(customSensor.getMagAzimuth());
        tracker.setManualLocation(coords);
    }

    /**
     * This is the 1/2 important part related to BFT.
     */
    protected void initTracker()
    {
        tracker = new NavisensLocalTracker(this);
        tracker.setTrackerListener(new TrackerListener() {
            @Override
            public void onNewCoords(Coords coords) {

                //Receive local coordinates
                Log.d(TAG,"X:"+coords.getX());
                Log.d(TAG,"Y:"+coords.getY());
                Log.d(TAG,"Z:"+coords.getLocalAltitude());
                Log.d(TAG,"bearing:"+coords.getLocalBearing());

                //Receive global coordinates (lat long)
                Log.d(TAG,"Lat:"+coords.getLatitude());
                Log.d(TAG,"Long:"+coords.getLongitude());
                Log.d(TAG,"Alt:"+coords.getGlobalAltitude());
                Log.d(TAG,"Heading:"+coords.getGlobalBearing());

                //Receive current action
                Log.d(TAG,"X:"+coords.getAction());
                Log.d(TAG,"Y:"+coords.getVerticalAction());

                //Do whatever you want with the above
                //E.g. Using the coordinates to plot, using the actions to show animation...etc.
                //In this case, i pump the coordinates into Google Map's LocationSource.
                Location newLoc = new Location("NAVISENS");
                newLoc.setLatitude(coords.getLatitude());
                newLoc.setLongitude(coords.getLongitude());
                newLoc.setAltitude(coords.getGlobalAltitude());
                newLoc.setBearing((float)coords.getGlobalBearing());
                navSource.listener.onLocationChanged(newLoc);
            }

            @Override
            public void onNewEvent(Event event) {

            }
        });
    }
}
