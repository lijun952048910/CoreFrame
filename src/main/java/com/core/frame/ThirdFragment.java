package com.core.frame;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.popup.BubblePopup;

public class ThirdFragment  extends Fragment {
    TextView tv_1;
    Activity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }
    public static ThirdFragment instance() {
        ThirdFragment view = new ThirdFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, null);
        tv_1= (TextView) view.findViewById(R.id.tv_1);
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inflate = View.inflate(mActivity, R.layout.popup_bubble_text, null);
                new BubblePopup(mActivity, inflate)
                        .anchorView(tv_1)
                        .bubbleColor(0xfffc6496)
                        .showAnim( new SlideBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .show();
            }
        });

        return view;
    }
}