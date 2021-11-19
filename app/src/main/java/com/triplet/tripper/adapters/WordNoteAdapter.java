package com.triplet.tripper.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.triplet.tripper.R;
import com.triplet.tripper.models.map.WordNote;

import java.util.ArrayList;
import java.util.List;

public class WordNoteAdapter extends RecyclerView.Adapter<WordNoteAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WordNote> wordNoteList;
    private WordNoteAdapter adapter = this;

    public WordNoteAdapter(Context context, ArrayList<WordNote> wordNoteList) {
        this.context = context;
        this.wordNoteList = wordNoteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View wordNoteView = layoutInflater.inflate(R.layout.note_word_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(wordNoteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordNote wordNote = wordNoteList.get(position);

        EditText text = holder.editText;
        MaterialButton edit = holder.editBtn;
        MaterialButton delete = holder.deleteBtn;
        int index = position;

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordNoteList.remove(index);
                notifyItemRemoved(index);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getMaxWidth() == 129){
                    text.setEnabled(false);
                    text.setMaxWidth(325);
                    edit.setText("M");
                }
                else {
                    text.setEnabled(true);
                    text.setMaxWidth(129);
                    edit.setText("S");
                }
            }
        });
        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false)
                    text.setEnabled(false);
            }
        });
        text.setText(wordNote.getWord());

    }

    @Override
    public int getItemCount() {
        return wordNoteList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private EditText editText;
        private MaterialButton deleteBtn;
        private MaterialButton editBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.edit_word);
            deleteBtn = itemView.findViewById(R.id.deleteWordBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
        }
    }

}
