package com.hyrulesoft.android.apps.howzmydrive;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends AppCompatActivity {
    public static final String HOME_ADDRESS = "HOME_ADDRESS";
    public static final String WORK_ADDRESS = "WORK_ADDRESS";
    Button homeButton, workButton;
    TextView homeAddress, workAddress;
    Intent homeIntent, workIntent;
    private static final int HOME_PLACE_PICKER_REQUEST = 1;
    private static final int WORK_PLACE_PICKER_REQUEST = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sp = getParent().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        if (resultCode == RESULT_OK && (requestCode == HOME_PLACE_PICKER_REQUEST || requestCode == WORK_PLACE_PICKER_REQUEST)) {
            switch (requestCode) {
                case HOME_PLACE_PICKER_REQUEST:
                    Place home = PlacePicker.getPlace(getApplicationContext(), data);
                    homeAddress.setText(home.getAddress());
                    ed.putString(HOME_ADDRESS, home.getAddress().toString());
                    break;
                case WORK_PLACE_PICKER_REQUEST:
                    Place work = PlacePicker.getPlace(getApplicationContext(), data);
                    workAddress.setText(work.getAddress());
                    ed.putString(WORK_ADDRESS, work.getAddress().toString());
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeButton = findViewById(R.id.HomeButton);
        workButton = findViewById(R.id.WorkButton);
        homeAddress = findViewById(R.id.HomeAddress);
        workAddress = findViewById(R.id.WorkAddress);

        SharedPreferences sp = getParent().getPreferences(Context.MODE_PRIVATE);
        String homeAddressSaved = sp.getString(HOME_ADDRESS, null);
        String workAddressSaved = sp.getString(WORK_ADDRESS, null);
        if (homeAddressSaved != null) {
            homeAddress.setText(homeAddressSaved);
        }
        if (workAddressSaved != null) {
            workAddress.setText(workAddressSaved);
        }

        if (homeAddressSaved != null && workAddressSaved != null) {
            Intent intent = new Intent("com.hyrulesoft.apps.android.howzmydrive.ALARM_START");
            sendBroadcast(intent);
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    homeIntent = builder.build(MainActivity.this);
                    startActivityForResult(homeIntent, HOME_PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    workIntent = builder.build(MainActivity.this);
                    startActivityForResult(workIntent, WORK_PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
