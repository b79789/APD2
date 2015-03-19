package com.brianstacks.sportsupclose.fragments;


import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.brianstacks.sportsupclose.DataAdapter;
import com.brianstacks.sportsupclose.GooglePlace;
import com.brianstacks.sportsupclose.MapDetailActivity;
import com.brianstacks.sportsupclose.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PlacesListFragment extends Fragment {

    public final String TAG = "PlacesListFragment.TAG";
    ArrayList<GooglePlace> venuesList;
    final String GOOGLE_KEY = "AIzaSyB9iOw6wF4FwbOdUTZYiU_MxsbfWM5iMOI";
            // this is the center of Home
     String latitude;
     String longtitude ;
    String sortVal;
    ArrayAdapter myAdapter;
    ListView listView;
    List<String> listTitle;

    public static PlacesListFragment newInstance(double param1, double param2) {
        PlacesListFragment fragment = new PlacesListFragment();
        Bundle args = new Bundle();
        args.putDouble("lat",param1);
        args.putDouble("lon",param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlacesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listTitle = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        if (getArguments()!=null) {
            if (getArguments().containsKey("lat") && getArguments().containsKey("lon")) {
                latitude = String.valueOf(getArguments().getDouble("lat"));
                longtitude = String.valueOf(getArguments().getDouble("lon"));
            } else {
                latitude = "0";
                longtitude = "0";
            }

        }

        listView= (ListView)getActivity().findViewById(R.id.myList);
        // start the AsyncTask that makes the call for the venus search.
        new googleplaces().execute();
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.mySpinner);
        String[] items = new String[]{"Closest", "Most Popular"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        sortVal="&rankby=distance";
                        new googleplaces().execute();
                        break;
                    case 1:
                        sortVal="&radius=50000";
                        new googleplaces().execute();
                        // Whatever you want to happen when the second item gets selected
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class googleplaces extends AsyncTask<View, Void, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longtitude + sortVal+"&keyword=sports&key=" + GOOGLE_KEY);
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp == null) {
                // we have an error to the call
                Log.v("onPostExecute","we have an error to the call");
                // we can also stop the progress bar
            } else {
                // all things went right

                // parse Google places search result
                venuesList = (ArrayList<GooglePlace>) parseGoogleParse(temp);



                for (int i = 0; i < venuesList.size(); i++) {
                    // make a list of the places that are loaded in the list.
                    listTitle.add(i, venuesList.get(i).getName() + "\n" + venuesList.get(i).getAddress()  + venuesList.get(i).getOpenNow());
                }

                Location selected_location=new Location("locationA");
                selected_location.setLatitude(getArguments().getDouble("lat"));
                selected_location.setLongitude(getArguments().getDouble("lon"));
                Location near_locations=new Location("locationA");
                near_locations.setLatitude(17.375775);
                near_locations.setLongitude(78.469218);


                double distance=selected_location.distanceTo(near_locations);
                double test= (double) distance/ 1280;
                Log.v("distance", String.valueOf(distance));

                if (getActivity()!=null){
                    // set the results to the list
                    // and show them in the xml
                    DataAdapter dataAdapter = new DataAdapter(getActivity(), venuesList);
                    myAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_layout, R.id.listText, listTitle);
                    listView.setAdapter(dataAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), MapDetailActivity.class);
                            intent.putExtra("ListObject", venuesList.get(position));
                            intent.putExtra("userLat",getArguments().getDouble("lat"));
                            intent.putExtra("userLon",getArguments().getDouble("lon"));
                            startActivity(intent);
                        }
                    });
                }else {
                    Log.v("Error","Error in onPostExecute in PlaceslistFragment");
                }

            }
        }
    }

    public static String makeCall(String url) {

        // string buffers the url

        String replyString = "";

        // instantiate an HttpClient

        HttpClient httpclient = new DefaultHttpClient();

        // instantiate an HttpGet

        HttpGet httpget = new HttpGet(url);

        try {

            // get the response of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result

            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d("replyString",replyString);

        // trim the whitespaces

        return replyString.trim();

    }

    private static ArrayList parseGoogleParse(final String response) {

        ArrayList temp = new ArrayList();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    GooglePlace poi = new GooglePlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));
                        poi.setAddress(jsonArray.getJSONObject(i).optString("vicinity"));
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("Open: YES");
                                } else {
                                    poi.setOpenNow("Open: NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Open: ?");
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + " " + poi.getCategory());

                            }
                        }
                        if (jsonArray.getJSONObject(i).has("geometry")){
                            JSONObject geoObject = jsonArray.getJSONObject(i).getJSONObject("geometry");
                            JSONObject locationObject = geoObject.getJSONObject("location");
                            double lat = (double) locationObject.get("lat");
                            double lon = (double) locationObject.get("lng");
                            poi.setLat(lat);
                            poi.setLon(lon);

                        }

                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return temp;

    }


}
