package com.liskovsoft.smartyoutubetv2.tv.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.smartyoutubetv2.common.app.models.data.Video;
import com.liskovsoft.smartyoutubetv2.common.prefs.MainUIData;
import com.liskovsoft.smartyoutubetv2.tv.R;
import com.liskovsoft.smartyoutubetv2.tv.presenter.base.LongClickPresenter;
import com.liskovsoft.smartyoutubetv2.tv.ui.browse.video.GridFragmentHelper;
import com.liskovsoft.smartyoutubetv2.tv.util.ViewUtil;

public class ChannelCardPresenter extends LongClickPresenter {
    private static final String TAG = VideoCardPresenter.class.getSimpleName();
    private int mWidth;
    private int mHeight;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Context context = parent.getContext();

        updateDimensions(context);

        @SuppressLint("InflateParams")
        View container = LayoutInflater.from(context).inflate(R.layout.channel_card, null);
        container.setBackground(null);

        TextView textView = container.findViewById(R.id.channel_title);
        textView.setTextColor(Color.WHITE);

        boolean autoScrollEnabled = isCardTextAutoScrollEnabled(context);
        if (autoScrollEnabled) {
            ViewUtil.setTextScrollSpeed(textView, getCardTextScrollSpeed(context));
        }

        container.setOnFocusChangeListener((v, hasFocus) -> {
            applyState(container, textView, hasFocus);

            if (!autoScrollEnabled) {
                return;
            }

            if (hasFocus) {
                ViewUtil.enableMarquee(textView);
            } else {
                ViewUtil.disableMarquee(textView);
            }
        });

        return new ViewHolder(container);
    }

    private void applyState(View container, TextView textView, boolean focusedOrSelected) {
        boolean active = focusedOrSelected || container.isSelected() || container.hasFocus();
        textView.setSelected(active);
        textView.setTextColor(active ? Color.parseColor("#0F0F0F") : Color.WHITE);
        textView.setBackgroundResource(active ? R.drawable.channel_card_pill_bg : 0);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        super.onBindViewHolder(viewHolder, item);

        Context context = viewHolder.view.getContext();
        Video video = (Video) item;

        ViewUtil.setDimensions(viewHolder.view.findViewById(R.id.channel_card_wrapper), mWidth, -1);

        TextView textView = viewHolder.view.findViewById(R.id.channel_title);
        textView.setText(video.getTitle());
        applyState(viewHolder.view, textView, viewHolder.view.hasFocus());

        ImageView imageView = viewHolder.view.findViewById(R.id.channel_image);
        imageView.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(video.cardImageUrl)
                .apply(ViewUtil.glideOptions())
                .circleCrop()
                .listener(mErrorListener)
                .into(imageView);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageView imageView = viewHolder.view.findViewById(R.id.channel_image);
        imageView.setImageDrawable(null);
    }

    private void updateDimensions(Context context) {
        Pair<Integer, Integer> dimens = getCardDimensPx(context);

        mWidth = dimens.first;
        mHeight = dimens.second;
    }

    protected Pair<Integer, Integer> getCardDimensPx(Context context) {
        return GridFragmentHelper.getCardDimensPx(
                context, R.dimen.channel_card_width,
                R.dimen.channel_card_height,
                MainUIData.instance(context).getVideoGridScale(),
                true);
    }

    protected boolean isCardTextAutoScrollEnabled(Context context) {
        return MainUIData.instance(context).isCardTextAutoScrollEnabled();
    }

    protected float getCardTextScrollSpeed(Context context) {
        return MainUIData.instance(context).getCardTextScrollSpeed();
    }

    private final RequestListener<Drawable> mErrorListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            Log.e(TAG, "Glide load failed: " + e);
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    };
}
