package com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Action;
import com.liskovsoft.smartyoutubetv2.tv.R;

public class MoreOptionsAction extends Action {
    public MoreOptionsAction(Context context) {
        super(R.id.action_more_options);
        setIcon(ContextCompat.getDrawable(context, R.drawable.yt_ic_player_more));
        setLabel1("More options");
    }
}
