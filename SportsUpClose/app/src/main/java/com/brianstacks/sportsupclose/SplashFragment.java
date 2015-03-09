package com.brianstacks.sportsupclose;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class SplashFragment extends Fragment implements LocationListener{
    public final String TAG = "SplashFragment.TAG";
    LocationManager mManager;
    GooglePlace googlePlace;
    private OnSplashscreenListener mListener;
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = (LocationManager)getActivity().getSystemService(MainActivity.LOCATION_SERVICE);
        googlePlace = new GooglePlace();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        enableGps();
        Button button = (Button)getActivity().findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(googlePlace.getLat(),googlePlace.getLon());
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSplashscreenListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSplashscreenListener {
        public void onFragmentInteraction(double lat,double lon);
    }

    private void enableGps() {
        if(mManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);


            Location loc = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) {
                googlePlace.setLat(loc.getLatitude());
                googlePlace.setLon(loc.getLongitude());
            }

        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Unavailable")
                    .setMessage("Please enable GPS in the system settings.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                        }

                    })
                    .show();
        }
    }


}
