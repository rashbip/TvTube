package com.liskovsoft.smartyoutubetv2.tv.ui.mod.leanback.playerglue.tooltips;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.leanback.R;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions.PaddingAction;

/**
 * Displays primary and secondary controls for a {@link PlaybackControlsRow}.
 *
 * Binds to items of type {@link Action}.
 */
public class ControlButtonPresenterSelector extends PresenterSelector {
    private final ControlButtonPresenter mPrimaryPresenter =
            new ControlButtonPresenter(R.layout.lb_control_button_primary);
    private final ControlButtonPresenter mSecondaryPresenter =
            new ControlButtonPresenter(R.layout.lb_control_button_primary);
    private final Presenter[] mPresenters = new Presenter[]{mPrimaryPresenter};

    public ControlButtonPresenterSelector(boolean tooltipsEnabled) {
        mPrimaryPresenter.tooltipsEnabled = mSecondaryPresenter.tooltipsEnabled = tooltipsEnabled;
    }

    public Presenter getPrimaryPresenter() {
        return mPrimaryPresenter;
    }

    public Presenter getSecondaryPresenter() {
        return mSecondaryPresenter;
    }

    @Override
    public Presenter getPresenter(Object item) {
        return mPrimaryPresenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return mPresenters;
    }

    static class ActionViewHolder extends Presenter.ViewHolder {
        ImageView mIcon;
        TextView mLabel;
        View mFocusableView;

        public ActionViewHolder(View view) {
            super(view);
            mIcon = (ImageView) view.findViewById(R.id.icon);
            mLabel = (TextView) view.findViewById(R.id.label);
            mFocusableView = view.findViewById(R.id.button);
        }
    }

    public static class ControlButtonPresenter extends Presenter {
        private int mLayoutResourceId;
        private boolean tooltipsEnabled;

        ControlButtonPresenter(int layoutResourceId) {
            mLayoutResourceId = layoutResourceId;
        }

        private static int dp(Context ctx, float value) {
            return Math.round(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, value, ctx.getResources().getDisplayMetrics()));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(mLayoutResourceId, parent, false);
            return new ActionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            final Action action = (Action) item;
            final ActionViewHolder vh = (ActionViewHolder) viewHolder;
            final Context ctx = vh.view.getContext();
            final long actionId = action.getId();

            int heightPx = dp(ctx, 42);
            int widthPx = dp(ctx, 42);
            int marginEndPx = 0;
            int iconSizePx = dp(ctx, 21);
            int bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_circle_bg;
            boolean isTextPill = (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_info);
            final boolean isChannelAvatar = (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_channel)
                    && (action instanceof PaddingAction) && ((PaddingAction) action).getPadding() > 0;

            if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_channel) {
                heightPx = dp(ctx, 46);
                widthPx = dp(ctx, 46);
                marginEndPx = dp(ctx, 7);
                iconSizePx = isChannelAvatar ? dp(ctx, 32) : dp(ctx, 22);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_circle_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_info) {
                widthPx = ViewGroup.LayoutParams.WRAP_CONTENT;
                marginEndPx = dp(ctx, 7);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_pill_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_subscribe) {
                widthPx = dp(ctx, 42);
                marginEndPx = dp(ctx, 7);
                iconSizePx = dp(ctx, 22);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_circle_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_video_speed) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 21);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_circle_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.lb_control_skip_previous) {
                widthPx = dp(ctx, 42);
                marginEndPx = dp(ctx, 7);
                iconSizePx = dp(ctx, 22);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_circle_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.lb_control_play_pause) {
                heightPx = dp(ctx, 50);
                widthPx = dp(ctx, 50);
                marginEndPx = dp(ctx, 7);
                iconSizePx = dp(ctx, 26);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_circle_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.lb_control_skip_next) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 22);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_btn_circle_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_thumbs_up) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 20);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_group_left_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_thumbs_down) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 20);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_group_mid_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_chat) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 20);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_group_mid_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_playlist_add) {
                widthPx = dp(ctx, 42);
                marginEndPx = dp(ctx, 7);
                iconSizePx = dp(ctx, 20);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_group_right_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.lb_control_closed_captioning) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 21);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_group_left_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.lb_control_high_quality) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 21);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_group_mid_bg;
            } else if (actionId == com.liskovsoft.smartyoutubetv2.tv.R.id.action_more_options) {
                widthPx = dp(ctx, 42);
                marginEndPx = 0;
                iconSizePx = dp(ctx, 21);
                bgResId = com.liskovsoft.smartyoutubetv2.tv.R.drawable.yt_player_group_right_bg;
            }

            // Outer FrameLayout params inside ControlBar (LinearLayout)
            LinearLayout.LayoutParams rootLp = new LinearLayout.LayoutParams(widthPx, heightPx);
            rootLp.gravity = Gravity.CENTER_VERTICAL;
            rootLp.setMarginEnd(marginEndPx);
            rootLp.rightMargin = marginEndPx;
            rootLp.setMarginStart(0);
            rootLp.leftMargin = 0;
            vh.view.setLayoutParams(rootLp);

            // Focusable background view (@id/button is an ImageView in lb_control_button_primary)
            if (vh.mFocusableView instanceof ImageView) {
                ((ImageView) vh.mFocusableView).setImageDrawable(null);
            }
            vh.mFocusableView.setBackgroundResource(bgResId);
            FrameLayout.LayoutParams btnLp = new FrameLayout.LayoutParams(
                    isTextPill ? ViewGroup.LayoutParams.MATCH_PARENT : widthPx,
                    heightPx,
                    Gravity.CENTER);
            vh.mFocusableView.setLayoutParams(btnLp);

            if (isTextPill) {
                vh.mIcon.setImageDrawable(null);
                vh.mIcon.setVisibility(View.GONE);
                if (vh.mLabel != null) {
                    vh.mLabel.setVisibility(View.VISIBLE);
                    vh.mLabel.setText(action.getLabel1());
                    vh.mLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    vh.mLabel.setTypeface(vh.mLabel.getTypeface(), android.graphics.Typeface.BOLD);
                    int padH = dp(ctx, 15);
                    vh.mLabel.setPadding(padH, 0, padH, 0);
                    FrameLayout.LayoutParams labelLp = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            heightPx,
                            Gravity.CENTER);
                    vh.mLabel.setLayoutParams(labelLp);
                    vh.mLabel.setGravity(Gravity.CENTER);
                    // Ensure mFocusableView matches label width
                    int wSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    int hSpec = View.MeasureSpec.makeMeasureSpec(heightPx, View.MeasureSpec.EXACTLY);
                    vh.mLabel.measure(wSpec, hSpec);
                    int measuredW = vh.mLabel.getMeasuredWidth();
                    rootLp.width = measuredW;
                    vh.view.setLayoutParams(rootLp);
                    btnLp.width = measuredW;
                    vh.mFocusableView.setLayoutParams(btnLp);
                }
            } else {
                vh.mIcon.setVisibility(View.VISIBLE);
                vh.mIcon.setImageDrawable(action.getIcon());
                vh.mIcon.setPadding(0, 0, 0, 0);
                FrameLayout.LayoutParams iconLp = new FrameLayout.LayoutParams(iconSizePx, iconSizePx, Gravity.CENTER);
                vh.mIcon.setLayoutParams(iconLp);
                if (vh.mLabel != null) {
                    vh.mLabel.setText(null);
                    vh.mLabel.setVisibility(View.GONE);
                }
            }

            applyFocusColors(vh, vh.mFocusableView.hasFocus(), isChannelAvatar);
            vh.mFocusableView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    applyFocusColors(vh, hasFocus, isChannelAvatar);
                }
            });

            CharSequence contentDescription = TextUtils.isEmpty(action.getLabel2())
                    ? action.getLabel1() : action.getLabel2();
            if (!TextUtils.equals(vh.mFocusableView.getContentDescription(), contentDescription)) {
                vh.mFocusableView.setContentDescription(contentDescription);
                vh.mFocusableView.sendAccessibilityEvent(
                        AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
            }
        }

        private void applyFocusColors(ActionViewHolder vh, boolean hasFocus, boolean isChannelAvatar) {
            if (vh.mLabel != null && vh.mLabel.getVisibility() == View.VISIBLE) {
                vh.mLabel.setTextColor(hasFocus ? Color.parseColor("#0F0F0F") : Color.WHITE);
            }
            if (vh.mIcon != null && vh.mIcon.getVisibility() == View.VISIBLE) {
                if (hasFocus && !isChannelAvatar) {
                    vh.mIcon.setColorFilter(Color.parseColor("#0F0F0F"), PorterDuff.Mode.SRC_IN);
                } else {
                    vh.mIcon.clearColorFilter();
                }
            }
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
            ActionViewHolder vh = (ActionViewHolder) viewHolder;
            vh.mIcon.setImageDrawable(null);
            vh.mIcon.clearColorFilter();
            if (vh.mLabel != null) {
                vh.mLabel.setText(null);
            }
            vh.mFocusableView.setOnFocusChangeListener(null);
            vh.mFocusableView.setContentDescription(null);
        }

        @Override
        public void setOnClickListener(ViewHolder viewHolder,
                                       View.OnClickListener listener) {
            ((ActionViewHolder) viewHolder).mFocusableView.setOnClickListener(listener);
        }

        public void setOnLongClickListener(ViewHolder viewHolder,
                                       View.OnLongClickListener listener) {
            ((ActionViewHolder) viewHolder).mFocusableView.setOnLongClickListener(listener);
        }
    }
}
