package com.core.frame.tabcontrol;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可控制滑动的ViewPager
 */
public class TabScrollViewPager extends ViewPager {

    /**
     * 是否可以左右滑动,默认不可以左右滑动
     */
    private boolean isCanScroll = false;

    public TabScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabScrollViewPager(Context context) {
        this(context, null);
    }

    /**
     * 设置是否可以左右滑动
     * @param canScroll
     */
    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }
}