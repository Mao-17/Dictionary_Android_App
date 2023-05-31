package com.example.mydictionary;
import java.io.Serializable;
import java.util.List;

import java.io.Serializable;
import java.util.List;

public class Meaning implements Serializable {
    private String partOfSpeech;
    private String definition;
    private String example;
    private List<String> synonyms;
    private List<String> antonyms;
    private String audioUrl;

    public Meaning(String partOfSpeech, String definition, String example, List<String> synonyms, List<String> antonyms, String audioUrl) {
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
        this.example = example;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
        this.audioUrl = audioUrl;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getDefinition() {
        return definition;
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

    public String getAudioUrl() {
        return audioUrl;
    }
}
