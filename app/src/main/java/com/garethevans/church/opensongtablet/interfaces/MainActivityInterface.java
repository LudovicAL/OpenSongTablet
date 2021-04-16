package com.garethevans.church.opensongtablet.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.garethevans.church.opensongtablet.abcnotation.ABCNotation;
import com.garethevans.church.opensongtablet.animation.CustomAnimation;
import com.garethevans.church.opensongtablet.animation.ShowCase;
import com.garethevans.church.opensongtablet.appdata.AlertChecks;
import com.garethevans.church.opensongtablet.appdata.SetTypeFace;
import com.garethevans.church.opensongtablet.appdata.VersionNumber;
import com.garethevans.church.opensongtablet.autoscroll.AutoscrollActions;
import com.garethevans.church.opensongtablet.ccli.CCLILog;
import com.garethevans.church.opensongtablet.chords.Transpose;
import com.garethevans.church.opensongtablet.controls.Gestures;
import com.garethevans.church.opensongtablet.controls.PageButtons;
import com.garethevans.church.opensongtablet.controls.PedalActions;
import com.garethevans.church.opensongtablet.controls.Swipes;
import com.garethevans.church.opensongtablet.customviews.DrawNotes;
import com.garethevans.church.opensongtablet.export.ExportActions;
import com.garethevans.church.opensongtablet.export.MakePDF;
import com.garethevans.church.opensongtablet.filemanagement.LoadSong;
import com.garethevans.church.opensongtablet.filemanagement.SaveSong;
import com.garethevans.church.opensongtablet.filemanagement.StorageAccess;
import com.garethevans.church.opensongtablet.importsongs.OCR;
import com.garethevans.church.opensongtablet.importsongs.WebDownload;
import com.garethevans.church.opensongtablet.metronome.Metronome;
import com.garethevans.church.opensongtablet.midi.Midi;
import com.garethevans.church.opensongtablet.nearby.NearbyConnections;
import com.garethevans.church.opensongtablet.pads.PadFunctions;
import com.garethevans.church.opensongtablet.preferences.Preferences;
import com.garethevans.church.opensongtablet.preferences.ProfileActions;
import com.garethevans.church.opensongtablet.screensetup.AppActionBar;
import com.garethevans.church.opensongtablet.screensetup.DoVibrate;
import com.garethevans.church.opensongtablet.screensetup.ShowToast;
import com.garethevans.church.opensongtablet.screensetup.ThemeColors;
import com.garethevans.church.opensongtablet.setprocessing.CurrentSet;
import com.garethevans.church.opensongtablet.setprocessing.SetActions;
import com.garethevans.church.opensongtablet.songprocessing.ConvertChoPro;
import com.garethevans.church.opensongtablet.songprocessing.ConvertOnSong;
import com.garethevans.church.opensongtablet.songprocessing.ConvertTextSong;
import com.garethevans.church.opensongtablet.songprocessing.PDFSong;
import com.garethevans.church.opensongtablet.songprocessing.ProcessSong;
import com.garethevans.church.opensongtablet.songprocessing.Song;
import com.garethevans.church.opensongtablet.songsandsetsmenu.SongListBuildIndex;
import com.garethevans.church.opensongtablet.sqlite.CommonSQL;
import com.garethevans.church.opensongtablet.sqlite.NonOpenSongSQLiteHelper;
import com.garethevans.church.opensongtablet.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.Locale;

public interface MainActivityInterface {
    void hideActionButton(boolean hide);
    void hideActionBar(boolean hide);
    void updateToolbar(String what);
    void updateActionBarSettings(String prefName, int intval, float floatval, boolean isvisible);
    void showTutorial(String what);
    void indexSongs();
    void initialiseActivity();
    void moveToSongInSongMenu();
    void hideKeyboard();
    void navigateToFragment(String deepLink, int id);
    void popTheBackStack(int id, boolean inclusive);
    void returnToHome(Fragment fragment, Bundle bundle);
    void songMenuActionButtonShow(boolean show);
    void lockDrawer(boolean lock);
    void closeDrawer(boolean close);
    void doSongLoad(String folder, String filename);
    void loadSongFromSet();
    void updateKeyAndLyrics(Song song);
    void editSongSaveButtonAnimation(boolean pulse);
    void registerFragment(Fragment frag, String what);
    void displayAreYouSure(String what, String action, ArrayList<String> arguments, String fragName, Fragment callingFragment, Song song);
    void confirmedAction(boolean agree, String what, ArrayList<String> arguments, String fragName, Fragment callingFragment, Song song);
    void refreshAll();
    void doExport(String what);
    void refreshSetList();
    void openDialog(DialogFragment frag, String tag);
    void updateFragment(String fragName, Fragment callingFragment, ArrayList<String> arguments);
    void updateSongMenu(String fragName, Fragment callingFragment, ArrayList<String> arguments);
    void updateSong(Song song);
    void setOriginalSong(Song originalSong);
    Song getOriginalSong();
    boolean songChanged();
    void updateSetList();
    void stopAutoscroll();
    void startAutoscroll();
    void fadeoutPad();
    void playPad();
    void updateConnectionsLog();
    boolean requestNearbyPermissions();
    boolean requestCoarseLocationPermissions();
    boolean requestFineLocationPermissions();
    void registerMidiAction(boolean actionDown, boolean actionUp, boolean actionLong, String note);
    void installPlayServices();
    void fixOptionsMenu();
    void fullIndex();
    void quickSongMenuBuild();
    void setFullIndexRequired(boolean fullIndexRequired);
    void changeActionBarVisible(boolean wasScrolling, boolean scrollButton);

    // Get the helpers initialised in the main activity
    NearbyConnections getNearbyConnections(MainActivityInterface mainActivityInterface);
    NearbyConnections getNearbyConnections();
    Midi getMidi(MainActivityInterface mainActivityInterface);
    Midi getMidi();
    DrawerLayout getDrawer();
    ActionBar getAb();
    MediaPlayer getMediaPlayer(int i);
    SetTypeFace getMyFonts();
    ThemeColors getMyThemeColors();
    StorageAccess getStorageAccess();
    Preferences getPreferences();
    ExportActions getExportActions();
    ConvertChoPro getConvertChoPro();
    ConvertOnSong getConvertOnSong();
    ConvertTextSong getConvertTextSong();
    ProcessSong getProcessSong();
    Song getSong();
    Song getIndexingSong();
    Song getTempSong();
    void setSong(Song song);
    void setIndexingSong(Song indexingSong);
    void setTempSong(Song tempSong);
    SQLiteHelper getSQLiteHelper();
    NonOpenSongSQLiteHelper getNonOpenSongSQLiteHelper();
    CommonSQL getCommonSQL();
    CCLILog getCCLILog();
    PedalActions getPedalActions();
    Gestures getGestures();
    DoVibrate getDoVibrate();
    String getImportFilename();
    Uri getImportUri();
    void setImportFilename(String importFilename);
    void setImportUri(Uri importUri);
    WebDownload getWebDownload();
    ShowToast getShowToast();
    String getMode();
    void setMode(String whichMode);
    void setNearbyOpen(boolean nearbyOpen);
    Locale getLocale();
    CurrentSet getCurrentSet();
    SetActions getSetActions();
    LoadSong getLoadSong();
    SaveSong getSaveSong();
    Activity getActivity();
    String getWhattodo();
    void setWhattodo(String whattodo);
    PageButtons getPageButtons();
    AutoscrollActions getAutoscrollActions();
    PadFunctions getPadFunctions();
    Metronome getMetronome();
    SongListBuildIndex getSongListBuildIndex();
    CustomAnimation getCustomAnimation();
    PDFSong getPDFSong();
    ShowCase getShowCase();
    OCR getOCR();
    MakePDF getMakePDF();
    VersionNumber getVersionNumber();
    Transpose getTranspose();
    AppActionBar getAppActionBar();
    Swipes getSwipes();
    int getFragmentOpen();
    void updatePageButtonLayout();
    void refreshMenuItems();
    void setScreenshot(Bitmap bitmap);
    Bitmap getScreenshot();
    ABCNotation getAbcNotation();
    AlertChecks getAlertChecks();
    void setMainActivityInterface(MainActivityInterface mainActivityInterface);
    MainActivityInterface getMainActivityInterface();
    DrawNotes getDrawNotes();
    void setDrawNotes(DrawNotes view);
    ProfileActions getProfileActions();
}
