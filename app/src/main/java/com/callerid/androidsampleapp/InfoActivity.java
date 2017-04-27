package com.callerid.androidsampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    static String Number = "n/a";

    TextView tbName;
    TextView tbNumber;
    TextView tbAddress;
    TextView tbCity;
    TextView tbState;
    TextView tbZip;

    // Setup field constants for accessing fields in database
    String field_number = "Number";
    String field_name = "Name";
    String field_address = "Address";
    String field_city = "City";
    String field_state = "State";
    String field_zip = "Zip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Set screen to stay on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Associations
        tbName = (TextView)findViewById(R.id.tbName);
        tbNumber = (TextView)findViewById(R.id.tbNumber);
        tbAddress = (TextView)findViewById(R.id.tbAddress);
        tbCity = (TextView)findViewById(R.id.tbCity);
        tbState = (TextView)findViewById(R.id.tbState);
        tbZip = (TextView)findViewById(R.id.tbZip);

        // Show number
        Number = getIntent().getStringExtra("number");
        tbNumber.setText(Number);

        // Check if already in database and if so, pull data
        String[] contactInfo = MainActivity.checkCallerIdForMatch(Number);

        String query = "";
        if(contactInfo[0]!="not found"){

            tbName.setText(contactInfo[0]);
            tbAddress.setText(contactInfo[1]);
            tbCity.setText(contactInfo[2]);
            tbState.setText(contactInfo[3]);
            tbZip.setText(contactInfo[4]);

        }

        // Listeners
        Button btnSaveAndClose = (Button)findViewById(R.id.btnSaveAndClose);
        btnSaveAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act2 = new Intent(view.getContext(), MainActivity.class);

                // Get data
                String name = tbName.getText().toString();
                String address = tbAddress.getText().toString();
                String city = tbCity.getText().toString();
                String state = tbState.getText().toString();
                String zip = tbZip.getText().toString();

                // Save data
                insertOrUpdateContact(name,Number,address,city,state,zip);

                // Update database image on other activity
                for(int i=1;i<5;i++){
                    if(MainActivity.memCallNumbers[i]==Number){

                        // update memory to reflect addition of contact
                        MainActivity.dbStatus[i] = MainActivity.DB_FOUND;
                        MainActivity.memCallNames[i] = name;
                        break;
                    }
                }

                // Go back to main activity
                startActivity(act2);

            }
        });

    }

    private boolean insertOrUpdateContact(String name,
                                          String number,
                                          String address,
                                          String city,
                                          String state,
                                          String zip)
    {

        // Check if already in database before adding
        String[] contactInfo = MainActivity.checkCallerIdForMatch(number);

        String query = "";
        if(contactInfo[0]=="not found"){

            // Contact not found, so add it now
            query = "INSERT INTO contacts (" +
                    field_name + "," +
                    field_number + "," +
                    field_address + "," +
                    field_city + "," +
                    field_state + "," +
                    field_zip +
                    "" +
                    ") VALUES (" +
                    "" +
                    "'" + name + "'," +
                    "'" + number + "'," +
                    "'" + address + "'," +
                    "'" + city + "'," +
                    "'" + state + "'," +
                    "'" + zip + "'" +
                    ");";

        }
        else{

            // Contact was found, so update it
            query = "UPDATE contacts SET " +
                    field_name + " = '" + name + "'," +
                    field_address + " = '" + address + "'," +
                    field_city + " = '" + city + "'," +
                    field_state + " = '" + state + "'," +
                    field_zip + " = '" + zip + "'" +
                    " " +
                    "WHERE " + field_number + " = '" + number + "';";

        }

        MainActivity.myDatabase.execSQL(query);

        return true;
    }

}
