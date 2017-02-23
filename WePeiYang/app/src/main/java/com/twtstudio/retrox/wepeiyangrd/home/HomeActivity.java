package com.twtstudio.retrox.wepeiyangrd.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.View;

import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;
import com.twtstudio.retrox.wepeiyangrd.base.BaseFragment;
import com.twtstudio.retrox.wepeiyangrd.home.common.CommonFragment;
import com.twtstudio.retrox.wepeiyangrd.home.news.NewsFragment;
import com.twtstudio.retrox.wepeiyangrd.home.tools.ToolsFragment;
import com.twtstudio.retrox.wepeiyangrd.home.user.UserFragment;
import com.twtstudio.retrox.wepeiyangrd.view.BottomBar;
import com.twtstudio.retrox.wepeiyangrd.view.BottomBarTab;

/**
 * Created by retrox on 2016/12/12.
 * home activity ui
 * ONLY UI
 */

public class HomeActivity extends BaseActivity {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private BaseFragment[] mFragments = new BaseFragment[4];

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            setWindowTransition();
        }
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            mFragments[FIRST] = CommonFragment.newInstance();
            mFragments[SECOND] = NewsFragment.newInstance();
            mFragments[THIRD] = ToolsFragment.newInstance();
            mFragments[FOURTH] = UserFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {

            mFragments[FIRST] = findFragment(CommonFragment.class);
            mFragments[SECOND] = findFragment(NewsFragment.class);
            mFragments[THIRD] = findFragment(ToolsFragment.class);
            mFragments[FOURTH] = findFragment(UserFragment.class);

        }

        initView();
    }

    private void setWindowTransition(){
        getWindow().setEnterTransition(new Slide());
        getWindow().setReenterTransition(new Slide());

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    private void initView() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        //fake icons
        mBottomBar.addItem(new BottomBarTab(this, R.drawable.ic_common))
                .addItem(new BottomBarTab(this, R.drawable.ic_news))
                .addItem(new BottomBarTab(this, R.drawable.ic_tools))
                .addItem(new BottomBarTab(this, R.drawable.ic_user));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
}