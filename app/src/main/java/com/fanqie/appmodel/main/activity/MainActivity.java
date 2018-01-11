package com.fanqie.appmodel.main.activity;

import android.content.Intent;
import android.view.KeyEvent;

import com.fanqie.appmodel.R;
import com.fanqie.appmodel.common.base.BaseActivity;
import com.fanqie.appmodel.common.customview.BottomBar;
import com.fanqie.appmodel.common.customview.NoScrollViewPager;
import com.fanqie.appmodel.common.utils.ToastUtils;
import com.fanqie.appmodel.main.adapter.MainPagerAdapter;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_bottombar)
    BottomBar mainBottombar;
    @BindView(R.id.vp_container)
    NoScrollViewPager vpContainer;

    private int currentBar = 0;
    private long exitTime = 0;
    private MainPagerAdapter mainPagerAdapter;

    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public int setBackButton() {
        return 0;
    }

    @Override
    public void initView() {
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        vpContainer.setAdapter(mainPagerAdapter);
//        vpContainer.setOffscreenPageLimit(4);  // 预加载 4 页
    }

    @Override
    public void registerPresenter() {

    }

    @Override
    public void unRegisterPresenter() {

    }


    @Override
    public void iniIntent(Intent it) {

    }

    @Override
    public void iniData() {

        //初始化第一页
        mainBottombar.setFirstButton(0);

        mainBottombar.setOnBottomButtonClickListener(new BottomBar.OnBottomButtonClickListener() {
            @Override
            public void firstClick() {
                if (currentBar != 1) {
                    vpContainer.setCurrentItem(0);
                    currentBar = 1;
                }
            }

            @Override
            public void secondClick() {
                if (currentBar != 2) {
                    vpContainer.setCurrentItem(1);
                    currentBar = 2;
                }
            }

            @Override
            public void thirdClick() {
                if (currentBar != 3) {
                    vpContainer.setCurrentItem(2);
                    currentBar = 3;
                }
            }

            @Override
            public void fourClick() {
                if (currentBar != 4) {
                    vpContainer.setCurrentItem(3);
                    currentBar = 4;
                }
            }

            @Override
            public void fifthClick() {
                if (currentBar != 5) {
                    vpContainer.setCurrentItem(4);
                    currentBar = 5;
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showMessage("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
