package com.example.mydictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PosAdapter extends RecyclerView.Adapter<PosAdapter.PosViewHolder> {

    private ArrayList<String> posList;
    private OnPosClickListener listener;

    public PosAdapter(ArrayList<String> posList, OnPosClickListener listener) {
        this.posList = posList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pos, parent, false);
        return new PosViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PosViewHolder holder, int position) {
        String pos = posList.get(position);
        holder.posTextView.setText(pos);
    }

    @Override
    public int getItemCount() {
        return posList.size();
    }

    public static class PosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView posTextView;
        private OnPosClickListener listener;

        public PosViewHolder(@NonNull View itemView, OnPosClickListener listener) {
            super(itemView);
            posTextView = itemView.findViewById(R.id.pos_text_view);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String pos = posTextView.getText().toString();
            listener.onPosClick(pos);
        }
    }

    public interface OnPosClickListener {
        void onPosClick(String pos);
    }
}
