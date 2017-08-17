package com.core.frame;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.core.frame.widget.CommonTitleBar;

/**
 * 基类Activity
 */
public abstract class BaseCoreActivity extends AppCompatActivity {

    public CommonTitleBar titleBar;

    private LinearLayout mContentContainer;

    private boolean isTitleImmerse = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.title_activity_base);
        initTitleBar();
    }
    /**
     * 初始化TitleBar
     */
    private void initTitleBar(){
      if (isTitleImmerse) {
          if (hasKitKat() && !hasLollipop()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (hasLollipop()) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
        
        mContentContainer =(LinearLayout) findViewById(R.id.content_frame_content);
        titleBar = (CommonTitleBar) findViewById(R.id.frame_title_header);
        titleBar.setImmersive(isTitleImmerse);
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setBackgroundColor(Color.parseColor("#fc6496"));
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setSubTitleColor(Color.WHITE);
        titleBar.setDividerColor(Color.parseColor("#D2D2D2"));
        titleBar.setActionTextColor(Color.WHITE);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContentContainer.addView(view);
    }

    /**
     * 设置activity标题
     * @param title
     */
    public void setTitleName(String title){
        if(titleBar!=null){
            titleBar.setTitle(title);
        }
    }
    /**
     * 设置是否是沉浸式titlebar
     * @param titleImmerse
     */
    public void setTitleImmerse(boolean titleImmerse) {
        isTitleImmerse = titleImmerse;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
