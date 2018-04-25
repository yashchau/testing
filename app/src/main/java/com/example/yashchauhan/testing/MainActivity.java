package com.example.yashchauhan.testing;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

//import org.osmdroid.tileprovider.MapTile;
//import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, LocationSource {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation, mCurrentLocation, mLastKnownLocation, location;
    LatLng mDefaultLocation;
    boolean mLocationPermissionGranted = true;
    CameraPosition mCameraPosition;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    //    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    String mLastUpdateTime;
    static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    int DEFAULT_ZOOM = 20;
    LatLng sydney = new LatLng(12.9676, 77.656);
    Circle circle[] = new Circle[100];
    Context context;
    int i = 0;
    Marker mine[] = new Marker[100];
    LatLng marker[] = new LatLng[100];
    int active= 0;

    Button database,activate;
    FeedReaderContract.FeedReaderDbHelper mDbHelper = new FeedReaderContract.FeedReaderDbHelper(this);
    //    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    LocationListener locationListener;

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        database = (Button) findViewById(R.id.button1);
        activate = (Button) findViewById(R.id.activate);
        mDbHelper.clearDatabase();
        long temp = mDbHelper.adddata("1", "yash");
        Log.d("checking if value added", String.valueOf(temp));
        mDbHelper.adddata("2", "archi");
        mDbHelper.adddata("3", "noah");
        mDbHelper.adddata("4", "Zoe");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());

//        marker[0] = new LatLng(12.967685586,77.65446916222);
//        marker[1] = new LatLng(12.96783784,77.65324674);
//        marker[2] = new LatLng(12.96797212,77.65232942);
//        marker[3] = new LatLng(12.9680714486,77.65169072);
//        marker[4] = new LatLng(12.9682553947,77.650952786);
//
//        mine[0]= createMarker(marker[0], "Mine 1", "Active");
//        mine[1] = createMarker(marker[1], "Mine 2", "Active");
//        mine[2] = createMarker(marker[2], "Mine 3", "Active");
//        mine[3] = createMarker(marker[2], "Mine 4", "Active");
//        mine[4] = createMarker(marker[4], "Mine 5", "Active");
//        mine[5] = createMarker(sydney,"sydney","Active");

        database.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                marker[i] = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                mine[i]= createMarker(marker[i],"mine " + i,"active");
                Log.d("Value of i updating",String.valueOf(i));
                i++;
//                Database_display connectionFragment = new Database_display();
//                FragmentManager manager = getSupportFragmentManager();
//                manager.beginTransaction().replace(R.id.relativelayoutforfragmant, connectionFragment).commit();
            }
        });

        activate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             active =1;

            }
        });

//        locationListener.onLocationChanged(location);

//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                // TODO Auto-generated method stub
//            }
//
//            public void onProviderDisabled(String provider) {
//                // TODO Auto-generated method stub
//            }
//            public void onProviderEnabled(String provider) {
//                // TODO Auto-generated method stub
//            }
//
//            public void onStatusChanged(String provider, int status,
//                                        Bundle extras) {
//                // TODO Auto-generated method stub
//            }
//        });

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location Change", "Location change working");
                Toast.makeText(context, "does this thing work", Toast.LENGTH_LONG).show();

            }
        };
    }



    private void getDeviceLocation() {

        // A step later in the tutorial adds the code to get the device location.

    /*
     * Before getting the device location, you must check location
     * permission, as described earlier in the tutorial. Then:
     * Get the best and most recent location of the device, which may be
     * null in rare cases when a location is not available.
     */
        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.



    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */

    @Override
    public void onConnected(Bundle connectionHint) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int k) {

    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mLocationPermissionGranted = true;
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
//                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 10.0f));


        // Do other setup activities here too, as described elsewhere in this tutorial.

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI();

        // Get the current location of the device and set the position of the map.
//        getDeviceLocation();

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(12.9676, 77.656);
        LatLng marker1 = new LatLng(12.9820, 77.58465);
        LatLng marker2 = new LatLng(12.9774, 77.58969);
        LatLng marker3 = new LatLng(12.9628, 77.60473);
        LatLng marker4 = new LatLng(12.9682, 77.60977);
        LatLng marker5 = new LatLng(12.9535, 77.61480);
//        LatLng marker[] = new LatLng[100];
        marker[0] = new LatLng(12.967685586,77.65446916222);
        marker[1] = new LatLng(12.96783784,77.65324674);
        marker[2] = new LatLng(12.96797212,77.65232942);
        marker[3] = new LatLng(12.9680714486,77.65169072);
        marker[4] = new LatLng(12.9682553947,77.650952786);
//        mine[0]= createMarker(marker1, "Mine 1", "Active");
//        mine[1] = createMarker(marker2, "Mine 2", "Active");
//        mine[2] = createMarker(marker2, "Mine 2", "Active");
//        mine[3] = createMarker(marker3, "Mine 3", "Active");
//        mine[4] = createMarker(marker4, "Mine 4", "Active");
//        mine[5] = createMarker(marker5, "Mine 5", "Active");
//
//        mine[6]= createMarker(marker[0], "Mine 1", "Active");
//        mine[7] = createMarker(marker[1], "Mine 2", "Active");
//        mine[8] = createMarker(marker[2], "Mine 3", "Active");
//        mine[9] = createMarker(marker[2], "Mine 4", "Active");
//        mine[10] = createMarker(marker[4], "Mine 5", "Active");
//        mine[11] = createMarker(sydney,"sydney","Active");
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

       circle[0] = mMap.addCircle(new CircleOptions()
                .center(new LatLng(12.9676, 77.656))
                .radius(10)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 200, 0, 0)));

        circle[0].setFillColor(Color.argb(50, 00, 00, 200));
//        mine[0].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        points = new ArrayList<>();
        lineOptions = new PolylineOptions();
        points.add(marker1);
        points.add(marker2);
        points.add(marker3);
        lineOptions.addAll(points);
        lineOptions.width(10);
        lineOptions.color(Color.RED);
        mMap.addPolyline(lineOptions);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
//                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.mine);

    }

    protected Marker createMarker(LatLng latLng, String title, String snippet) {

//        i++;
//        Log.d("Value of i updating",String.valueOf(i));
//

        circle[i] = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .radius(20)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 200, 0, 0)));

//        i++;
//        Log.d("Value of i updating",String.valueOf(i));


        return mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.mine))
                .position(latLng)
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet + "\n" + latLng.latitude + " , " + latLng.longitude)
                .draggable(true));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(lat2-lat1);  // deg2rad below
        double dLon = Math.toRadians(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c*1000; // Distance in km
        String D = String.valueOf(d);
        return d;
    }




    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
    @Override
    protected void onStart() {
        super.onStart();
        // 2
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(this,String.valueOf(getDistanceFromLatLonInKm(location.getLatitude(),location.getLongitude(),sydney.latitude,sydney.longitude)),Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext()," Dude stop moving so much",Toast.LENGTH_LONG).show();
//        Log.d("onLocationChanged",String.valueOf(i));
//        int j=0;
        for(int j=0;j<i;j=j+1) {
            if (active == 1) {
                Log.d("value of j", String.valueOf(j));
                if (getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), mine[j].getPosition().latitude, mine[j].getPosition().longitude) < 20) {
                    circle[j].setFillColor(Color.argb(50, 00, 00, 200));
                    Log.d("mine number 1:" + i, String.valueOf(getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), mine[j].getPosition().latitude, mine[j].getPosition().longitude)));
                    Log.d("onLocationChanged", "inside if statement");
                    Toast.makeText(this, String.valueOf(getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), mine[j].getPosition().latitude, mine[j].getPosition().longitude)), Toast.LENGTH_LONG).show();

                }
            }
        }

        for(int j=0;j<i;j=j+1) {
            if (active == 1) {
                Log.d("value of j", String.valueOf(j));
                if (getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), mine[j].getPosition().latitude, mine[j].getPosition().longitude) > 20) {
                    circle[j].setFillColor(Color.argb(50, 200, 00, 00));
                    Log.d("mine number 1:" + i, String.valueOf(getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), mine[j].getPosition().latitude, mine[j].getPosition().longitude)));
                    Log.d("onLocationChanged", "inside if statement");
                    Toast.makeText(this, String.valueOf(getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), mine[j].getPosition().latitude, mine[j].getPosition().longitude)), Toast.LENGTH_LONG).show();

                }
            }
        }
//        circle[0].setFillColor(Color.argb(50, 00, 00, 200));

//        Log.d("onLocationChanged","its moving!!!! yaay");
    }
}
