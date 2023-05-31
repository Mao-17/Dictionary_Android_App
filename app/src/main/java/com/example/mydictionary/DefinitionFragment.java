package com.example.mydictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.List;

public class DefinitionFragment extends DialogFragment {

    private Meaning meaning;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String audioUrl);
    }

    // Declare a variable of this interface type
    private OnFragmentInteractionListener mListener;

    // Override the onAttach() method to ensure that the parent activity implements the interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

//    // Call the interface method whenever appropriate
//    private void someMethod() {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(audioUrl);
//        }
//    }

    public static DefinitionFragment newInstance(Meaning meaning) {
        DefinitionFragment fragment = new DefinitionFragment();
        Bundle args = new Bundle();
        args.putSerializable("meaning", meaning);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meaning = (Meaning) getArguments().getSerializable("meaning");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_definition, null);

        TextView definitionTextView = rootView.findViewById(R.id.definition_text_view);
        TextView partOfSpeechTextView = rootView.findViewById(R.id.part_of_speech_text_view);
        TextView exampleTextView = rootView.findViewById(R.id.example_text_view);
        TextView synonymTextView = rootView.findViewById(R.id.synonym_text_view);
        TextView antonymTextView = rootView.findViewById(R.id.antonym_text_view);

        // Set meaning data to views
        definitionTextView.setText(meaning.getDefinition());
        partOfSpeechTextView.setText(meaning.getPartOfSpeech());
        exampleTextView.setText(meaning.getExample());

        if (meaning.getSynonyms() != null && !meaning.getSynonyms().isEmpty()) {
            String synonyms = TextUtils.join(", ", meaning.getSynonyms());
            synonymTextView.setText(synonyms);
        } else {
            synonymTextView.setText("No synonyms found");
        }
        if (meaning.getAntonyms() != null && !meaning.getAntonyms().isEmpty()) {
            String antonyms = TextUtils.join(", ", meaning.getAntonyms());
            antonymTextView.setText(antonyms);
        } else {
            antonymTextView.setText("No antonyms found");
        }

        builder.setView(rootView)
                .setPositiveButton("OK", null);

        return builder.create();
    }

    public static class Meaning implements Serializable {
        private String definition;
        private String partOfSpeech;
        private String example;
        private List<String> synonyms;
        private List<String> antonyms;

        public Meaning(String definition, String partOfSpeech, String example, List<String> synonyms, List<String> antonyms) {
            this.definition = definition;
            this.partOfSpeech = partOfSpeech;
            this.example = example;
            this.synonyms = synonyms;
            this.antonyms = antonyms;
        }

        public String getDefinition() {
            return definition;
        }

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public String getExample() {
            return example;
        }

        public List<String> getSynonyms() {
            return synonyms;
        }

        public List<String> getAntonyms() {
            return antonyms;
        }
    }
}
