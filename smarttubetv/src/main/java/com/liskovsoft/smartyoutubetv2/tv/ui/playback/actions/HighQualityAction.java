package com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Action;
import com.liskovsoft.smartyoutubetv2.tv.R;

public class HighQualityAction extends Action {
    public HighQualityAction(Context context) {
        super(R.id.lb_control_high_quality);
        BitmapDrawable uncoloredDrawable = ActionHelpers.toBitmapDrawable(
                context, ContextCompat.getDrawable(context, R.drawable.yt_ic_player_settings));

        setIcon(uncoloredDrawable);
        setLabel1(context.getString(R.string.playback_settings));
    }
}