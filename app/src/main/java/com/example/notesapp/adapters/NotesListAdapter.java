package com.example.notesapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.listeners.NoteOnClickListener;
import com.example.notesapp.modal.Note;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> {
    private List<Note> listNotes;
    private final NoteOnClickListener listener;

    public NotesListAdapter(List<Note> notes, NoteOnClickListener noteOnClickListener) {
        listNotes = notes;
        listener = noteOnClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note_recycler, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.textViewTitle.setText(listNotes.get(position).getTitle());
        holder.textViewDate.setText(listNotes.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        Log.v(NotesListAdapter.class.getSimpleName(),"" + listNotes.size());
        return listNotes.size();
    }

    /**
     * ViewHolder class
     */
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewDate;

        public NoteViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewDate = view.findViewById(R.id.textViewDate);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.displayContent(getAdapterPosition());
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.openDialog(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
