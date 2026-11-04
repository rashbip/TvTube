/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.liskovsoft.smartyoutubetv2.tv.ui.mod.leanback.playerglue.tweaks;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.leanback.R;

import java.util.ArrayList;

class ControlBar extends LinearLayout {

    public interface OnChildFocusedListener {

        public void onChildFocusedListener(View child, View focused);
    }

    private int mChildMarginFromCenter;
    private OnChildFocusedListener mOnChildFocusedListener;
    // Can't set to static. Because we have two control bars.
    private int mLastFocusIndex = -1;
    // MOD: Sync focus index between rows
    private static int sSharedFocusIndex = -1;
    private boolean mIsSharedFocusEnabled = true;
    private boolean mDefaultFocusToMiddle = true;
    private boolean mFocusRecovery = true;

    public ControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);
        resetFocus(); // MOD: reset global focus index
    }

    public ControlBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGravity(Gravity.CENTER_VERTICAL);
        resetFocus(); // MOD: reset global focus index
    }

    void setDefaultFocusToMiddle(boolean defaultFocusToMiddle) {
        mDefaultFocusToMiddle = defaultFocusToMiddle;
    }

    /**
     * MOD: enable/disable focus restoration
     */
    void setFocusRecovery(boolean focusRecovery) {
        mFocusRecovery = focusRecovery;
    }

    /**
     * MOD: synced navigation between rows
     */
    void setSharedFocusEnabled(boolean enabled) {
        mIsSharedFocusEnabled = enabled;
    }

    void resetFocus() {
        setFocusIndex(-1);
    }

    private boolean isPrimaryControlsBar() {
        View p = (View) getParent();
        return p != null && p.getId() == R.id.controls_dock;
    }

    int getDefaultFocusIndex() {
        // When primary bar has 7 children (Channel, Description, Bell, Speed, Prev, Play/Pause, Next),
        // default focus to Play/Pause at index 5.
        if (isPrimaryControlsBar() && getChildCount() == 7) {
            return 5;
        }
        return mDefaultFocusToMiddle ? getChildCount() / 2 : 0;
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (getChildCount() > 0) {
            // MOD: fix situation when rows have different length
            if (getFocusIndex() >= getChildCount()) {
                setFocusIndex(getChildCount() - 1);
            }

            int index = getFocusIndex() >= 0 && getFocusIndex() < getChildCount()
                    ? getFocusIndex() : getDefaultFocusIndex();

            if (getChildAt(index).requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if ((direction == ViewGroup.FOCUS_UP || direction == ViewGroup.FOCUS_DOWN)) {
            if (getFocusIndex() >= 0 && getFocusIndex() < getChildCount()) {
                views.add(getChildAt(getFocusIndex()));
            } else if (getChildCount() > 0) {
                views.add(getChildAt(getDefaultFocusIndex()));
            }
        } else {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public void setOnChildFocusedListener(OnChildFocusedListener listener) {
        mOnChildFocusedListener = listener;
    }

    public void setChildMarginFromCenter(int marginFromCenter) {
        mChildMarginFromCenter = marginFromCenter;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (mFocusRecovery) {
            setFocusIndex(indexOfChild(child));
        }
        if (mOnChildFocusedListener != null) {
            mOnChildFocusedListener.onChildFocusedListener(child, focused);
        }
    }

    private int dp(float value) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Center Play/Pause (index 5) ONLY in the primary control bar (controls_dock):
        // [0:Channel, 1:Description, 2:Bell, 3:Speed]  ...  [4:Prev, 5:Play/Pause, 6:Next]
        if (isPrimaryControlsBar() && getChildCount() == 7) {
            int rowWidth = 0;
            View p = (View) getParent();
            if (p != null && p.getParent() instanceof View) {
                View rowParent = (View) p.getParent();
                rowWidth = rowParent.getMeasuredWidth();
            }
            if (rowWidth <= 0) {
                rowWidth = getResources().getDisplayMetrics().widthPixels - dp(80);
            }

            int leftGroupWidth = 0;
            for (int i = 0; i < 4; i++) {
                View c = getChildAt(i);
                LayoutParams clp = (LayoutParams) c.getLayoutParams();
                leftGroupWidth += c.getMeasuredWidth() + clp.leftMargin + clp.rightMargin;
            }

            View prevView = getChildAt(4);
            LayoutParams prevLp = (LayoutParams) prevView.getLayoutParams();
            int prevSpan = prevView.getMeasuredWidth() + prevLp.rightMargin;

            View playView = getChildAt(5);
            int playWidth = playView.getMeasuredWidth();

            int desiredMarginStart = Math.max(dp(12), (rowWidth / 2) - (playWidth / 2) - prevSpan - leftGroupWidth);
            if (prevLp.leftMargin != desiredMarginStart) {
                prevLp.setMarginStart(desiredMarginStart);
                prevLp.leftMargin = desiredMarginStart;
                prevView.setLayoutParams(prevLp);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            return;
        }

        if (mChildMarginFromCenter <= 0) {
            return;
        }

        int totalExtraMargin = 0;
        for (int i = 0; i < getChildCount() - 1; i++) {
            View first = getChildAt(i);
            View second = getChildAt(i+1);
            int measuredWidth = first.getMeasuredWidth() + second.getMeasuredWidth();
            int marginStart = mChildMarginFromCenter - measuredWidth / 2;
            LayoutParams lp = (LayoutParams) second.getLayoutParams();
            int extraMargin = marginStart - lp.getMarginStart();
            lp.setMarginStart(marginStart);
            second.setLayoutParams(lp);
            totalExtraMargin += extraMargin;
        }
        setMeasuredDimension(getMeasuredWidth() + totalExtraMargin, getMeasuredHeight());
    }

    private void setFocusIndex(int index) {
        mLastFocusIndex = index;
        if (index != -1) // don't reset global focus between player launches
            sSharedFocusIndex = index;
    }

    private int getFocusIndex() {
        return mIsSharedFocusEnabled ? sSharedFocusIndex : mLastFocusIndex;
    }
}
