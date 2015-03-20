package com.brianstacks.sportsupclose;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import com.brianstacks.sportsupclose.fragments.AdFragment;
import com.brianstacks.sportsupclose.fragments.DetailsFragment;
import com.brianstacks.sportsupclose.fragments.DirectionsFragment;


public class MapDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);
        FragmentTransaction trans = getFragmentManager().beginTransaction();

        if (savedInstanceState==null){
            //AdFragment adFragment = new AdFragment();

            //trans.add(adFragment,AdFragment.TAG);
            trans.replace(R.id.container, new DetailsFragment()).commit();
        }

    }
}
