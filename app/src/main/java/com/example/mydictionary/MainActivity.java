package com.example.mydictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText wordEditText;
    private String word;
    private String audioUrl_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get reference to the word EditText
        wordEditText = findViewById(R.id.word_edit_text);

        // Set click listener for the search button
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered word
                word = wordEditText.getText().toString().trim();

                // Validate the input
                if (TextUtils.isEmpty(word)) {
                    Toast.makeText(MainActivity.this, "Please enter a word", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!word.matches("[a-zA-Z]+")) {
                    Toast.makeText(MainActivity.this, "Only characters are allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make the API call
                new GetMeaningsTask().execute(word);
            }
        });
    }

    // AsyncTask to handle the API call
    private class GetMeaningsTask extends AsyncTask<String, Void, List<Meaning>> {

        String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

        @Override
        protected List<Meaning> doInBackground(String... params) {
            String word = params[0];
            List<Meaning> meanings = new ArrayList<>();
            try {
                URL url = new URL(API_URL + word);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                String jsonString = builder.toString();
                JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject json = jsonArray.getJSONObject(0);
                JSONArray phoneticsArray = json.getJSONArray("phonetics");
                audioUrl_final = null;

                for (int i = 0; i < phoneticsArray.length(); i++) {
                    JSONObject phoneticObject = phoneticsArray.getJSONObject(i);
                    if (phoneticObject.has("audio")) {
                        audioUrl_final = phoneticObject.getString("audio");
                        break;
                    }
                }

                JSONArray meaningsArray = json.getJSONArray("meanings");
                for (int i = 0; i < meaningsArray.length(); i++) {
                    JSONObject meaningObject = meaningsArray.getJSONObject(i);
                    String partOfSpeech = meaningObject.getString("partOfSpeech");
                    //...
                    JSONArray definitionsArray = meaningObject.getJSONArray("definitions");
                    for (int j = 0; j < definitionsArray.length(); j++) {
                        JSONObject definitionObject = definitionsArray.getJSONObject(j);
                        String definition = definitionObject.getString("definition");
                        String example = definitionObject.optString("example", null);
                        JSONArray synonymsArray = definitionObject.optJSONArray("synonyms");
                        List<String> synonyms = new ArrayList<>();
                        if (synonymsArray != null) {
                            for (int k = 0; k < synonymsArray.length(); k++) {
                                synonyms.add(synonymsArray.getString(k));
                            }
                        }
                        JSONArray antonymsArray = definitionObject.optJSONArray("antonyms");
                        List<String> antonyms = new ArrayList<>();
                        if (antonymsArray != null) {
                            for (int k = 0; k < antonymsArray.length(); k++) {
                                antonyms.add(antonymsArray.getString(k));
                            }
                        }
                        String audioUrl = definitionObject.optString("audio", null);
                        Meaning meaning = new Meaning(partOfSpeech, definition, example, synonyms, antonyms, audioUrl);
                        meanings.add(meaning);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return meanings;
        }

        @Override
        protected void onPostExecute(List<Meaning> meanings) {
            super.onPostExecute(meanings);

            // Check if any meanings were found
            if (meanings.size() == 0) {
                Toast.makeText(MainActivity.this, "Word not found in dictionary", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to the Meaning Display Activity and pass the meaning data
            Intent intent = new Intent(MainActivity.this, MeaningDisplayActivity.class);
            intent.putExtra("word", word);
            //Toast.makeText(MainActivity.this, "wordurlisabc " + word, Toast.LENGTH_SHORT).show();
            intent.putExtra("meanings", (Serializable) meanings);
            intent.putExtra("audioUrl", audioUrl_final);

            //Toast.makeText(MainActivity.this, "audiourlisabc " + audiourl, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
}