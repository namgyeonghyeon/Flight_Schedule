package com.professionalandroid.apps.airport;

import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// 검색 결과 보여주는 액티비티
public class AirportSearchResultActivity extends AppCompatActivity {

    private ArrayList<Airport> mAirports = new ArrayList<>();
    private AirportRecyclerViewAdapter mAirportAdapter
            = new AirportRecyclerViewAdapter(mAirports);

    // 검색 키워드 값이 변할때마다 값이 변할 수 있도록 함 -> query 변경시 Airport search 결과도 바뀌어야함
    MutableLiveData<String> searchQuery;
    LiveData<List<Airport>> searchResults;

    // Airport 이용한것.
    MutableLiveData<String> selectedSearchSuggestionAirportSeq;
    LiveData<Airport> selectedSearchSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport_search_result);

        RecyclerView recyclerView = findViewById(R.id.search_result_list);
        recyclerView.setAdapter(mAirportAdapter);

        // 검색 쿼리 라이브 데이터를 초기화한다.
        searchQuery = new MutableLiveData<>();
        searchQuery.setValue(null);

        // query 이용
        // 검색 쿼리 라이브 데이터를 검색 결과 라이브 데이터에 연결한다.
        // 검색 쿼리가 변경되면 데이터베이스에 쿼리를 수행해
        // 검색 결과가 변경되도록 변환 Map을 구성한다.
        searchResults = Transformations.switchMap(searchQuery,
                query -> AirportDatabaseAccessor
                        .getInstance(getApplicationContext())
                        .airportDAO()
                        .searchAirports("%" + query + "%"));

        // 검색 결과 라이브 데이터의 변경 내용을 관찰한다. 글자 하나마다 result 가 바뀔 수 있도록 함.
        searchResults.observe(AirportSearchResultActivity.this, searchQueryResultObserver);

        // Airport 이용
        // 선택된 검색 제안 ID 라이브 데이터를 초기화한다.
        selectedSearchSuggestionAirportSeq = new MutableLiveData<>();
        selectedSearchSuggestionAirportSeq.setValue(null);

        // 선택된 검색 제안 Airport 라이브 데이터를 선택된 검색 제안 라이브 데이터에 연결한다.
        // 선택된 검색 제안의 Airport 변경되면 데이터베이스에 쿼리를 수행해
        // 해당 데이터를 반환하는 라이브데이터를 변경하도록 변환 Map을 구성한다.
        selectedSearchSuggestion =
                Transformations.switchMap(selectedSearchSuggestionAirportSeq,
                        airportSeq -> AirportDatabaseAccessor
                                .getInstance(getApplicationContext())
                                .airportDAO()
                                .getAirport(airportSeq)); // AirportDAO 에 있는 함수

        // 액티비티가 검색 제안에 따라 시작되면 (클릭)
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            selectedSearchSuggestion.observe(this,
                    selectedSearchSuggestionObserver);
            setSelectedSearchSuggestion(getIntent().getData());
        } else {
            // 액티비티가 검색 쿼리로부터 시작되면
            // 검색어를 추출하고
            // 검색 쿼리 라이브 데이터를 변경한다.
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            setSearchQuery(query);
        }
    }
    // 검색 값이 바뀐 값 넣어주는 함수
    private void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    private final Observer<List<Airport>> searchQueryResultObserver
            = updatedAirports -> {
        // 변경된 검색 쿼리 결과로 UI를 변경한다.
        mAirports.clear(); // 초기화
        if (updatedAirports != null)
            mAirports.addAll(updatedAirports);
        mAirportAdapter.notifyDataSetChanged();
    };

    // create가 아닌 newIntent
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 검색 액티비티가 존재하고, 다른 검색이 수행 중이면
        // 시작 인텐트를 새로 받은 검색 인텐트로 설정하고
        // 새 검색을 수행한다.
        setIntent(intent);

        // 액티비티가 검색 제안에 따라 시작되면 (클릭)
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            setSelectedSearchSuggestion(getIntent().getData());
        } else {
            // 액티비티가 검색 쿼리로부터 시작되면
            // 검색어를 추출하고
            // 검색 쿼리 라이브 데이터를 변경한다.
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            setSearchQuery(query);
        }
    }

    private void setSelectedSearchSuggestion(Uri dataString) {
        String airportSeq = dataString.getPathSegments().get(1);
        selectedSearchSuggestionAirportSeq.setValue(airportSeq);
    }

    final Observer<Airport> selectedSearchSuggestionObserver
            = selectedSearchSuggestion -> {
        // 선택된 검색 제안에 일치되도록 검색 쿼리를 변경한다.
        if (selectedSearchSuggestion != null){
            setSearchQuery(selectedSearchSuggestion.getAirline());
        }
    };
}