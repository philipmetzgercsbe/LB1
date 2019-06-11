package com.example.lb1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lb1.Models.ImageData;
import com.example.lb1.SQL.SQLiteCursor;
import com.example.lb1.SQL.SQLiteHelper;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.lb1.MainActivity.CurrLocation;
import static com.example.lb1.MainActivity.CursorDriver;
import static com.example.lb1.MainActivity.Db;
import static com.example.lb1.MainActivity.Locationmanager;
import static com.example.lb1.MainActivity.MapCritera;
import static com.example.lb1.MainActivity.Geocode;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements OnMapReadyCallback {
    private View View;
    private MapView ViewMap;
    private GoogleMap GMap;
    private TextView mTextMessage;
    private Spinner sTimer;
    private static final String MAPVIEW_BUNDLE_KEY  = Resources.getSystem().getString(R.string.google_maps_key);
    private OnFragmentInteractionListener mListener;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ArrayList<ImageData> Markers= new ArrayList<>();

    public Home() {
        // Required empty public constructor
    }


    public static Home newInstance() {
        Home fragment = new Home();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteCursor Helper = new SQLiteCursor(CursorDriver,null,null);
        Markers = Helper.getAll(Db);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView();
        Bundle mapViewBundle = null;

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        ViewMap.getMapAsync(this);
        // Inflate the layout for this fragment
        View =  inflater.inflate(R.layout.fragment_home, container, false);
        return View;
    }

    private void initView() {
        sTimer = View.findViewById(R.id.spinner);
        mTextMessage = (TextView) View.findViewById(R.id.message);
        ViewMap = View.findViewById(R.id.mapView);
        GMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        GMap.setTrafficEnabled(false);
        GMap.setIndoorEnabled(false);
        GMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        GMap.getUiSettings().setZoomGesturesEnabled(false);
        GMap.setMaxZoomPreference(6.0f);
        GMap.setMinZoomPreference(2.0f);
        Geocode = new Geocoder(getContext());

        boolean isEnabled = Locationmanager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        String provider = Locationmanager.getBestProvider(MapCritera, true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GMap = googleMap;
        LatLng Location = new LatLng(CurrLocation.getLongitude(),CurrLocation.getLongitude());
        GMap.addMarker(new MarkerOptions().position(Location));
        for(ImageData img: Markers){
            LatLng MarkLoc = new LatLng(img.getLongitude(),img.getLatitude());
            MarkerOptions MarkerOpts = new MarkerOptions();
            MarkerOpts.title(img.Name).position(MarkLoc);
            GMap.addMarker(MarkerOpts).setIcon(BitmapDescriptorFactory.fromBitmap(img.Image));

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
