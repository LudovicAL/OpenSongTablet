package com.garethevans.church.opensongtablet.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.databinding.SettingsCategoriesBinding;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class SettingsCategories extends Fragment {

    private SettingsCategoriesBinding myView;
    private MainActivityInterface mainActivityInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivityInterface = (MainActivityInterface) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myView = SettingsCategoriesBinding.inflate(inflater,container,false);
        mainActivityInterface.updateToolbar(null,getString(R.string.settings));

        // Hide the features not available to this device
        hideUnavailable();

        // Set listeners
        setListeners();

        return myView.getRoot();
    }



    private void hideUnavailable() {
        // If the user doesn't have Google API availability, they can't use the connect feature
        setPlayEnabled(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS);
        // If they don't have midi functionality, remove this
        setMidiEnabled(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI));
    }

    private void setPlayEnabled(boolean enabled) {
        myView.connectButton.setEnabled(enabled);
        myView.connectLine.setEnabled(enabled);
        if (enabled) {
            myView.needPlayServices.setVisibility(View.GONE);
        } else {
            myView.needPlayServices.setVisibility(View.VISIBLE);
        }
    }

    private void setMidiEnabled(boolean enabled) {
        String message;
        if (enabled) {
            message = getString(R.string.midi_description);
        } else {
            message = getString(R.string.not_available);
        }
        myView.midiButton.setEnabled(enabled);
        ((TextView)myView.midiButton.findViewById(R.id.subText)).setText(message);
    }

    private void setListeners() {
        myView.ccliButton.setOnClickListener(v -> mainActivityInterface.navigateToFragment(R.id.settingsCCLI));
        myView.storageButton.setOnClickListener(v -> mainActivityInterface.navigateToFragment(R.id.storage_graph));
        myView.displayButton.setOnClickListener(v -> mainActivityInterface.navigateToFragment(R.id.display_graph));
        myView.connectButton.setOnClickListener(v -> {
            // Check we have the required permissions
            if (mainActivityInterface.requestNearbyPermissions()) {
                mainActivityInterface.navigateToFragment(R.id.nearbyConnectionsFragment);
            }
        });
        myView.midiButton.setOnClickListener(v -> mainActivityInterface.navigateToFragment(R.id.midiFragment));
        myView.aboutButton.setOnClickListener(v -> mainActivityInterface.navigateToFragment(R.id.about_graph));
        myView.gesturesButton.setOnClickListener(v -> mainActivityInterface.navigateToFragment(R.id.controlMenuFragment));
        myView.playServicesHow.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_services_help)));
            startActivity(i);
        });
    }


    // TODO - have an option to reset the app preferences and permissions
    /*private void clearTheCaches() {
        // Clear the user preferences
        File cacheDirectory = getCacheDir();
        File applicationDirectory;
        if (cacheDirectory!=null && cacheDirectory.getParent()!=null) {
            applicationDirectory = new File(cacheDirectory.getParent());
        } else {
            applicationDirectory = cacheDirectory;
        }
        if (applicationDirectory!=null && applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            if (fileNames!=null) {
                for (String fileName : fileNames) {
                    if (!fileName.equals("lib")) {
                        File ftodel = new File(applicationDirectory,fileName);
                        doDeleteFile(ftodel);
                    }
                }
            }
        }
        try {
            PreferenceManager.getDefaultSharedPreferences(BootUpCheck.this).edit().clear().apply();
        } catch (Exception e) {
            Log.d("d","Error clearing new preferences");
        }

        // Clear the old preferences (that will eventually get phased out!)
        try {
            BootUpCheck.this.getSharedPreferences("OpenSongApp", Context.MODE_PRIVATE).edit().clear().apply();
        } catch (Exception e) {
            Log.d("d","Error clearing old preferences");
            e.printStackTrace();
        }


        // Clear the cache and data folder
        try {
            File dir = BootUpCheck.this.getCacheDir();
            doDeleteCacheFile(dir);

            // Set the last used version to 1 (otherwise we get stuck in a loop!)
            preferences.setMyPreferenceInt(BootUpCheck.this,"lastUsedVersion",1);
            // Now restart the BootUp activity
            BootUpCheck.this.recreate();

        } catch (Exception e) {
            Log.d("d","Error clearing the cache directory");
            e.printStackTrace();
        }

        try {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            Log.d("BootUpCheck","Clearing data");
            if (am!=null) {
                am.clearApplicationUserData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean doDeleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                if (children!=null) {
                    for (String child : children) {
                        deletedAll = doDeleteFile(new File(file, child)) && deletedAll;
                    }
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }

    private boolean doDeleteCacheFile(File file) {

        if (file != null && file.isDirectory()) {
            String[] children = file.list();
            if (children!=null) {
                for (String child : children) {
                    boolean success = doDeleteCacheFile(new File(file, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return file.delete();
        } else if(file!= null && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }


    */
}