package edu.csueb.android.googlemapandsqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import edu.csueb.android.googlemapandsqlite.databinding.ActivityMapsBinding;
import edu.csueb.android.googlemapandsqllite.LocationsContentProvider;
import edu.csueb.android.googlemapandsqllite.LocationsDB;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up delete all button
        Button btnDeleteAll = findViewById(R.id.btn_delete_all);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllLocations();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setOnMapClickListener(latLng -> insertLocation(latLng));
        mMap.setOnMapLongClickListener(latLng -> deleteAllLocations());
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }


    private void insertLocation(LatLng latLng) {
        runOnUiThread(() -> {

            float zoomLevel = mMap.getCameraPosition().zoom;


            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                ContentValues values = new ContentValues();
                values.put(LocationsDB.FIELD_LAT, latLng.latitude);
                values.put(LocationsDB.FIELD_LNG, latLng.longitude);
                values.put(LocationsDB.FIELD_ZOOM, zoomLevel);


                //values.put(LocationsDB.FIELD_ZOOM, mMap.getCameraPosition().zoom);
                getContentResolver().insert(LocationsContentProvider.CONTENT_URI, values);
            });
            executorService.shutdown();
        });
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


