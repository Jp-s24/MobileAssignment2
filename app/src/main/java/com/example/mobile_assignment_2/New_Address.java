package com.example.mobile_assignment_2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class New_Address extends AppCompatActivity {

    private EditText titleEditText, descEditText;

    private TextView addressEditText;
    private Button cancel, deleteButton, saveAddressButton, geocodeButton;
    private Note selectedNote;

    public Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_address);
        initWidgets();
        checkForEditNote();
        // initialize geocoder
        geocoder = new Geocoder(this);

        cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewNote();
            }
        });

        saveAddressButton = findViewById(R.id.saveAddressButton);
        saveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote(v);
            }
        });

        deleteButton = findViewById(R.id.deleteNoteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote(v);
            }
        });

        geocodeButton = findViewById(R.id.Geocode);
        geocodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double latitude;
                double longitude;
                // Get user latitude and longitude
                latitude = Double.parseDouble(titleEditText.getText().toString());
                longitude = Double.parseDouble(descEditText.getText().toString());

                // Get address set Text
                addressEditText.setText(getAddress(latitude, longitude));
            }
        });

    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.latitudeEditText);
        descEditText = findViewById(R.id.longitudeEditText);
        addressEditText = findViewById(R.id.addressEditText);
        saveAddressButton = findViewById(R.id.saveAddressButton);
        cancel = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteNoteButton);
    }

    private void checkForEditNote() {
        Intent previousIntent = getIntent();
        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null) {
            titleEditText.setText(selectedNote.getTitle());
            descEditText.setText(selectedNote.getDescription());
            addressEditText.setText(selectedNote.getAddress());
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveNote(View view)
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());
        String address = String.valueOf(addressEditText.getText());




        if(selectedNote == null)
        {
            int id = Note.noteArrayList.size();
            Note newNote = new Note(id, title, desc, address);
            Note.noteArrayList.add(newNote);
            sqLiteManager.addNoteToDatabase(newNote); //add note
        }
        else
        {
            selectedNote.setTitle(title);
            selectedNote.setDescription(desc);
            selectedNote.setAddress(address);
            sqLiteManager.updateNoteInDB(selectedNote);
        }

        finish();
    }

    public void openNewNote() {
        Intent newNoteIntent = new Intent(this, MainActivity.class);
        startActivity(newNoteIntent);
    }

    public void deleteNote(View view) {
        if (selectedNote != null) {
            selectedNote.setDeleted(new Date());
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.updateNoteInDB(selectedNote);
        }
        finish();
    }
    // Get Address for longitude and latitude
    public String getAddress(double latitude, double longitude){
        try {

            // Call Geocoder for address
            List addressList = geocoder.getFromLocation(latitude, longitude, 1);

            // If Address is found return the string
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address)
                        addressList.get(0);
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i));
                }

                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Address not Found";
    }
}