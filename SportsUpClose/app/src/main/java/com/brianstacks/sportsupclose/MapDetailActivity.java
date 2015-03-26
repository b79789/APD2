package com.brianstacks.sportsupclose;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.brianstacks.sportsupclose.fragments.AdFragment;
import com.brianstacks.sportsupclose.fragments.DetailsFragment;


public class MapDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);
        if (getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        FragmentTransaction trans = getFragmentManager().beginTransaction();

        if (savedInstanceState==null){
            AdFragment adFragment = new AdFragment();

            trans.add(adFragment,AdFragment.TAG);
            trans.replace(R.id.container, new DetailsFragment()).commit();
        }

    }
}
