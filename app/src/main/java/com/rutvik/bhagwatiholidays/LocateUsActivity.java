package com.rutvik.bhagwatiholidays;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocateUsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private Toolbar mToolbar;

    App app;

    TextView tvContactNumberLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_us);

        app=(App) getApplication();

        app.trackScreenView(LocateUsActivity.class.getSimpleName());

        tvContactNumberLink=(TextView) findViewById(R.id.tv_contactNumberLink);
        tvContactNumberLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.trackEvent(SwipeTabActivity.class.getSimpleName(), "SUPPORT CLICKED", "NAV ACTION");
                Intent supportIntent = new Intent(Intent.ACTION_DIAL);
                supportIntent.setData(Uri.parse("tel:07940223333"));
                startActivity(supportIntent);
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng bhagwatiHolidays = new LatLng(23.025819, 72.5096982);
        mMap.addMarker(new MarkerOptions().position(bhagwatiHolidays).title("Bhagwati Holidays"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bhagwatiHolidays));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }
}
