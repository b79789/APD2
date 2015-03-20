package com.brianstacks.sportsupclose;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.brianstacks.sportsupclose.fragments.AdFragment;
import com.brianstacks.sportsupclose.fragments.PlacesListFragment;
import com.brianstacks.sportsupclose.fragments.SplashFragment;


public class MainActivity extends Activity implements SplashFragment.OnSplashscreenListener {

    public static final String MY_PREFS_NAME = "MY_PREFS_NAME";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        float value = prefs.getFloat("lat",0);
        Log.v("latVal", String.valueOf(value));
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        //AdFragment adFragment = new AdFragment();
        //trans.add(adFragment,AdFragment.TAG);

        if (value != 0){
            float lat = prefs.getFloat("lat", 0);//0 is the default value.
            float lon = prefs.getFloat("lon", 0); //0 is the default value.
            PlacesListFragment placesListFragment =PlacesListFragment.newInstance(lat,lon);
            trans.replace(R.id.fragmentContainer, placesListFragment, placesListFragment.TAG);
            trans.commit();
        }else{
            SplashFragment splashFragment = new SplashFragment();
            trans.replace(R.id.fragmentContainer, splashFragment, splashFragment.TAG);
            trans.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.resetPrefs){
            SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME, 0);
            preferences.edit().clear().apply();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onFragmentInteraction(double lat, double lon) {
        preferences = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("lat", (float) lat);
        editor.putFloat("lon", (float) lon);
        editor.apply();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        PlacesListFragment placesListFragment =PlacesListFragment.newInstance(lat,lon);
        trans.replace(R.id.fragmentContainer, placesListFragment, placesListFragment.TAG);
        trans.commit();
    }
}
