package com.garethevans.church.opensongtablet.appdata;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.databinding.SettingsLanguageBinding;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;
import com.google.android.material.snackbar.Snackbar;

public class LanguageFragment extends Fragment {

    private SettingsLanguageBinding myView;
    private MainActivityInterface mainActivityInterface;
    private final String[] languageCodes = new String[] {"af","cs","de","el","en","es","fr","hu","it","ja","pl","pt","ru","sr","sv","zh"};

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivityInterface = (MainActivityInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = SettingsLanguageBinding.inflate(inflater, container, false);

        // Update the toolbar
        mainActivityInterface.updateToolbar(getString(R.string.language));

        // Build the radio group
        buildRadioGroup();

        return myView.getRoot();
    }

    private void buildRadioGroup() {
        String languageCode = mainActivityInterface.getPreferences().getMyPreferenceString(getContext(), "language", "en");
        String[] languages = requireContext().getResources().getStringArray(R.array.languagelist);
        int id = -1;
        for (int x=0; x<languages.length; x++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(languages[x].toUpperCase());
            radioButton.setId(View.generateViewId());
            radioButton.setTag(languageCodes[x]);

            radioButton.setPadding(24,24,24,24);
            if (languageCode.equals(languageCodes[x])) {
                id = radioButton.getId();
            }
            myView.languageGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton button = myView.languageGroup.findViewById(checkedId);
                String tag = button.getTag().toString();
                mainActivityInterface.getPreferences().setMyPreferenceString(getContext(),"language",tag);
                Snackbar.make(requireActivity().findViewById(R.id.coordinator),"You will need to restart the app to see the changes",Snackbar.LENGTH_LONG).show();
            });
            myView.languageGroup.addView(radioButton);
        }
        try{
            myView.languageGroup.check(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myView = null;
    }
}
