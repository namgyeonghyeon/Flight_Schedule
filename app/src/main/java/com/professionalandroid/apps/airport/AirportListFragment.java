package com.professionalandroid.apps.airport;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class AirportListFragment extends Fragment {
    private ArrayList<Airport> mAirports = new ArrayList<Airport>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeToRefreshView;
    private AirportRecyclerViewAdapter mAirportAdapter =
            new AirportRecyclerViewAdapter(mAirports);
    protected AirportViewModel airportViewModel;
    private int mWind = 0;

    public AirportListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airport_list,
                container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mSwipeToRefreshView = view.findViewById(R.id.swiperefresh);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAirportAdapter);

        mSwipeToRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateAirports();
            }
        });
    }

    public void setAirports(List<Airport> airports) {
        updateFromPreferences();

        for(Airport airport: airports) {
            if (airport.getWind() >= mWind){
                if(!mAirports.contains(airport)){
                    mAirports.add(airport);
                    mAirportAdapter.notifyItemChanged(mAirports.indexOf(airport));
                }
            }
        }

        if (mAirports != null && mAirports.size() > 0) {
            for (int i = mAirports.size() - 1; i >= 0; i--) {
                if (mAirports.get(i).getWind() < mWind) {
                    mAirports.remove(i);
                    mAirportAdapter.notifyItemRemoved(i);
                }
            }
        }

        mSwipeToRefreshView.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        airportViewModel = ViewModelProviders.of(getActivity()).get(AirportViewModel.class);
        airportViewModel.getAirports().observe(this,
                new Observer<List<Airport>>() {
                    @Override
                    public void onChanged(@Nullable List<Airport> airports) {
                        if(airports != null)
                            setAirports(airports);
                    }
                });
        // OnSharedPreferenceChangeListener를 등록한다.
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(mPrefListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (PreferenceActivity.PREF_MIN_MAG.equals(key)) {
                        List<Airport> airports =
                                airportViewModel.getAirports().getValue();
                        if (airports != null)
                            setAirports(airports);
                    }
                }
            };

    public interface OnListFragmentInteractionListener{
        void onListFragmentRefreshRequested();
    }

    private OnListFragmentInteractionListener mListener;

    protected void updateAirports() {
        if(mListener != null)
            mListener.onListFragmentRefreshRequested();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateFromPreferences() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        mWind = Integer.parseInt(prefs.getString(PreferenceActivity.PREF_MIN_MAG, "1"));
    }
}
