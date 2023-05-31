# Dictionary Android App

This is a dictionary application for Android that allows users to search for words and retrieve their meanings and related information. The app interacts with the [Dictionary API](https://dictionaryapi.dev/) to fetch the necessary data.

## Features

1. **Main Activity**: This activity contains two components:
   - Text field: Users can enter a word in this field as input.
   - Search Button: Upon clicking this button, an API call is made to the dictionary API using the input word. The request is handled asynchronously using AsyncTask. If the API returns a valid response, the user is navigated to the list of meanings and audio fragment page. In case of an invalid response, a toast message is displayed indicating that the word was not found in the dictionary. The input is validated to allow only characters.

2. **Meaning and Audio Fragment**: This fragment displays the meaning of the searched word and includes three components:
   - Word Heading: The searched word is displayed as the heading.
   - Audio Button: This button allows users to download and play the pronunciation audio of the word. The audio URL is obtained from the API response under the key "phonetics". If multiple URLs are available, any one of them can be played. The audio download and playback are handled asynchronously using AsyncTask. The audio button is positioned horizontally next to the word heading.
   - RecyclerView: The API response contains a list of meanings for the searched word under the key "meanings". Each meaning in the list includes definition, examples, synonym, antonym, and part of speech. The RecyclerView displays items associated with each meaning, designed using TextView. Each item in the RecyclerView only shows the part of speech for that meaning. When an item is clicked, it opens the definition fragment. The RecyclerView and fragments can be hosted in one activity or split into separate activities.

3. **Definition Fragment**: This fragment displays detailed information about a selected meaning. It includes the following components:
   - Definition: Displays the definition of the word obtained from the API.
   - Example: Displays an example sentence using the word.
   - Part of Speech: Displays the part of speech for the meaning.
   - Antonym: Displays the antonym of the word if available in the API response.
   - Synonym: Displays the synonym of the word if available in the API response.

## Usage

To use the dictionary app, follow these steps:

1. Clone the repository from GitHub.
```bash
git clone https://github.com/Mao-17/Dictionary_Android_App.git
```
2. Open the project in Android Studio.
3. Build and run the app on an Android device or emulator.
4. In the main activity, enter a word in the text field.
5. Click the search button to make an API call and retrieve the meaning and related information for the word.
6. The app will navigate to the meaning and audio fragment, where you can see the word heading, play the pronunciation audio, and view the list of meanings.
7. Clicking on a meaning in the list will open the definition fragment, displaying detailed information about that meaning, including definition, example, part of speech, antonym, and synonym (if available).

Feel free to explore the app and search for different words to learn their meanings!
