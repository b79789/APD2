package com.brianstacks.sportsupclose.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.brianstacks.sportsupclose.R;
import java.util.ArrayList;


public class DirectionsFragment extends Fragment {
    ListView listView;


    public DirectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_directions, container, false);

    }

   @Override
    public void onActivityCreated(Bundle savedInstanceState){
       super.onActivityCreated(savedInstanceState);
       listView = (ListView)getActivity().findViewById(R.id.directText);
       ArrayList<String> arrayList =(ArrayList) getActivity().getIntent().getSerializableExtra("directionsString");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.row2,R.id.directionText, arrayList);
       listView.setAdapter(arrayAdapter);
       getActivity().invalidateOptionsMenu();

   }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_map_detail, menu);
        getFragmentManager().executePendingTransactions();
        //getVisible method return current visible fragment
        MenuItem item=menu.findItem(R.id.closeAction);
        item.setVisible(true);
        MenuItem item2=menu.findItem(R.id.directionAction);
        item2.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.closeAction:
                getFragmentManager().popBackStack();

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
    public void onPrepareOptionsMenu(Menu menu){
        //code here
        MenuItem item=menu.findItem(R.id.closeAction);
        item.setVisible(true);

    }
}
