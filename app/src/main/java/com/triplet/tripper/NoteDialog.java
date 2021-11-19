package com.triplet.tripper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.triplet.tripper.adapters.WordNoteAdapter;
import com.triplet.tripper.models.map.WordNote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NoteDialog extends AppCompatDialogFragment {
    private GoogleMap map;
    private Marker marker;
    private RecyclerView recyclerView;
    private WordNoteAdapter wordNoteAdapter;
    private ArrayList<WordNote> wordNoteList;
    public NoteDialog(GoogleMap curMap, Marker marker) {
        map = curMap;
        this.marker = marker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_note,null);
        builder.setView(view)
                .setTitle("Vocabulary Note")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
        .setNeutralButton("Destroy Marker", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                marker.remove();
            }
        });
        //Cai dat Recycler view
        recyclerView = view.findViewById(R.id.word_note_list);
        wordNoteList = new ArrayList<WordNote>();
        //wordNoteList.add(new WordNote("Hello"));
        wordNoteAdapter = new WordNoteAdapter(getContext(),wordNoteList);
        recyclerView.setAdapter(wordNoteAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        FloatingActionButton btn = view.findViewById(R.id.floatBtnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordNoteList.add(wordNoteList.size(),new WordNote(""));
                wordNoteAdapter.notifyItemInserted(wordNoteList.size());
            }
        });
        return builder.create();
    }
}
