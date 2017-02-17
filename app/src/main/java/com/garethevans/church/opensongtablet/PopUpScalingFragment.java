package com.garethevans.church.opensongtablet;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class PopUpScalingFragment extends DialogFragment {

    static PopUpScalingFragment newInstance() {
        PopUpScalingFragment frag;
        frag = new PopUpScalingFragment();
        return frag;
    }

    public interface MyInterface {
        void refreshAll();
    }

    private MyInterface mListener;

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {
        mListener = (MyInterface) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    LinearLayout fontsize_change_group;
    LinearLayout maxAutoScale_Group;
    SeekBar fontsize_seekbar;
    SeekBar maxAutoScale_seekBar;
    SeekBar minAutoScale_seekBar;
    TextView fontsize_TextView;
    TextView maxAutoScale_TextView;
    TextView minAutoScale_TextView;
    SwitchCompat switchAutoScaleOnOff_SwitchCompat;
    SwitchCompat switchAutoScaleWidthFull_SwitchCompat;
    SwitchCompat overrideFull_Switch;
    SwitchCompat overrideWidth_Switch;
    Button closebutton;

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null && getDialog() != null) {
            PopUpSizeAndAlpha.decoratePopUp(getActivity(),getDialog());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getActivity().getResources().getString(R.string.options_options_scale));
        getDialog().setCanceledOnTouchOutside(true);
        View V = inflater.inflate(R.layout.popup_scaling, container, false);

        // Initialise the views
        maxAutoScale_seekBar = (SeekBar) V.findViewById(R.id.maxAutoScale_seekBar);
        minAutoScale_seekBar = (SeekBar) V.findViewById(R.id.minAutoScale_seekBar);
        fontsize_change_group = (LinearLayout) V.findViewById(R.id.fontsize_change_group);
        maxAutoScale_Group = (LinearLayout) V.findViewById(R.id.maxAutoScale_Group);
        fontsize_seekbar = (SeekBar) V.findViewById(R.id.fontsize_seekbar);
        maxAutoScale_TextView = (TextView) V.findViewById(R.id.maxAutoScale_TextView);
        minAutoScale_TextView = (TextView) V.findViewById(R.id.minAutoScale_TextView);
        fontsize_TextView = (TextView) V.findViewById(R.id.fontsize_TextView);
        switchAutoScaleOnOff_SwitchCompat = (SwitchCompat) V.findViewById(R.id.switchAutoScaleOnOff_SwitchCompat);
        switchAutoScaleWidthFull_SwitchCompat = (SwitchCompat) V.findViewById(R.id.switchAutoScaleWidthFull_SwitchCompat);
        overrideFull_Switch = (SwitchCompat) V.findViewById(R.id.overrideFull_Switch);
        overrideWidth_Switch = (SwitchCompat) V.findViewById(R.id.overrideWidth_Switch);
        closebutton = (Button) V.findViewById(R.id.closebutton);

        // Set the seekbars to the currently chosen values;
        switch (FullscreenActivity.toggleYScale) {
            case "W":
                // Width only
                switchAutoScaleOnOff_SwitchCompat.setChecked(true);
                switchAutoScaleWidthFull_SwitchCompat.setChecked(false);
                switchAutoScaleWidthFull_SwitchCompat.setVisibility(View.VISIBLE);
                maxAutoScale_Group.setVisibility(View.VISIBLE);
                fontsize_change_group.setVisibility(View.GONE);
                break;
            case "Y":
                // Full
                switchAutoScaleOnOff_SwitchCompat.setChecked(true);
                switchAutoScaleWidthFull_SwitchCompat.setChecked(true);
                switchAutoScaleWidthFull_SwitchCompat.setVisibility(View.VISIBLE);
                maxAutoScale_Group.setVisibility(View.VISIBLE);
                fontsize_change_group.setVisibility(View.GONE);
                break;
            default:
                // Off
                switchAutoScaleOnOff_SwitchCompat.setChecked(false);
                switchAutoScaleWidthFull_SwitchCompat.setChecked(false);
                switchAutoScaleWidthFull_SwitchCompat.setVisibility(View.GONE);
                maxAutoScale_Group.setVisibility(View.GONE);
                fontsize_change_group.setVisibility(View.VISIBLE);
                break;
        }

        overrideFull_Switch.setChecked(FullscreenActivity.override_fullscale);
        overrideWidth_Switch.setChecked(FullscreenActivity.override_widthscale);
        setupfontsizeseekbar();
        setupmaxfontsizeseekbar();
        setupminfontsizeseekbar();

        // If user has enabled autoscale and is allowing overriding the width, the font size should be shown
        if ((FullscreenActivity.toggleYScale.equals("W") || FullscreenActivity.toggleYScale.equals("Y")) &&
                FullscreenActivity.override_widthscale) {
            fontsize_change_group.setVisibility(View.VISIBLE);
        } else {
            fontsize_change_group.setVisibility(View.GONE);
        }

        // Set up listeners
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.refreshAll();
            }
        });

        fontsize_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatefontsize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updatefontsize();
            }
        });
        maxAutoScale_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatemaxfontsize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updatefontsize();
            }
        });
        minAutoScale_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateminfontsize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updatefontsize();
            }
        });
        overrideFull_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FullscreenActivity.override_fullscale = isChecked;
                Preferences.savePreferences();
            }
        });
        overrideWidth_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FullscreenActivity.override_widthscale = isChecked;
                // If user has enabled autoscale and is allowing overriding the width, the font size should be shown
                if ((FullscreenActivity.toggleYScale.equals("W") || FullscreenActivity.toggleYScale.equals("Y")) &&
                        FullscreenActivity.override_widthscale) {
                    fontsize_change_group.setVisibility(View.VISIBLE);
                } else {
                    fontsize_change_group.setVisibility(View.GONE);
                }
                Preferences.savePreferences();
             }
        });
        switchAutoScaleOnOff_SwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    switchAutoScaleWidthFull_SwitchCompat.setEnabled(true);
                    switchAutoScaleWidthFull_SwitchCompat.setVisibility(View.VISIBLE);
                    maxAutoScale_Group.setVisibility(View.VISIBLE);
                    fontsize_change_group.setVisibility(View.GONE);
                    if (switchAutoScaleWidthFull_SwitchCompat.isChecked()) {
                        FullscreenActivity.toggleYScale = "Y";
                    } else {
                        FullscreenActivity.toggleYScale = "W";
                    }
                } else {
                    FullscreenActivity.toggleYScale = "N";
                    switchAutoScaleWidthFull_SwitchCompat.setVisibility(View.GONE);
                    maxAutoScale_Group.setVisibility(View.GONE);
                    fontsize_change_group.setVisibility(View.VISIBLE);
                }
                // If user has enabled autoscale and is allowing overriding the width, the font size should be shown
                if ((FullscreenActivity.toggleYScale.equals("W") || FullscreenActivity.toggleYScale.equals("Y")) &&
                        FullscreenActivity.override_widthscale) {
                    fontsize_change_group.setVisibility(View.VISIBLE);
                } else {
                    fontsize_change_group.setVisibility(View.GONE);
                }
                Preferences.savePreferences();
            }
        });

        switchAutoScaleWidthFull_SwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && switchAutoScaleOnOff_SwitchCompat.isChecked()) {
                    FullscreenActivity.toggleYScale = "Y";
                    maxAutoScale_Group.setVisibility(View.VISIBLE);
                    fontsize_change_group.setVisibility(View.GONE);
                } else if (!b &&switchAutoScaleOnOff_SwitchCompat.isChecked()) {
                    FullscreenActivity.toggleYScale = "W";
                    maxAutoScale_Group.setVisibility(View.VISIBLE);
                    fontsize_change_group.setVisibility(View.GONE);
                } else {
                    FullscreenActivity.toggleYScale = "N";
                    maxAutoScale_Group.setVisibility(View.GONE);
                    fontsize_change_group.setVisibility(View.VISIBLE);
                }
                Preferences.savePreferences();
            }
        });

        return V;
    }

    public void setupfontsizeseekbar() {
        // Seekbar size is 80 wide
        // Add 4 to all values - min is then 4, max is then 84
        if (FullscreenActivity.mFontSize<4) {
            FullscreenActivity.mFontSize = 4.0f;
        } else if (FullscreenActivity.mFontSize>84) {
            FullscreenActivity.mFontSize = 84.0f;
        }

        int progressbar = (int) FullscreenActivity.mFontSize - 4;
        String text = FullscreenActivity.mFontSize + " sp";

        fontsize_seekbar.setProgress(progressbar);
        fontsize_TextView.setText(text);
        fontsize_TextView.setTextSize(FullscreenActivity.mFontSize);
    }

    public void updatefontsize() {
        FullscreenActivity.mFontSize = fontsize_seekbar.getProgress() + 4.0f;
        String text = (int) FullscreenActivity.mFontSize + " sp";
        fontsize_TextView.setText(text);
        fontsize_TextView.setTextSize(FullscreenActivity.mFontSize);
        Preferences.savePreferences();
    }

    public void setupmaxfontsizeseekbar() {
        // Seekbar size is 60 wide
        // Subtract 20 from all values - min is then 0, max is then 60
        // Actual mMaxFontSize is between 20 and 80 though
        if (FullscreenActivity.mMaxFontSize<20) {
            FullscreenActivity.mMaxFontSize = 20;
        } else if (FullscreenActivity.mMaxFontSize>80) {
            FullscreenActivity.mMaxFontSize = 80;
        }

        int progressbar = FullscreenActivity.mMaxFontSize - 20;
        String text = FullscreenActivity.mMaxFontSize + " sp";

        maxAutoScale_seekBar.setProgress(progressbar);
        maxAutoScale_TextView.setText(text);
        maxAutoScale_TextView.setTextSize(FullscreenActivity.mMaxFontSize);
    }

    public void setupminfontsizeseekbar() {
        // Seekbar size is 28 wide
        // Subtract 2 from all values - min is then 0, max is then 28
        // Actual mMinFontSize is between 2 and 30 though
        if (FullscreenActivity.mMinFontSize<2) {
            FullscreenActivity.mMinFontSize = 2;
        } else if (FullscreenActivity.mMinFontSize>32) {
            FullscreenActivity.mMaxFontSize = 32;
        }

        // Check the min is below the max!
        if (FullscreenActivity.mMinFontSize>FullscreenActivity.mMaxFontSize) {
            FullscreenActivity.mMinFontSize -= 1;
        }

        int progressbar = FullscreenActivity.mMinFontSize - 2;
        String text = FullscreenActivity.mMinFontSize + " sp";

        minAutoScale_seekBar.setProgress(progressbar);
        minAutoScale_TextView.setText(text);
        minAutoScale_TextView.setTextSize(FullscreenActivity.mMinFontSize);
    }

    public void updatemaxfontsize() {
        FullscreenActivity.mMaxFontSize = maxAutoScale_seekBar.getProgress() + 20;
        String text = FullscreenActivity.mMaxFontSize + " sp";
        maxAutoScale_TextView.setText(text);
        maxAutoScale_TextView.setTextSize(FullscreenActivity.mMaxFontSize);
        // Check the min is below the max!
        if (FullscreenActivity.mMinFontSize>=FullscreenActivity.mMaxFontSize) {
            FullscreenActivity.mMinFontSize = FullscreenActivity.mMaxFontSize - 1;
            setupminfontsizeseekbar();
        }
        Preferences.savePreferences();
    }

    public void updateminfontsize() {
        FullscreenActivity.mMinFontSize = (minAutoScale_seekBar.getProgress() + 2);
        String text = FullscreenActivity.mMinFontSize + " sp";
        minAutoScale_TextView.setText(text);
        minAutoScale_TextView.setTextSize(FullscreenActivity.mMinFontSize);
        // Check the min is below the max!
        if (FullscreenActivity.mMinFontSize>=FullscreenActivity.mMaxFontSize) {
            FullscreenActivity.mMaxFontSize = FullscreenActivity.mMinFontSize + 1;
            setupmaxfontsizeseekbar();
        }
        Preferences.savePreferences();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.dismiss();
    }

}