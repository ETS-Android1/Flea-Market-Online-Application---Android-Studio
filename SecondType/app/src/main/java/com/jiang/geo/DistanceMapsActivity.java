package com.jiang.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.ChangeEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.jiang.geo.util.AndroidUIUtil;
import com.jiang.geo.util.BitmapUtil;
import com.jiang.geo.util.LocationUtils;

import java.util.ArrayList;
import java.util.List;

public class DistanceMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener, ChangeEventListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    public static Location lastlocation;
    public static final int REQUEST_LOCATION_CODE = 99;
    double latitude, longitude;
    private View mMapView;
    private TextView dis;
    public static DistanceMapsActivity sLocationMapsActivity;
    public Handler mHandler = new Handler();

    // public static LatLng l1 = new LatLng(40.7128, -74.0060), l2 = new LatLng(40.7328, -73.8910);
    public static LatLng l1 = new LatLng(LocationUtils.sLastLocation.getLatitude(), LocationUtils.sLastLocation.getLongitude()), l2 = new LatLng(40.7328, -73.8910);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sey layout
        sLocationMapsActivity = this;
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        findViewById(R.id.ccc).setVisibility(View.VISIBLE);
        mMapView = mapFragment.getView();
        dis = findViewById(R.id.cc);
        findViewById(R.id.xx).setVisibility(View.VISIBLE);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (client == null) {
                                    bulidGoogleApiClient(); // 创建api client
                                }
                                if (ActivityCompat.checkSelfPermission(DistanceMapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DistanceMapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                mMap.setMyLocationEnabled(true); //
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }, 3000L);
                    }
                } else {
                    // failed
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    /**
     * 地图准备就绪
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
        // map 对象
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                updateLocation();
            }
        });
        // start
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            // check
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }
        }

    }

    // 生成google api client connect
    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect(); // connect
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
    }

    @Override
    public void onDataChanged() {
    }

    @Override
    public void onError(@NonNull DatabaseError databaseError) {
    }

    public void onLocationChanged(Location location) {
        boolean first = lastlocation == null;
        lastlocation = location;
        // mMap.clear();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        // updateLocation();
    }

    public void updateLocation() {
        if (l1 != null && l2 != null) {
            mMap.clear();
            mMap.setOnMarkerClickListener(this);
            double latitude = l1.latitude;
            double longitude = l1.longitude;
            LatLng ll = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(ll);
            markerOptions.title("Your location");
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.getBitmapFromRes(this, R.mipmap.m1)));
            mMap.addMarker(markerOptions).setTag(l1);
            double latitude2 = l2.latitude;
            double longitude2 = l2.longitude;
            LatLng ll2 = new LatLng(latitude2, longitude2);
            MarkerOptions markerOptions2 = new MarkerOptions();
            markerOptions2.position(ll2);
            markerOptions2.title("Another location");
            markerOptions2.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.getBitmapFromRes(this, R.mipmap.m2)));
            mMap.addMarker(markerOptions2).setTag(l2);
            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .color(Color.BLUE)
                    .add(l1, l2));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder().include(l1).include(l2).build(), 150));
            mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {
                    Toast.makeText(DistanceMapsActivity.this, "distance is " + AndroidUIUtil.getDistance(l1.longitude, l1.latitude, l2.longitude, l2.latitude), Toast.LENGTH_LONG).show();
                }
            });
            dis.setVisibility(View.VISIBLE);
            dis.setText("distance is " + AndroidUIUtil.getDistance(l1.longitude, l1.latitude, l2.longitude, l2.latitude));
        } else {
            finish();
        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    public void go(View view) {
        if (isAvilible(this, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + l2.latitude + "," + l2.longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No google map app", Toast.LENGTH_LONG).show();

            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * google api client
     *
     * @param bundle
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // [arameter
        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // request
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
    }

    // 检测定位权限
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    /**
     * google api client failed
     *
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag == l1) {
            Toast.makeText(DistanceMapsActivity.this, "Your location", Toast.LENGTH_LONG).show();
            return true;
        }
        if (tag == l2) {
            Toast.makeText(DistanceMapsActivity.this, "Another location", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
