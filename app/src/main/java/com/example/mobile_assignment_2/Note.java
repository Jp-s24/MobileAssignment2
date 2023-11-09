package com.example.mobile_assignment_2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note {
    public static ArrayList<Note> noteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA =  "noteEdit";

    private int id;
    private String title;
    private String description;

    private String address;

    private Date deleted;


    public Note(int id, String title, String description, String address, Date deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.deleted = deleted;
    }

    public Note(int id, String title, String description, String address) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        deleted = null;
    }


    public static Note getNoteForID(int passedNoteID)
    {
        for (Note note : noteArrayList)
        {
            if(note.getId() == passedNoteID)
                return note;
        }

        return null;
    }

    public static ArrayList<Note> nonDeletedNotes()
    {
        ArrayList<Note> nonDeleted = new ArrayList<>();
        for(Note note : noteArrayList)
        {
            if(note.getDeleted() == null)
                nonDeleted.add(note);
        }

        return nonDeleted;
    }


    //function used to the search query to filter the notes based on user input
    public static List<Note> filterNotes(String query) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : noteArrayList) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        return filteredNotes;
    }


    //intialize getters and setters below
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getAddress() {return address; }
    public void setAddress (String address)
    {
        this.address = address;
    }


    public Date getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Date deleted)
    {
        this.deleted = deleted;
    }


}
