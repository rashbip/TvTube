package com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions;

import android.content.Context;
import com.liskovsoft.smartyoutubetv2.tv.R;

public class ThumbsDownAction extends TwoStateAction {
    public ThumbsDownAction(Context context) {
        super(context, R.id.action_thumbs_down, R.drawable.yt_ic_player_thumb_down, R.drawable.yt_ic_player_thumb_down_filled, false, true);

        String[] labels = new String[2];
        labels[INDEX_OFF] = context.getString(R.string.action_dislike);
        labels[INDEX_ON] = context.getString(R.string.action_dislike);
        setLabels(labels);
    }
}