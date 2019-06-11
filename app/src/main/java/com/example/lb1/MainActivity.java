package com.example.lb1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lb1.Models.ImageData;
import com.example.lb1.SQL.SQLiteHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener,imagesFragment.OnListFragmentInteractionListener,Settings.OnFragmentInteractionListener {

    //TODO:  Camera Module + Writing to DB, Fix Route(LocManager every 2 min), ImageMarker
    public static SQLiteDatabase Db;
    public static SQLiteCursorDriver CursorDriver;
    public static Criteria MapCritera;
    public static LocationManager Locationmanager;
    public static Geocoder Geocode;
    public static Location CurrLocation;
    private LocationListener LocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            CurrLocation = new Location(location);
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
    private FragmentManager Manager = getSupportFragmentManager();
    private HandlerThread LocationHandler = new HandlerThread("LocationHandler");
    private Looper LocationLooper;
    private Runnable LocationRunner = new Runnable() {
        @Override
        public void run() {
            GetLocationData();
        }
    };
    private static final long HIGH_POWER_FINE = (60 * 1000) * 2;
    private static final long HIGH_POWER_COARSE = (60 * 1000) * 4;
    private static final long MEDIUM_POWER_FINE = (60 * 1000) * 5;
    private static final long MEDIUM_POWER_COARSE = (60 * 1000) * 7;
    private static final long LOW_POWER_FINE = (60 * 1000) * 12;
    private static final long LOW_POWER_COARSE = (60 * 1000) * 15;
    private ActivityCompat Permissions;

    FragmentTransaction fragmentTransaction = Manager.beginTransaction();

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Home Home = new Home();
                    fragmentTransaction.replace(R.id.HomeFragment, Home);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_last_images:
                    imagesFragment Images = new imagesFragment();
                    fragmentTransaction.replace(R.id.ImagesFragment, Images);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_settings:
                    Settings Settings = new Settings();
                    fragmentTransaction.replace(R.id.Settingfragment, Settings);
                    fragmentTransaction.commit();
                    return true;
                case R.id.camera:
                    //TODO: Use Camera Intent here
            }
            return false;
        }
    };

    public MainActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        setContentView(R.layout.activity_main);
        Startup();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void Startup() {
        SQLiteHelper Helper= new SQLiteHelper(this);
        Db = Helper.getWritableDatabase();
        fragmentTransaction.add(R.id.HomeFragment, new Home());
        fragmentTransaction.commit();
        Locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationHandler.start();
        LocationLooper = LocationHandler.getLooper();
        fragmentTransaction.replace(R.id.HomeFragment, new Home());
    }


    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
            case 0:
                return;
                // all permissions were granted
                // initialize();
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationRunner.run();
    }

    private void GetLocationData() {
        Locationmanager.getBestProvider(MapCritera, false);
        LocationHandler.start();
        LocationLooper = LocationHandler.getLooper();
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
        switch (MapCritera.getPowerRequirement()) {
            case Criteria.POWER_HIGH:
                if (MapCritera.getAccuracy() == Criteria.ACCURACY_FINE) {

                    Locationmanager.requestLocationUpdates(HIGH_POWER_FINE, 5, MapCritera, LocationListener, LocationLooper);
                } else {
                    Locationmanager.requestLocationUpdates(HIGH_POWER_COARSE, 5,MapCritera, LocationListener,LocationLooper);
                }
                break;
            case Criteria.POWER_MEDIUM:
                if (MapCritera.getAccuracy() == Criteria.ACCURACY_FINE) {
                    Locationmanager.requestLocationUpdates(MEDIUM_POWER_FINE,10, MapCritera,LocationListener,LocationLooper);
                } else {
                    Locationmanager.requestLocationUpdates(MEDIUM_POWER_COARSE, 10, MapCritera,LocationListener,LocationLooper);
                }
                break;
            case Criteria.POWER_LOW:
                if (MapCritera.getAccuracy() == Criteria.ACCURACY_FINE) {
                    Locationmanager.requestLocationUpdates(LOW_POWER_FINE,10, MapCritera,LocationListener,LocationLooper);
                } else {
                    Locationmanager.requestLocationUpdates(LOW_POWER_COARSE,20, MapCritera,LocationListener,LocationLooper);
                }
                break;
            default:
                Locationmanager.requestLocationUpdates(MEDIUM_POWER_COARSE,10,getDefaults(), LocationListener, LocationLooper);


        }
    }


    private Criteria getDefaults(){
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_MEDIUM);
        crit.setPowerRequirement(Criteria.POWER_MEDIUM);
        return crit;
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationHandler.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationHandler.quit();
        LocationLooper.quit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(ImageData item) {

    }
}
