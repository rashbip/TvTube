package com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions;

import android.content.Context;
import com.liskovsoft.smartyoutubetv2.tv.R;

public class ThumbsUpAction extends TwoStateAction {
    public ThumbsUpAction(Context context) {
        super(context, R.id.action_thumbs_up, R.drawable.yt_ic_player_thumb_up, R.drawable.yt_ic_player_thumb_up_filled, false, true);

        String[] labels = new String[2];
        labels[INDEX_OFF] = context.getString(R.string.action_like);
        labels[INDEX_ON] = context.getString(R.string.action_like);
        setLabels(labels);
    }
}