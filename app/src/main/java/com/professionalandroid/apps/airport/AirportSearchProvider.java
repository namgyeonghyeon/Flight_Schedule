package com.professionalandroid.apps.airport;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AirportSearchProvider extends ContentProvider {
    private static final int SEARCH_SUGGESTIONS = 1;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.pro.provider.airport",
                SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.professionalandroid.provider.airport",
                SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.professionalandroid.provider.airport",
                SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH_SUGGESTIONS);
        uriMatcher.addURI("com.professionalandroid.provider.airport",
                SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH_SUGGESTIONS);
    }

    @Override
    public boolean onCreate() {
        AirportDatabaseAccessor.getInstance(getContext().getApplicationContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        if (uriMatcher.match(uri) == SEARCH_SUGGESTIONS) {
            String searchQuery = "%" + uri.getLastPathSegment() + "%";

            AirportDAO airportDAO
                    = AirportDatabaseAccessor.getInstance(getContext().getApplicationContext())
                    .airportDAO();

            Cursor c = airportDAO.generateSearchSuggestions(searchQuery);

            return c;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGESTIONS :
                return SearchManager.SHORTCUT_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
