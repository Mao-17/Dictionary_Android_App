package com.example.mydictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder> {

    private ArrayList<Meaning> meaningsList;
    private OnMeaningClickListener listener;
    private Context context;

    public interface OnMeaningClickListener {
        void onMeaningClick(int position);
    }

    public MeaningAdapter(ArrayList<Meaning> meaningsList, OnMeaningClickListener listener, Context context) {
        this.meaningsList = meaningsList != null ? meaningsList : new ArrayList<Meaning>();
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public MeaningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meaning, parent, false);
        return new MeaningViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeaningViewHolder holder, int position) {
        Meaning meaning = meaningsList.size() > position ? meaningsList.get(position) : null;
        if (meaning != null) {
            holder.meaningTextView.setText(meaning.getDefinition() != null ? meaning.getDefinition() : "");
            holder.exampleTextView.setText(meaning.getExample() != null ? meaning.getExample() : "");

            List<String> synonymsList = meaning.getSynonyms();
            if (synonymsList != null && !synonymsList.isEmpty()) {
                StringBuilder synonymsBuilder = new StringBuilder();
                for (int i = 0; i < synonymsList.size(); i++) {
                    String synonym = synonymsList.get(i);
                    if (synonym != null) {
                        synonymsBuilder.append(synonym);
                        if (i < synonymsList.size() - 1) {
                            synonymsBuilder.append(", ");
                        }
                    }
                }
                holder.synonymsTextView.setText(synonymsBuilder.toString());
            } else {
                holder.synonymsTextView.setText("");
            }

            List<String> antonymsList = meaning.getAntonyms();
            if (antonymsList != null && !antonymsList.isEmpty()) {
                StringBuilder antonymsBuilder = new StringBuilder();
                for (int i = 0; i < antonymsList.size(); i++) {
                    String antonym = antonymsList.get(i);
                    if (antonym != null) {
                        antonymsBuilder.append(antonym);
                        if (i < antonymsList.size() - 1) {
                            antonymsBuilder.append(", ");
                        }
                    }
                }
                holder.antonymsTextView.setText(antonymsBuilder.toString());
            } else {
                holder.antonymsTextView.setText("");
            }
        } else {
            holder.meaningTextView.setText("");
            holder.exampleTextView.setText("");
            holder.synonymsTextView.setText("");
            holder.antonymsTextView.setText("");
        }
    }


    @Override
    public int getItemCount() {
        return meaningsList.size();
    }

    public class MeaningViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView meaningTextView;
        public TextView exampleTextView;
        public TextView synonymsTextView;
        public
        TextView antonymsTextView;

        public MeaningViewHolder(@NonNull View itemView) {
            super(itemView);
            meaningTextView = itemView.findViewById(R.id.meaning_text_view);
            exampleTextView = itemView.findViewById(R.id.example_text_view);
            synonymsTextView = itemView.findViewById(R.id.synonyms_text_view);
            antonymsTextView = itemView.findViewById(R.id.antonyms_text_view);
            itemView.setOnClickListener(this);
        }

            @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onMeaningClick(position);
            }
        }
    }
}