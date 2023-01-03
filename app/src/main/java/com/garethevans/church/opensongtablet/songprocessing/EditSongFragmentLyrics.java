package com.garethevans.church.opensongtablet.songprocessing;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.databinding.EditSongLyricsBinding;
import com.garethevans.church.opensongtablet.interfaces.EditSongFragmentInterface;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;

// This fragment purely deals with the lyrics/chords

public class EditSongFragmentLyrics extends Fragment {

    private MainActivityInterface mainActivityInterface;
    private EditSongFragmentInterface editSongFragmentInterface;
    private EditSongLyricsBinding myView;
    @SuppressWarnings("unused")
    private final String TAG = "EditSongFragmentLyrics";
    private float editTextSize = 11;
    private int cursorPos=0;
    private boolean addUndoStep = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivityInterface = (MainActivityInterface) context;
        editSongFragmentInterface = (EditSongFragmentInterface) context;
        // Hide the keyboard
        //mainActivityInterface.getSoftKeyboard().hideKeyboard(requireActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the keyboard
        //mainActivityInterface.getSoftKeyboard().hideKeyboard(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hide the keyboard
        //mainActivityInterface.getSoftKeyboard().hideKeyboard(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = EditSongLyricsBinding.inflate(inflater, container, false);

        // Put the values in
        setupValues();

        // Add listeners
        setupListeners();

        return myView.getRoot();
    }

    private void setupValues() {
        if (mainActivityInterface.getTempSong()!=null && !mainActivityInterface.getTempSong().getEditingAsChoPro()) {
            mainActivityInterface.getTranspose().checkChordFormat(mainActivityInterface.getTempSong());
        } else {
            String choProLyrics = mainActivityInterface.getTempSong().getLyrics();
            // Convert to OpenSong to detect the chord format
            String openSongLyrics = mainActivityInterface.getConvertChoPro().fromChordProToOpenSong(choProLyrics);
            // Set the lyrics back to the temp song
            mainActivityInterface.getTempSong().setLyrics(openSongLyrics);
            // Now detect the chord format
            mainActivityInterface.getTranspose().checkChordFormat(mainActivityInterface.getTempSong());
            // Now set the lyrics back as chordpro
            mainActivityInterface.getTempSong().setLyrics(choProLyrics);
        }


        if ((Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP &&
                mainActivityInterface.getSong().getFiletype().equals("PDF")) ||
                mainActivityInterface.getSong().getFiletype().equals("IMG")) {
            // Show the OCR button
            myView.ocr.setVisibility(View.VISIBLE);
        } else {
            myView.ocr.setVisibility(View.GONE);
        }

        myView.lyrics.setText(mainActivityInterface.getTempSong().getLyrics());
        mainActivityInterface.getProcessSong().editBoxToMultiline(myView.lyrics);
        editTextSize = mainActivityInterface.getPreferences().getMyPreferenceFloat("editTextSize",14);
        myView.lyrics.setTextSize(editTextSize);
        mainActivityInterface.getProcessSong().stretchEditBoxToLines(myView.lyrics,20);

        validUndoRedo(mainActivityInterface.getTempSong().getLyricsUndosPos());
    }

    private void setupListeners() {
        myView.lyrics.setOnFocusChangeListener((view, b) -> mainActivityInterface.enableSwipe("edit",!b));

        myView.lyrics.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mainActivityInterface.getTempSong().setLyrics(editable.toString());
                // If we are not undoing/redoing add the changes to the undo list
                if (addUndoStep) {
                    int lyricsUndosPos = mainActivityInterface.getTempSong().getLyricsUndosPos() + 1;
                    mainActivityInterface.getTempSong().setLyricsUndos(lyricsUndosPos, editable.toString());
                    mainActivityInterface.getTempSong().setLyricsUndosPos(lyricsUndosPos);
                    // Enable/disable the undo and redo button
                    validUndoRedo(lyricsUndosPos);
                } else {
                    // We were undoing/redoing, so we didn't do the above.  Now turn this off
                    addUndoStep = true;
                }
            }
        });

        myView.ocr.setOnClickListener(v -> {
            mainActivityInterface.navHome();
            if (mainActivityInterface.getSong().getFiletype().equals("PDF")) {
                mainActivityInterface.getOCR().getTextFromPDF(
                        mainActivityInterface.getSong().getFolder(), mainActivityInterface.getSong().getFilename());
            } else if (mainActivityInterface.getSong().getFiletype().equals("IMG")) {
                mainActivityInterface.getOCR().getTextFromImageFile(
                        mainActivityInterface.getSong().getFolder(),
                        mainActivityInterface.getSong().getFilename());
            }
        });

        myView.settingsButton.setOnClickListener(v -> {
            cursorPos = myView.lyrics.getSelectionStart();
            LyricsOptionsBottomSheet lyricsOptionsBottomSheet = new LyricsOptionsBottomSheet(this);
            lyricsOptionsBottomSheet.show(mainActivityInterface.getMyFragmentManager(),"LyricsBottomSheet");
        });

        myView.undoButton.setOnClickListener(v -> undoLyrics());
        myView.redoButton.setOnClickListener(v -> redoLyrics());

        // Scroll listener
        myView.nestedScrollView.setExtendedFabToAnimate(editSongFragmentInterface.getSaveButton());
        myView.nestedScrollView.setFabToAnimate(myView.undoButton);
        myView.nestedScrollView.setFab2ToAnimate(myView.redoButton);
        myView.nestedScrollView.setFab3ToAnimate(myView.settingsButton);

        // Resize the bottom padding to the soft keyboard height or half the screen height for the soft keyboard (workaround)
        mainActivityInterface.getWindowFlags().adjustViewPadding(mainActivityInterface,myView.resizeForKeyboardLayout);
    }

    private void undoLyrics() {
        // If we can go back, undo the changes
        int lyricsUndosPos = mainActivityInterface.getTempSong().getLyricsUndosPos();
        if (lyricsUndosPos>0) {
            lyricsUndosPos = lyricsUndosPos - 1;
            mainActivityInterface.getTempSong().setLyricsUndosPos(lyricsUndosPos);
            addUndoStep = false;
            myView.lyrics.setText(mainActivityInterface.getTempSong().getLyricsUndos().get(lyricsUndosPos));
        }
        validUndoRedo(lyricsUndosPos);
    }
    private void redoLyrics() {
        // If we can go forward, redo the changes
        int lyricsUndosPos = mainActivityInterface.getTempSong().getLyricsUndosPos();
        if (lyricsUndosPos<mainActivityInterface.getTempSong().getLyricsUndos().size()-1) {
            lyricsUndosPos = lyricsUndosPos + 1;
            addUndoStep = false;
            mainActivityInterface.getTempSong().setLyricsUndosPos(lyricsUndosPos);
            myView.lyrics.setText(mainActivityInterface.getTempSong().getLyricsUndos().get(lyricsUndosPos));
        }
        validUndoRedo(lyricsUndosPos);
    }
    private void validUndoRedo(int currentPosition) {
        // Enable/disable the undo button
        myView.undoButton.setEnabled(currentPosition>0);

        // Enable/disable the redo button
        myView.redoButton.setEnabled(currentPosition<mainActivityInterface.getTempSong().getLyricsUndos().size()-1);
    }

    // The stuff below is called from the LyricsOptionsBottomSheet
    public float getEditTextSize() {
        return editTextSize;
    }
    public void setEditTextSize(float editTextSize) {
        this.editTextSize = editTextSize;
        myView.lyrics.setTextSize(editTextSize);
    }
    public void insertSection() {
        // Try to get the current text position
        String text = myView.lyrics.getText().toString();
        if (text.length()>cursorPos && cursorPos!=-1) {
            text = text.substring(0, cursorPos) + "[]\n" + text.substring(cursorPos);
            myView.lyrics.setText(text);
        }
        myView.lyrics.setSelection(cursorPos+1);
    }

    public void transpose(String direction) {
        // If nothing is selected, we select the entire song
        int selectedStart = myView.lyrics.getSelectionStart();
        int selectedEnd = myView.lyrics.getSelectionEnd();
        String selectedText;
        String key = null;
        if (selectedStart >= 0 && selectedEnd > selectedStart) {
            selectedText = myView.lyrics.getText().toString().substring(selectedStart,selectedEnd);
        } else {
            selectedText = myView.lyrics.getText().toString();
        }
        if (selectedText.equals(myView.lyrics.getText().toString())) {
            // We are transposing everything, so we'll pass the key to update
            key = mainActivityInterface.getTempSong().getKey();
        }

        // Get a temp variable as we might need to tweak first
        // This keeps the original as we will replace that later
        String processedSelectedText = selectedText;

        // If we are editing as choPro, we need to convert to OpenSong first
        if (mainActivityInterface.getTempSong().getEditingAsChoPro()) {
            processedSelectedText = mainActivityInterface.getConvertChoPro().fromChordProToOpenSong(processedSelectedText);
        }

        // Transpose the OpenSong lyrics using a temp song that only has the important stuff
        Song transposeSong = new Song();
        transposeSong.setLyrics(processedSelectedText);
        transposeSong.setDetectedChordFormat(mainActivityInterface.getTempSong().getDetectedChordFormat());
        transposeSong.setDesiredChordFormat(mainActivityInterface.getTempSong().getDesiredChordFormat());
        transposeSong.setKey(key);
        transposeSong = mainActivityInterface.getTranspose().doTranspose(transposeSong,
                direction,1,1,1);

        processedSelectedText = transposeSong.getLyrics();

        // If we were using ChoPro, convert back
        if (mainActivityInterface.getTempSong().getEditingAsChoPro()) {
            processedSelectedText =  mainActivityInterface.getConvertChoPro().fromOpenSongToChordPro(
                    processedSelectedText);
        }

        // Now get the non selected text
        String textBeforeSelection = "", textAfterSelection = "";
        if (selectedStart>=0 && selectedEnd>selectedStart) {
            textBeforeSelection = myView.lyrics.getText().toString().substring(0,selectedStart);
        }
        if (selectedStart >= 0 && selectedEnd > selectedStart) {
            textAfterSelection = myView.lyrics.getText().toString().substring(selectedEnd,myView.lyrics.getText().length());
            selectedEnd = selectedStart + processedSelectedText.length();
        }

        String newText = textBeforeSelection + processedSelectedText + textAfterSelection;
        myView.lyrics.setText(newText);
        mainActivityInterface.getTempSong().setLyrics(newText);
        if (key!=null) {
            mainActivityInterface.getTempSong().setKey(transposeSong.getKey());
        }

        // Highlight the new text
        if (selectedStart>=0) {
            myView.lyrics.setSelection(selectedStart,selectedEnd);
        }

        addUndoStep = true;

        // Don't add this as an undo/redo as it breaks the key
        //addUndoStep = false;
        //myView.lyrics.setText(mainActivityInterface.getTempSong().getLyrics());
    }

    public void convertToOpenSong() {
        // Get the lyrics converted
        String lyrics = mainActivityInterface.getConvertChoPro().fromChordProToOpenSong(mainActivityInterface.getTempSong().getLyrics());
        // Set them into the edit box - don't include an undo/redo step, so pretend we're carrying this out
        addUndoStep = false;
        myView.lyrics.setText(lyrics);
    }
    public void convertToChoPro() {
        // Get the lyrics converted
        String lyrics = mainActivityInterface.getConvertChoPro().fromOpenSongToChordPro(mainActivityInterface.getTempSong().getLyrics());
        // Set them into the edit box - don't include an undo/redo step, so pretend we're carrying this out
        addUndoStep = false;
        myView.lyrics.setText(lyrics);
    }

    public void autoFix() {
        String lyrics;
        if (mainActivityInterface.getTempSong().getEditingAsChoPro()) {
            lyrics = mainActivityInterface.getConvertChoPro().fromChordProToOpenSong(mainActivityInterface.getTempSong().getLyrics());
        } else {
            lyrics = mainActivityInterface.getTempSong().getLyrics();
        }

        // Use the text conversion to fix
        lyrics = mainActivityInterface.getConvertTextSong().convertText(lyrics);

        // Put back (includes undo step)
        if (mainActivityInterface.getTempSong().getEditingAsChoPro()) {
            lyrics = mainActivityInterface.getConvertChoPro().fromOpenSongToChordPro(lyrics);
        }
        myView.lyrics.setText(lyrics);
        mainActivityInterface.getShowToast().doIt(getString(R.string.success));
    }

}
