package com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions;

import android.content.Context;
import androidx.leanback.widget.Action;
import com.liskovsoft.smartyoutubetv2.tv.R;

public class VideoInfoAction extends Action {
    public VideoInfoAction(Context context) {
        super(R.id.action_info);
        setIcon(null);
        setLabel1("Description");
    }
}