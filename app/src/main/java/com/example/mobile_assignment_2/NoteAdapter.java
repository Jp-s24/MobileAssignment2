package com.example.mobile_assignment_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
        this.notes = notes; // Initialize the 'notes' list
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_cell, parent, false);

            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.address);
            holder.desc = convertView.findViewById(R.id.latitudeField);
            holder.address = convertView.findViewById(R.id.longitudeField);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = getItem(position);
        if (note != null) {
            holder.title.setText(note.getTitle());
            holder.desc.setText(note.getDescription());
            holder.address.setText(note.getAddress());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView desc;
        TextView address;
    }


    //method to update the list

    public void updateList(List<Note> newList) {
        if (notes != null) {
            notes.clear();
            notes.addAll(newList);
            notifyDataSetChanged(); // Notify adapter of changes in the list
        }
    }
}
