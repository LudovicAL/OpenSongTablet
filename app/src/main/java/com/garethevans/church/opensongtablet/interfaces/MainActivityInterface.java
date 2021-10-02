package com.garethevans.church.opensongtablet.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.garethevans.church.opensongtablet.abcnotation.ABCNotation;
import com.garethevans.church.opensongtablet.animation.CustomAnimation;
import com.garethevans.church.opensongtablet.animation.ShowCase;
import com.garethevans.church.opensongtablet.appdata.AlertChecks;
import com.garethevans.church.opensongtablet.appdata.CheckInternet;
import com.garethevans.church.opensongtablet.appdata.SetTypeFace;
import com.garethevans.church.opensongtablet.appdata.VersionNumber;
import com.garethevans.church.opensongtablet.autoscroll.Autoscroll;
import com.garethevans.church.opensongtablet.bible.Bible;
import com.garethevans.church.opensongtablet.ccli.CCLILog;
import com.garethevans.church.opensongtablet.chords.Transpose;
import com.garethevans.church.opensongtablet.controls.Gestures;
import com.garethevans.church.opensongtablet.controls.PageButtons;
import com.garethevans.church.opensongtablet.controls.PedalActions;
import com.garethevans.church.opensongtablet.controls.Swipes;
import com.garethevans.church.opensongtablet.customslides.CustomSlide;
import com.garethevans.church.opensongtablet.customviews.DrawNotes;
import com.garethevans.church.opensongtablet.export.ExportActions;
import com.garethevans.church.opensongtablet.export.PrepareFormats;
import com.garethevans.church.opensongtablet.filemanagement.LoadSong;
import com.garethevans.church.opensongtablet.filemanagement.SaveSong;
import com.garethevans.church.opensongtablet.filemanagement.StorageAccess;
import com.garethevans.church.opensongtablet.importsongs.WebDownload;
import com.garethevans.church.opensongtablet.metronome.Metronome;
import com.garethevans.church.opensongtablet.midi.Midi;
import com.garethevans.church.opensongtablet.nearby.NearbyConnections;
import com.garethevans.church.opensongtablet.pads.Pad;
import com.garethevans.church.opensongtablet.pdf.MakePDF;
import com.garethevans.church.opensongtablet.pdf.OCR;
import com.garethevans.church.opensongtablet.pdf.PDFSong;
import com.garethevans.church.opensongtablet.performance.DisplayPrevNext;
import com.garethevans.church.opensongtablet.preferences.Preferences;
import com.garethevans.church.opensongtablet.preferences.ProfileActions;
import com.garethevans.church.opensongtablet.screensetup.AppActionBar;
import com.garethevans.church.opensongtablet.screensetup.DoVibrate;
import com.garethevans.church.opensongtablet.screensetup.ShowToast;
import com.garethevans.church.opensongtablet.screensetup.ThemeColors;
import com.garethevans.church.opensongtablet.secondarydisplay.PresentationCommon;
import com.garethevans.church.opensongtablet.setprocessing.CurrentSet;
import com.garethevans.church.opensongtablet.setprocessing.SetActions;
import com.garethevans.church.opensongtablet.songmenu.SongListBuildIndex;
import com.garethevans.church.opensongtablet.songprocessing.ConvertChoPro;
import com.garethevans.church.opensongtablet.songprocessing.ConvertOnSong;
import com.garethevans.church.opensongtablet.songprocessing.ConvertTextSong;
import com.garethevans.church.opensongtablet.songprocessing.ProcessSong;
import com.garethevans.church.opensongtablet.songprocessing.Song;
import com.garethevans.church.opensongtablet.songprocessing.SongSheetHeaders;
import com.garethevans.church.opensongtablet.sqlite.CommonSQL;
import com.garethevans.church.opensongtablet.sqlite.NonOpenSongSQLiteHelper;
import com.garethevans.church.opensongtablet.sqlite.SQLiteHelper;
import com.garethevans.church.opensongtablet.tools.TimeTools;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Locale;

public interface MainActivityInterface {

    // The most common classes
    StorageAccess getStorageAccess();
    Preferences getPreferences();

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
    void navHome();
    void songMenuActionButtonShow(boolean show);
    void lockDrawer(boolean lock);
    void closeDrawer(boolean close);
    void doSongLoad(String folder, String filename, boolean closeDrawer);
    void loadSongFromSet(int position);
    void updateKeyAndLyrics(Song song);
    void showSaveAllowed(boolean saveAllowed);
    void registerFragment(Fragment frag, String what);
    void displayAreYouSure(String what, String action, ArrayList<String> arguments, String fragName, Fragment callingFragment, Song song);
    void confirmedAction(boolean agree, String what, ArrayList<String> arguments, String fragName, Fragment callingFragment, Song song);
    void refreshAll();
    void doExport(String what);
    void refreshSetList();
    void openDialog(BottomSheetDialogFragment frag, String tag);
    void updateFragment(String fragName, Fragment callingFragment, ArrayList<String> arguments);
    void updateSongMenu(String fragName, Fragment callingFragment, ArrayList<String> arguments);
    void updateSongMenu(Song song);
    boolean songChanged();
    void updateSetList();
    void toggleAutoscroll();
    void fadeoutPad();
    boolean playPad();
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
    ActionBar getMyActionBar();
    SetTypeFace getMyFonts();
    ThemeColors getMyThemeColors();
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
    Autoscroll getAutoscroll();
    Pad getPad();
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
    CheckInternet getCheckInternet();
    void isWebConnected(Fragment fragment, int fragId, boolean isConnected);
    void songSelectDownloadPDF(Fragment fragment, int fragId, Uri uri);
    //void setDisplay(Display display);
    //Display getDisplay();
    //ExternalDisplay getExternalDisplay();
    PresentationCommon getPresentationCommon();
    void openDocument(String guideId, String location);
    PrepareFormats getPrepareFormats();
    void setSectionViews(ArrayList<View> views);
    ArrayList<View> getSectionViews();
    ArrayList<Integer> getSectionWidths();
    ArrayList<Integer> getSectionHeights();
    void addSectionSize(int width, int height);
    void setSongSheetTitleLayout(LinearLayout linearLayout);
    LinearLayout getSongSheetTitleLayout();
    SongSheetHeaders getSongSheetHeaders();
    ArrayList<Integer> getSongSheetTitleLayoutSize();
    void setSongSheetTitleLayoutSize(ArrayList<Integer> sizes);
    void enableSwipe(boolean canSwipe);
    ArrayList<Song> getSongsFound(String whichMenu);
    TimeTools getTimeTools();
    DisplayPrevNext getDisplayPrevNext();
    int getPositionOfSongInMenu();
    Song getSongInMenu(int position);
    ArrayList<Song> getSongsInMenu();
    void toggleMetronome();
    FragmentManager getMyFragmentManager();
    Bible getBible();
    CustomSlide getCustomSlide();
    void chooseMenu(boolean showSetMenu);
    void updateSizes(int width, int height);
    int[] getDisplayMetrics();
}

