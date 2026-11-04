package com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions;

import android.content.Context;
import com.liskovsoft.smartyoutubetv2.tv.R;

public class ClosedCaptioningAction extends TwoStateAction {
    public ClosedCaptioningAction(Context context) {
        super(context, R.id.lb_control_closed_captioning, R.drawable.yt_ic_player_cc, R.drawable.yt_ic_player_cc_filled, true, true);

        String[] labels = new String[2];
        labels[INDEX_OFF] = context.getString(R.string.lb_playback_controls_closed_captioning_enable);
        labels[INDEX_ON] = context.getString(R.string.lb_playback_controls_closed_captioning_disable);
        setLabels(labels);
    }
}