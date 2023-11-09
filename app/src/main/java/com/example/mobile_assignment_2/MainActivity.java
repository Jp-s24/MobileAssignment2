package com.example.mobile_assignment_2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView noteListView;
    public Geocoder geocoder;

    private SearchView searchBar;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this);

        initWidgets();
        loadFromDBToMemory();
        try {
            ReadFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setOnClickListener();
        setNoteAdapter();

        searchBar = findViewById(R.id.searchView);


        //search query handler
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = newText;
                setNoteAdapter(query);
                return true;
            }
        });

        ImageView imageView = findViewById(R.id.newbtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewNote();
            }
        });
    }

    private void initWidgets() {
        noteListView = findViewById(R.id.list);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateNoteListArray();
    }

    private void setNoteAdapter(String query) {
        List<Note> nonDeletedNotes = Note.nonDeletedNotes();

        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : nonDeletedNotes) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }

        noteAdapter.updateList(filteredNotes); // Assuming this method exists in your NoteAdapter
    }

    private void setNoteAdapter() {
        noteAdapter = new NoteAdapter(getApplicationContext(), Note.nonDeletedNotes());
        noteListView.setAdapter(noteAdapter);
    }

    private void setOnClickListener() {
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Note selectedNote = (Note) adapterView.getItemAtPosition(position);
                Intent editNoteIntent = new Intent(getApplicationContext(), New_Address.class);
                editNoteIntent.putExtra(Note.NOTE_EDIT_EXTRA, selectedNote.getId());
                startActivity(editNoteIntent);
            }
        });
    }

    public void openNewNote() {
        Intent newNoteIntent = new Intent(this, New_Address.class);
        startActivity(newNoteIntent);
    }

    public void ReadFile() throws IOException, IOException {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        InputStream raw = getResources().openRawResource(R.raw.locations);
        BufferedReader in = new BufferedReader(new InputStreamReader(raw));
        String line = in.readLine();

        while (line != null) {
            String[] split = line.split(",");
            double latitude = Double.parseDouble(split[0].trim());
            double longitude = Double.parseDouble(split[1].trim());
            String address = getAddress(latitude, longitude);

            int id = Note.noteArrayList.size();
            String latitudeStr = String.valueOf(latitude);
            String longitudeStr = String.valueOf(longitude);

            Note newNote = new Note(id, latitudeStr, longitudeStr, address);
            Note.noteArrayList.add(newNote);
            sqLiteManager.addNoteToDatabase(newNote);

            line = in.readLine();
        }

        in.close();
    }

    public String getAddress(double latitude, double longitude) {
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
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

    @Override
    protected void onResume() {
        super.onResume();
        String query = searchBar.getQuery().toString();
        setNoteAdapter(query);
    }
}
