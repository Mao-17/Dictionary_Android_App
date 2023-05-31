package com.example.mydictionary;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.HashMap;

public class MeaningDisplayActivity extends AppCompatActivity implements PosAdapter.OnPosClickListener {

    private TextView wordTextView;
    private ImageView audioButton;
    private RecyclerView posRecyclerView;
    private PosAdapter posAdapter;
    private ArrayList<String> posList;
    private String filePath;
    private HashMap<String, ArrayList<Meaning>> posToMeaningsMap;
    private String audioUrl;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meaning_display);

        // Get the word and meanings from the intent extras
        word = getIntent().getStringExtra("word");
        ArrayList<Meaning> meaningsList = (ArrayList<Meaning>) getIntent().getSerializableExtra("meanings");

        // Group the meanings by part of speech
        posToMeaningsMap = new HashMap<>();
        for (Meaning meaning : meaningsList) {
            String pos = meaning.getPartOfSpeech();
            ArrayList<Meaning> posMeanings = posToMeaningsMap.get(pos);
            if (posMeanings == null) {
                posMeanings = new ArrayList<>();
                posToMeaningsMap.put(pos, posMeanings);
            }
            posMeanings.add(meaning);
        }

        // Create a list of part of speech tags
        posList = new ArrayList<>(posToMeaningsMap.keySet());

        // Get references to the views
        wordTextView = findViewById(R.id.word_text_view);
        audioButton = findViewById(R.id.audio_button);
        posRecyclerView = findViewById(R.id.pos_recycler_view);

        // Set the word in the text view
        wordTextView.setText(word);

        // Set up the RecyclerView and adapter
        posRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        posAdapter = new PosAdapter(posList, this);
        posRecyclerView.setAdapter(posAdapter);

        // Set up the audio button
        audioUrl = getIntent().getStringExtra("audioUrl");
        //Toast.makeText(MeaningDisplayActivity.this, "audiourlis " + audioUrl, Toast.LENGTH_SHORT).show();
        if (audioUrl != null && !audioUrl.isEmpty()) {
            downloadAndPlayAudio();
            audioButton.setVisibility(View.VISIBLE);
            Log.d("AUDIO_URL", audioUrl);
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

    @Override
    public void onPosClick(String pos) {
        ArrayList<Meaning> meaningsList = posToMeaningsMap.get(pos);
        if (meaningsList != null) {
            Intent intent = new Intent(this, WordDisplayActivity.class);
            intent.putExtra("word", word);
            intent.putExtra("pos", pos);
            intent.putExtra("meanings", meaningsList);
            intent.putExtra("filepath", filePath);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No meanings found for " + pos, Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAndPlayAudio() {
        // Create a download manager request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Downloading audio file");
        request.setDescription("Please wait...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "audio.mp3");

        // Get the download service and enqueue the request
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);

        // Set a broadcast receiver to listen for when the download is complete
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {
                    // Get the download file path
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = cursor.getInt(columnIndex);
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            // Play the audio file
                            Toast.makeText(MeaningDisplayActivity.this, "audio downloaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MeaningDisplayActivity.this, "Audio download failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MeaningDisplayActivity.this, "Failed to get download file path", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
}