package com.brianstacks.sportsupclose.fragments;


import android.content.ClipData;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brianstacks.sportsupclose.GooglePlace;
import com.brianstacks.sportsupclose.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DetailsFragment extends Fragment{
    public static final String TAG = "DetailsFragment.TAG";

    MapView mapView;
    GoogleMap mMap;
    GooglePlace googlePlace;
    ArrayList<GooglePlace> myArrayList;
    HashMap<String, GooglePlace> mMarkers = new HashMap<>();
    String directionsString;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_map_detail, menu);
        getFragmentManager().executePendingTransactions();
        //getVisible method return current visible fragment
            MenuItem item=menu.findItem(R.id.closeAction);
            item.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.closeAction:


                // Not implemented here
                return true;
            case R.id.directionAction:
                // Do Fragment menu item stuff here


                    getFragmentManager().beginTransaction().replace(R.id.container, new DirectionsFragment()).addToBackStack(null).commit();

                return true;
            default:
                break;
        }

        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        // Gets the MapView from the XML layout and creates it
        MapsInitializer.initialize(this.getActivity());
        return v;
    }

    //along with the following
    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        TextView textView = (TextView)getActivity().findViewById(R.id.detailText);
        TextView textView2 = (TextView)getActivity().findViewById(R.id.detailText2);
        TextView textView3 = (TextView)getActivity().findViewById(R.id.detailText3);
        mapView = (MapView) getActivity().findViewById(R.id.map);

        googlePlace = (GooglePlace)getActivity().getIntent().getExtras().get("ListObject");
        Log.v("address",googlePlace.getAddress());
        final double uLat =(double)getActivity().getIntent().getExtras().get("userLat");
        final double uLon =(double)getActivity().getIntent().getExtras().get("userLon");
        myArrayList=new ArrayList<>();
        myArrayList.add(googlePlace);
        textView.setText(googlePlace.getName());
        textView2.setText(googlePlace.getAddress());
        textView3.setText(googlePlace.getCategory());


        // start to get Direction API info
        new ApiDirectionsAsyncTask().execute();
        // get map by id
        mapView.onCreate(savedInstance);
        mMap=mapView.getMap();
        if (mMap!=null) {

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    // create markers
                    MarkerOptions mo = new MarkerOptions()
                            .position(new LatLng(googlePlace.getLat(), googlePlace.getLon())).title(googlePlace.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.x_spot));
                    Marker marker = mMap.addMarker(mo);
                    MarkerOptions mo2 = new MarkerOptions()
                            .position(new LatLng(uLat, uLon)).title("Starting Position")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker));
                    // add markers to map
                    mMap.addMarker(mo);
                    mMap.addMarker(mo2);
                    mMarkers.put(marker.getId(), googlePlace);
                    mMap.setInfoWindowAdapter(new MarkerAdapter());
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                        }
                    });
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                        }
                    });
                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {

                        }
                    });

                    // set the animate camera between the two points here
                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    boundsBuilder.include(new LatLng(uLat, uLon));
                    boundsBuilder.include(new LatLng(googlePlace.getLat(), googlePlace.getLon()));
                    LatLngBounds bounds = boundsBuilder.build();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                }
            });
        }
    }

    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

        TextView mText;
        public MarkerAdapter() {
            mText = new TextView(getActivity());
        }

        @Override
        public View getInfoContents(Marker marker) {
            mText.setText(marker.getTitle());
            return mText;
        }

        @Override
        public View getInfoWindow(Marker marker) {

            return null;
        }
    }

    // Async task to get directions from the Directions API
    public class ApiDirectionsAsyncTask extends AsyncTask<URL, Integer, StringBuilder> {

        private  final String TAG = "ApiDirectionsAsyncTask";
        private static final String API_KEY = "AIzaSyB9iOw6wF4FwbOdUTZYiU_MxsbfWM5iMOI";
        double uLat =(double)getActivity().getIntent().getExtras().get("userLat");
        double uLon =(double)getActivity().getIntent().getExtras().get("userLon");
        @Override
        protected StringBuilder doInBackground(URL... params) {
            Log.i(TAG, "doInBackground of ApiDirectionsAsyncTask");

            HttpURLConnection mUrlConnection = null;
            StringBuilder mJsonResults = new StringBuilder();
            try {

                URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin="+String.valueOf(uLat)+","+String.valueOf(uLon)+"&destination="+String.valueOf(googlePlace.getLat())+","+String.valueOf(googlePlace.getLon())+"&key="+API_KEY);
                mUrlConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(mUrlConnection.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1){
                    mJsonResults.append(buff, 0, read);
                }

            } catch (MalformedURLException e) {
                Log.e(TAG, "Error processing Distance API URL");
                return null;

            } catch (IOException e) {
                System.out.println("Error connecting to Distance API");
                return null;
            } finally {
                if (mUrlConnection != null) {
                    mUrlConnection.disconnect();
                }
            }
            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(mJsonResults.toString());
            //Log.v("mJsonResults",mJsonResults.toString());
            return mJsonResults;
        }
    }

    // A class to parse the  Directions and add the polyline to the map
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    Log.v("lat+lon", String.valueOf(lat + lng));

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(Color.BLUE);
            }

            if (mMap!=null){
                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);
            }

        }
    }

    // A class to parse the Google Directions
    public class DirectionsJSONParser {

        // Receives a JSONObject and returns a list of lists containing latitude and longitude

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;
            ArrayList<String> arrayList = new ArrayList<>();

            try {

                jRoutes = jObject.getJSONArray("routes");

                // getting all routes
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();

                    // getting all legs
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        // getting all steps
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline;
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");

                            directionsString=(String)(((JSONObject) jSteps.get(k)).get("html_instructions"));
                            stripHtml(directionsString);
                            String noHtml = directionsString.replaceAll("<[^>]*>", "");
                            String fixD = noHtml.replace("D","  D");
                            arrayList.add(fixD);
                            if (getActivity()!=null){
                                getActivity().getIntent().putExtra("directionsString",arrayList);

                            }
                            List<LatLng> list = decodePoly(polyline);

                            // getting all points
                            for (int l = 0; l < list.size(); l++) {

                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString(( list.get(l)).latitude));
                                hm.put("lng", Double.toString(( list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }


        // class to get info for the polyline
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

}
