package edu.csueb.android.googlemapandsqllite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import edu.csueb.android.googlemapandsqlite.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>{

    private GoogleMap mMap;
    private static final LatLng DEFAULT_LOCATION = new LatLng(-34, 151); // Sydney, Australia
    private static final float DEFAULT_ZOOM = 10f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                new LocationInsertTask().execute(latLng);
//            }
//        });
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                new LocationDeleteTask().execute();
//            }
//        });
//        LoaderManager.getInstance(this).initLoader(0, null, this);
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
//
//        mMap.setOnMapClickListener(latLng -> insertLocation(latLng));
//        mMap.setOnMapLongClickListener(latLng -> deleteAllLocations());
//        LoaderManager.getInstance(this).initLoader(0, null, this);
    }


    private void insertLocation(LatLng latLng) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            ContentValues values = new ContentValues();
            values.put(LocationsDB.FIELD_LAT, latLng.latitude);
            values.put(LocationsDB.FIELD_LNG, latLng.longitude);

            values.put(LocationsDB.FIELD_ZOOM, mMap.getCameraPosition().zoom);
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, values);
        });
        executorService.shutdown();
    }


    private class LocationInsertTask extends AsyncTask<LatLng, Void, Void> {
        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        protected Void doInBackground(LatLng... params) {
            LatLng latLng = params[0];
            ContentValues values = new ContentValues();
            values.put(LocationsDB.FIELD_LAT, latLng.latitude);
            values.put(LocationsDB.FIELD_LNG, latLng.longitude);
            values.put(LocationsDB.FIELD_ZOOM, mMap.getCameraPosition().zoom);
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, values);
            return null;
        }
    }

    private void deleteAllLocations() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null));
        executorService.shutdown();
    }


    private class LocationDeleteTask extends AsyncTask<Void, Void, Void> {
        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, LocationsContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mMap.clear();
        if (data != null && data.moveToFirst()) {
            int latIndex = data.getColumnIndex(LocationsDB.FIELD_LAT);
            int lngIndex = data.getColumnIndex(LocationsDB.FIELD_LNG);
            int zoomIndex = data.getColumnIndex(LocationsDB.FIELD_ZOOM);

            if (latIndex >= 0 && lngIndex >= 0 && zoomIndex >= 0) {
                do {
                    double lat = data.getDouble(latIndex);
                    double lng = data.getDouble(lngIndex);
                    float zoom = data.getFloat(zoomIndex);
                    LatLng latLng = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                } while (data.moveToNext());
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMap.clear();
    }
}
