package com.example.mydictionary;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class WordDisplayActivity extends AppCompatActivity implements MeaningAdapter.OnMeaningClickListener {

    private TextView wordTextView;
    private TextView posTextView;
    private ImageView audioButton;
    private RecyclerView meaningsRecyclerView;
    private MeaningAdapter meaningsAdapter;
    private ArrayList<Meaning> meaningsList;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_display);

        // Get the word, part of speech, meanings, and audio URL from the intent extras
        String word = getIntent().getStringExtra("word");
        String pos = getIntent().getStringExtra("pos");
        meaningsList = (ArrayList<Meaning>) getIntent().getSerializableExtra("meanings");
        filePath = getIntent().getStringExtra("filepath");

        // Get references to the views
        wordTextView = findViewById(R.id.word_text_view);
        posTextView = findViewById(R.id.pos_text_view);
        audioButton = findViewById(R.id.audio_button);
        meaningsRecyclerView = findViewById(R.id.meanings_recycler_view);

        // Set the word and part of speech in the text views
        wordTextView.setText(word);
        posTextView.setText(pos);

        // Set up the RecyclerView and adapter
        meaningsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        meaningsAdapter = new MeaningAdapter(meaningsList, this, this);
        meaningsRecyclerView.setAdapter(meaningsAdapter);

        // Set up the audio button
        if (filePath != null && !filePath.isEmpty()) {
            audioButton.setVisibility(View.VISIBLE);
            //Log.d("AUDIO_URL", fi);
            audioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playaudio(filePath);
                }
            });
        } else {
            audioButton.setVisibility(View.GONE);
        }
    }
    private void playaudio(String fp) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fp);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing audio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMeaningClick(int position) {
        // Handle meaning item click event
        Toast.makeText(this, "Meaning item clicked at position " + position, Toast.LENGTH_SHORT).show();
    }
}
