package com.project.edn.washit.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.edn.washit.Adapter.GooglePlacesAutocompleteAdapter;
import com.project.edn.washit.Adapter.PlacesAutoCompleteAdapter;
import com.project.edn.washit.Config;
import com.project.edn.washit.R;

import java.io.IOException;
import java.util.List;

public class SetAddress extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleApiClient.OnConnectionFailedListener {

    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private GoogleMap globalGoogleMap;
    private boolean isLocationFound = false;

    protected GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView autoCompView;
    private TextView address;
    private CameraPosition myPosition;
    private Geocoder geocoder;
    private PlacesAutoCompleteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mapFragment = new SupportMapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_map1, mapFragment);
        fragmentTransaction.commit();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */,
                        this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(SetAddress.this);
        address = (TextView) findViewById(R.id.address);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.search_location);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this,R.layout.custom_autocompletelist));
        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                LatLng latLng = getLocationFromAddress(item.description.toString());
                globalGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        LinearLayout pin = (LinearLayout) findViewById(R.id.pick);
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                autoCompView.setText(address.getText().toString());
                data.putExtra("LAT", myPosition.target.latitude);
                data.putExtra("LON", myPosition.target.longitude);
                data.putExtra("ADDRESS", address.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("place", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
        }
    };



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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        globalGoogleMap = googleMap;

        if (ContextCompat.checkSelfPermission(SetAddress.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SetAddress.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SetAddress.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);

        }
        globalGoogleMap.setMyLocationEnabled(true);
        globalGoogleMap.setPadding(0, 150, 0, 0);


        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        globalGoogleMap.moveCamera(zoom);

        globalGoogleMap.setOnMyLocationChangeListener(SetAddress.this);
        globalGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-7.984012707227514,
                112.62083536013961)));

        globalGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(final CameraPosition cameraPosition) {
                myPosition = cameraPosition;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final List<Address> addresses = geocoder.getFromLocation(cameraPosition.target.latitude,
                                    cameraPosition.target.longitude, 5);
                            if (addresses.size() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        address.setText(addresses.get(0).getAddressLine(0));
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (!isLocationFound) {
            isLocationFound = true;
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
            globalGoogleMap.moveCamera(zoom);

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));

            globalGoogleMap.animateCamera(center);
        }
    }
    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1;
        try {

            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
