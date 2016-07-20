package com.rutvik.bhagwatiholidays;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class BusActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar mToolbar;

    private EditText etMobilePhone, etFrom, etTo, etDateOfTravel;

    private FloatingActionButton fabDone;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Book Buses");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etMobilePhone = (EditText) findViewById(R.id.et_mobilePhone);

        etFrom = (EditText) findViewById(R.id.et_from);

        etTo = (EditText) findViewById(R.id.et_to);

        etDateOfTravel = (EditText) findViewById(R.id.et_dateOfTravel);


        etDateOfTravel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show(getFragmentManager(), "DateOfTravel");
                }
            }
        });

        etDateOfTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), "DateOfTravel");
            }
        });

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMinDate(calendar);


        fabDone = (FloatingActionButton) findViewById(R.id.done);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        etDateOfTravel.setText(date);
    }
}
