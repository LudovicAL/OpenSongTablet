package com.garethevans.church.opensongtablet.screensetup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;
import com.garethevans.church.opensongtablet.songprocessing.SongDetailsBottomSheet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AppActionBar {

    // This holds references to the items in the ActionBar (except the battery)
    // Battery changes get sent via the mainactivityInterface
    private final String TAG = "AppActionBar";
    private final Context c;
    private final MainActivityInterface mainActivityInterface;
    private final ActionBar actionBar;
    private final TextView title;
    private final TextView author;
    private final TextView key;
    private final TextView capo;
    private final TextView clock;
    private final Handler delayactionBarHide;
    private final Runnable hideActionBarRunnable;
    private final int autoHideTime = 1200;
    private float clockTextSize;
    private boolean clock24hFormat, clockOn, hideActionBar;
    private Timer clockTimer;
    private TimerTask clockTimerTask;

    private boolean performanceMode;

    public AppActionBar(Context c, ActionBar actionBar, TextView title, TextView author,
                        TextView key, TextView capo, TextView clock) {
        this.c = c;
        mainActivityInterface = (MainActivityInterface) c;

        this.actionBar = actionBar;
        this.title = title;
        this.author = author;
        this.key = key;
        this.capo = capo;
        this.clock = clock;
        delayactionBarHide = new Handler();
        hideActionBarRunnable = () -> {
            if (actionBar != null && actionBar.isShowing()) {
                actionBar.hide();
            }
        };

        updateActionBarPrefs();

        clockTimerTask = new TimerTask() {
            @Override
            public void run() {
                updateClock();
            }
        };
        clockTimer = new Timer();
        clockTimer.scheduleAtFixedRate(clockTimerTask,0,10000);
    }

    private void updateActionBarPrefs() {
        clockTextSize = mainActivityInterface.getPreferences().getMyPreferenceFloat(c,"clockTextSize",9.0f);
        clock24hFormat = mainActivityInterface.getPreferences().getMyPreferenceBoolean(c,"clock24hFormat",true);
        clockOn = mainActivityInterface.getPreferences().getMyPreferenceBoolean(c,"clockOn",true);
        hideActionBar = mainActivityInterface.getPreferences().getMyPreferenceBoolean(c,"hideActionBar",false);
    }

    public void setHideActionBar(boolean hideActionBar) {
        this.hideActionBar = hideActionBar;
    }
    public boolean getHideActionBar() {
        return hideActionBar;
    }
    public void setActionBar(Context c, MainActivityInterface mainActivityInterface, String newtitle) {
        if (newtitle == null) {
            // We are in the Performance/Stage mode
            float mainsize = mainActivityInterface.getPreferences().getMyPreferenceFloat(c,"songTitleSize",13.0f);

            if (title != null && mainActivityInterface.getSong().getTitle() != null) {
                title.setTextSize(mainsize);
                title.setText(mainActivityInterface.getSong().getTitle());
            }
            if (author != null && mainActivityInterface.getSong().getAuthor() != null &&
                    !mainActivityInterface.getSong().getAuthor().isEmpty()) {
                author.setTextSize(mainActivityInterface.getPreferences().getMyPreferenceFloat(c,"songAuthorSize",11.0f));
                author.setText(mainActivityInterface.getSong().getAuthor());
                hideView(author, false);
            } else {
                hideView(author, true);
            }
            if (key != null && mainActivityInterface.getSong().getKey() != null &&
                    !mainActivityInterface.getSong().getKey().isEmpty()) {
                String k = " (" + mainActivityInterface.getSong().getKey() + ")";
                key.setTextSize(mainsize);
                capo.setTextSize(mainsize);
                key.setText(k);
                hideView(key, false);
            } else {
                hideView(key, true);
            }
            if (title!=null) {
                title.setOnClickListener(v -> openDetails(mainActivityInterface));
                title.setOnLongClickListener(view -> {
                    editSong(mainActivityInterface);
                    return true;
                });
            }
            if (author!=null) {
                author.setOnClickListener(v -> openDetails(mainActivityInterface));
                author.setOnLongClickListener(view -> {
                    editSong(mainActivityInterface);
                    return true;
                });
            }
            if (key!=null) {
                key.setOnClickListener(v -> openDetails(mainActivityInterface));
                key.setOnLongClickListener(view -> {
                    editSong(mainActivityInterface);
                    return true;
                });
            }

        } else {
            // We are in a different fragment, so don't hide the song info stuff
            actionBar.show();
            if (title != null) {
                title.setTextSize(18.0f);
                title.setText(newtitle);
                hideView(author, true);
                hideView(key, true);
            }
        }
    }

    private void openDetails(MainActivityInterface mainActivityInterface) {
        if (!mainActivityInterface.getSong().getTitle().equals("Welcome to OpenSongApp")) {
            SongDetailsBottomSheet songDetailsBottomSheet = new SongDetailsBottomSheet();
            songDetailsBottomSheet.show(mainActivityInterface.getMyFragmentManager(), "songDetailsBottomSheet");
        }
    }
    private void editSong(MainActivityInterface mainActivityInterface) {
        if (!mainActivityInterface.getSong().getTitle().equals("Welcome to OpenSongApp")) {
            mainActivityInterface.navigateToFragment("opensongapp://settings/edit", 0);
        }
    }

    public void setActionBarCapo(TextView capo, String string) {
        capo.setText(string);
    }

    public void updateActionBarSettings(String prefName, float value, boolean isvisible) {
        Log.d(TAG,"prefName="+prefName+"  value="+value+"  isvisible="+isvisible);

        switch (prefName) {
            case "batteryDialOn":
                mainActivityInterface.getBatteryStatus().setBatteryDialOn(isvisible);
                break;
            case "batteryDialThickness":
                mainActivityInterface.getBatteryStatus().setBatteryDialThickness((int)value);
                mainActivityInterface.getBatteryStatus().setBatteryImage();
                break;
            case "batteryTextOn":
                mainActivityInterface.getBatteryStatus().setBatteryTextOn(isvisible);
                break;
            case "batteryTextSize":
                mainActivityInterface.getBatteryStatus().setBatteryTextSize(value);
                break;
            case "clockOn":
                hideView(clock,!isvisible);
                break;
            case "clock24hFormat":
                updateClock();
                break;
            case "clockTextSize":
                clock.setTextSize(value);
                break;
            case "songTitleSize":
                title.setTextSize(value);
                key.setTextSize(value);
                capo.setTextSize(value);
                break;
            case "songAuthorSize":
                author.setTextSize(value);
                break;
            case "hideActionBar":
                setHideActionBar(!isvisible);
                break;
        }
    }

    private void hideView(View v, boolean hide) {
        if (hide) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    // Action bar stuff
    public void toggleActionBar(boolean wasScrolling, boolean scrollButton,
                                boolean menusActive) {
        try {
            delayactionBarHide.removeCallbacks(hideActionBarRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (actionBar != null) {
            if (wasScrolling || scrollButton) {
                if (hideActionBar && !menusActive) {
                    actionBar.hide();
                }
            } else if (!menusActive) {
                if (actionBar.isShowing() && hideActionBar) {
                    delayactionBarHide.postDelayed(hideActionBarRunnable, 500);
                } else {
                    actionBar.show();
                    // Set a runnable to hide it after 3 seconds
                    if (hideActionBar) {
                        delayactionBarHide.postDelayed(hideActionBarRunnable, autoHideTime);
                    }
                }
            }
        }
    }


    // Set when entering/exiting performance mode
    public void setPerformanceMode(boolean inPerformanceMode) {
        performanceMode = inPerformanceMode;
    }

    // Show/hide the actionbar
    public void showActionBar(boolean menuOpen) {
        // Show the ActionBar based on the user preferences
        // If we are in performance mode (boolean set when opening/closing PerformanceFragment)
        // The we can autohide if the user preferences state that's what is wanted
        // If we are not in performance mode, we don't set a runnable to autohide them
        try {
            delayactionBarHide.removeCallbacks(hideActionBarRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (actionBar != null) {
            actionBar.show();
        }

        if (hideActionBar && performanceMode && !menuOpen) {
            try {
                delayactionBarHide.postDelayed(hideActionBarRunnable, autoHideTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeCallBacks() {
        delayactionBarHide.removeCallbacks(hideActionBarRunnable);
    }

    // Flash on/off for metronome
    public void doFlash(int colorBar) {
        actionBar.setBackgroundDrawable(new ColorDrawable(colorBar));
    }

    // Get the actionbar height - fakes a height of 0 if autohiding
    public int getActionBarHeight() {
        if (hideActionBar && performanceMode) {
            return 0;
        } else {
            return actionBar.getHeight();
        }
    }

    public void updateClock() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df;
        if (clock24hFormat) {
            df = new SimpleDateFormat("HH:mm", mainActivityInterface.getLocale());
        } else {
            df = new SimpleDateFormat("h:mm", mainActivityInterface.getLocale());
        }
        String formattedTime = df.format(cal.getTime());

        clock.post(() -> {
            if (clockOn) {
                clock.setVisibility(View.VISIBLE);
            } else {
                clock.setVisibility(View.GONE);
            }
            clock.setTextSize(clockTextSize);
            clock.setText(formattedTime);
        });
    }

    public void stopTimers() {
        if (clockTimer!=null) {
            clockTimer.cancel();
            clockTimer.purge();
        }
        clockTimer = null;
        if (clockTimerTask!=null) {
            clockTimerTask.cancel();
        }
        clockTimerTask = null;
    }
}
