package com.core.frame;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.core.frame.tabcontrol.AdvancedPagerSlidingTabStrip;
import com.core.frame.tabcontrol.TabScrollViewPager;
import com.core.frame.widget.CommonTitleBar;


public class IconTabActivity extends BaseCoreActivity implements ViewPager.OnPageChangeListener{

    public AdvancedPagerSlidingTabStrip mAPSTS;

    public TabScrollViewPager mVP;

    private static final int VIEW_FIRST = 0;

    private static final int VIEW_SECOND = 1;

    private static final int VIEW_THIRD = 2;

    private static final int VIEW_FOURTH = 3;

    private static final int VIEW_SIZE = 4;

    private FirstFragment mFirstFragment = null;

    private SecondFragment mSecondFragment = null;

    private ThirdFragment mThirdFragment = null;

    private FourthFragment mFourthFragment = null;

    private boolean isPageScrolled=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tab);
        setTitleName("首页");
        findViews();
        init();
    }

    private void findViews(){
        mAPSTS = (AdvancedPagerSlidingTabStrip)findViewById(R.id.tabs);
        mVP = (TabScrollViewPager)findViewById(R.id.vp_main);
    }

    private void init(){
        mVP.setOffscreenPageLimit(VIEW_SIZE);
        mVP.setCanScroll(false);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        mVP.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        adapter.notifyDataSetChanged();
        mAPSTS.setViewPager(mVP);
        mAPSTS.setOnPageChangeListener(this);
        mVP.setCurrentItem(VIEW_FIRST);
        mAPSTS.showDot(VIEW_FOURTH,"99+");
        mAPSTS.showDot(VIEW_THIRD);
        titleBar.setLeftImageResource(R.mipmap.button_search);

        titleBar.addAction(new CommonTitleBar.ImageAction(R.mipmap.button_posts) {
            @Override
            public void performAction(View view) {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == VIEW_FIRST) {
            titleBar.setLeftImageResource(R.mipmap.button_search);
            titleBar.removeAllActions();
            titleBar.addAction(new CommonTitleBar.ImageAction(R.mipmap.button_posts) {
                @Override
                public void performAction(View view) {

                }
            });
            titleBar.setTitle("首页");
        } else if (position == VIEW_SECOND) {
            titleBar.setLeftImageResource(0);
            titleBar.removeAllActions();
            titleBar.setActionTextColor(Color.WHITE);
            titleBar.addAction(new CommonTitleBar.TextAction("发布消息") {
                @Override
                public void performAction(View view) {

                }
            });
            titleBar.setTitle("消息");
        } else if (position == VIEW_THIRD) {
            titleBar.setLeftImageResource(0);
            titleBar.removeAllActions();
            titleBar.setTitle("孕育");
        } else if (position == VIEW_FOURTH) {
            titleBar.setLeftImageResource(0);
            titleBar.removeAllActions();
            titleBar.setTitle("我的");
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class FragmentAdapter extends FragmentPagerAdapter implements AdvancedPagerSlidingTabStrip.IconTabProvider{

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                if (position == VIEW_FIRST) {
                    if (null == mFirstFragment){
                        mFirstFragment = FirstFragment.instance();
                    }
                    return mFirstFragment;
                } else if (position == VIEW_SECOND) {
                    if (null == mSecondFragment){
                        mSecondFragment = SecondFragment.instance();
                    }
                    return mSecondFragment;
                } else if (position == VIEW_THIRD) {
                    if (null == mThirdFragment){
                        mThirdFragment = ThirdFragment.instance();
                    }
                    return mThirdFragment;
                } else if (position == VIEW_FOURTH) {
                    if (null == mFourthFragment){
                        mFourthFragment = FourthFragment.instance();
                    }
                    return mFourthFragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position >= 0 && position < VIEW_SIZE){
                switch (position){
                    case  VIEW_FIRST:
                        return  "首页";
                    case  VIEW_SECOND:
                        return  "消息";
                    case  VIEW_THIRD:
                        return  "孕育";
                    case  VIEW_FOURTH:
                        return  "我的";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public Integer getPageIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.home_main_icon_n;
                    case VIEW_SECOND:
                        return  R.mipmap.home_categry_icon_n;
                    case VIEW_THIRD:
                        return  R.mipmap.home_live_icon_n;
                    case VIEW_FOURTH:
                        return  R.mipmap.home_mine_icon_n;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Integer getPageSelectIcon(int index) {
            if(index >= 0 && index < VIEW_SIZE){
                switch (index){
                    case  VIEW_FIRST:
                        return  R.mipmap.home_main_icon_f_n;
                    case VIEW_SECOND:
                        return  R.mipmap.home_categry_icon_f_n;
                    case VIEW_THIRD:
                        return  R.mipmap.home_live_icon_f_n;
                    case VIEW_FOURTH:
                        return  R.mipmap.home_mine_icon_f_n;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Rect getPageIconBounds(int position) {
            return null;
        }
    }

}
