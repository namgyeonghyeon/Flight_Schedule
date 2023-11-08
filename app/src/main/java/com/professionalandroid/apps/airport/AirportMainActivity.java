package com.professionalandroid.apps.airport;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AirportMainActivity extends AppCompatActivity implements AirportListFragment.OnListFragmentInteractionListener{
    private static final String TAG_LIST_FRAGMENT = "TAG_LIST_FRAGMENT";
    AirportListFragment mAirportListFragment;
    AirportViewModel airportViewModel;
    private static final int MENU_PREFERENCES = Menu.FIRST + 1;
    private static final int SHOW_PREFERENCES = 1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.settings_menu_item:
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivityForResult(intent, SHOW_PREFERENCES);
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchableInfo searchableInfo = searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(), AirportSearchResultActivity.class));

        SearchView searchView =
                (SearchView)menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false);

        menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_settings);
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport_main);

        FragmentManager fm = getSupportFragmentManager();

        // 구성 변경이 생긴 후에 안드로이드는 이전에 추가된 프래그먼트를 자동으로 추가한다.
        // 따라서 자동으로 다시 시작된 경우가 아닐 때만 우리가 추가해야 한다.
        if (savedInstanceState == null) {
            FragmentTransaction ft = fm.beginTransaction();

            mAirportListFragment = new AirportListFragment();
            ft.add(R.id.main_activity_frame,
                    mAirportListFragment, TAG_LIST_FRAGMENT);
            ft.commitNow();
        } else {
            mAirportListFragment =
                    (AirportListFragment)fm.findFragmentByTag(TAG_LIST_FRAGMENT);
        }
        airportViewModel = ViewModelProviders.of(this).get(AirportViewModel.class);
    }

    @Override
    public void onListFragmentRefreshRequested() {
        updateAirports();
    }

    private void updateAirports() {
        airportViewModel.loadAirports();
    }
}