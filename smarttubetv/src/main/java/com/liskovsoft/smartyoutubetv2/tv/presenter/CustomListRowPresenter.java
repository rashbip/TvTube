package com.liskovsoft.smartyoutubetv2.tv.presenter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ListRowView;
import androidx.leanback.widget.RowPresenter;
import com.liskovsoft.smartyoutubetv2.common.prefs.MainUIData;
import com.liskovsoft.smartyoutubetv2.tv.R;
import com.liskovsoft.smartyoutubetv2.tv.ui.browse.video.GridFragmentHelper;
import com.liskovsoft.smartyoutubetv2.tv.util.ViewUtil;

public class CustomListRowPresenter extends ListRowPresenter {
    public CustomListRowPresenter() {
        super(ViewUtil.FOCUS_ZOOM_FACTOR, ViewUtil.FOCUS_DIMMER_ENABLED);
        setSelectEffectEnabled(ViewUtil.ROW_SELECT_EFFECT_ENABLED);
    }

    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        RowPresenter.ViewHolder holder = super.createRowViewHolder(parent);
        if (holder instanceof ListRowPresenter.ViewHolder) {
            HorizontalGridView gridView = ((ListRowPresenter.ViewHolder) holder).getGridView();
            int balancedPadding = calculateBalancedSidePadding(parent.getContext());
            gridView.setPadding(balancedPadding, gridView.getPaddingTop(), balancedPadding, gridView.getPaddingBottom());
        }
        return holder;
    }

    private int calculateBalancedSidePadding(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = Math.max(dm.widthPixels, dm.heightPixels);
        int defaultPadding = context.getResources().getDimensionPixelSize(R.dimen.lb_browse_padding_start);
        int cardWidth = GridFragmentHelper.getCardDimensPx(context, R.dimen.card_width, R.dimen.card_height, MainUIData.instance(context).getVideoGridScale()).first;
        int spacing = context.getResources().getDimensionPixelSize(R.dimen.grid_item_horizontal_spacing);

        int numCards = (screenWidth + spacing) / (cardWidth + spacing);
        if (numCards > 0) {
            int totalCardsWidth = numCards * cardWidth + (numCards - 1) * spacing;
            int remaining = screenWidth - totalCardsWidth;
            if (remaining > 0) {
                return Math.max(defaultPadding, remaining / 2);
            }
        }
        return defaultPadding;
    }
}
