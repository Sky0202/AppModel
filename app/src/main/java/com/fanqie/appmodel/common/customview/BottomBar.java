package com.fanqie.appmodel.common.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanqie.appmodel.R;


/**
 * 主页bottombar
 * 1.使用时直接修改对应的图标 修改主题颜色
 * 2.xml内容 不需要的bar注释掉 修改对应的图标
 * <p>
 * Created by Administrator on 2016/7/28.
 */
public class BottomBar extends RelativeLayout {

    private LinearLayout bottombar_ll_first;
    private ImageView bottombar_iv_first;
    private TextView bottombar_tv_first;
    private LinearLayout bottombar_ll_second;
    private ImageView bottombar_iv_second;
    private TextView bottombar_tv_second;
    private LinearLayout bottombar_ll_third;
    private ImageView bottombar_iv_third;
    private TextView bottombar_tv_third;
    private LinearLayout bottombar_ll_fourth;
    private ImageView bottombar_iv_fourth;
    private TextView bottombar_tv_fourth;
    private LinearLayout bottombar_ll_fifth;
    private ImageView bottombar_iv_fifth;
    private TextView bottombar_tv_fifth;

    //文字切换颜色
    private int colorDisplay = getResources().getColor(R.color.colorPrimary);
    private int color = Color.GRAY;

    OnBottomButtonClickListener onBottomButtonClickListener;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.customview_bottombar, this, true);
        iniView();
        iniClick();

    }

    /**
     * 初始化所有控件
     */
    public void iniView() {

        bottombar_ll_first = (LinearLayout) findViewById(R.id.bottombar_ll_first);
        bottombar_iv_first = (ImageView) findViewById(R.id.bottombar_iv_first);
        bottombar_tv_first = (TextView) findViewById(R.id.bottombar_tv_first);
        bottombar_ll_second = (LinearLayout) findViewById(R.id.bottombar_ll_second);
        bottombar_iv_second = (ImageView) findViewById(R.id.bottombar_iv_second);
        bottombar_tv_second = (TextView) findViewById(R.id.bottombar_tv_second);
        bottombar_ll_third = (LinearLayout) findViewById(R.id.bottombar_ll_third);
        bottombar_iv_third = (ImageView) findViewById(R.id.bottombar_iv_third);
        bottombar_tv_third = (TextView) findViewById(R.id.bottombar_tv_third);
        bottombar_ll_fourth = (LinearLayout) findViewById(R.id.bottombar_ll_fourth);
        bottombar_iv_fourth = (ImageView) findViewById(R.id.bottombar_iv_fourth);
        bottombar_tv_fourth = (TextView) findViewById(R.id.bottombar_tv_fourth);
        bottombar_ll_fifth = (LinearLayout) findViewById(R.id.bottombar_ll_fifth);
        bottombar_iv_fifth = (ImageView) findViewById(R.id.bottombar_iv_fifth);
        bottombar_tv_fifth = (TextView) findViewById(R.id.bottombar_tv_fifth);

    }


    /**
     * 按钮的点击事件
     */
    public void iniClick() {

        bottombar_ll_first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFirst();
                clickAnimation(bottombar_ll_first);
                onBottomButtonClickListener.firstClick();
            }
        });

        bottombar_ll_second.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                showSecond();
                clickAnimation(bottombar_ll_second);
                onBottomButtonClickListener.secondClick();

            }
        });

        bottombar_ll_third.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showThird();
                clickAnimation(bottombar_ll_third);
                onBottomButtonClickListener.thirdClick();
            }
        });

        bottombar_ll_fourth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFourth();
                clickAnimation(bottombar_ll_fourth);
                onBottomButtonClickListener.fourClick();

            }
        });

        bottombar_ll_fifth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFifth();
                clickAnimation(bottombar_ll_fifth);
                onBottomButtonClickListener.fifthClick();

            }
        });

    }

    private static void clickAnimation(LinearLayout bottombar) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(bottombar, "scaleX", 0.9f, 1f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(bottombar, "scaleY", 0.9f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    /**
     * 根据点击事件显示不同的颜色
     */
    public void showFirst() {

        setFirstColorSelected();
        setSecondColorNormal();
        setThirdColorNormal();
        setFourthColorNormal();
        setFifthColorNormal();

    }

    public void showSecond() {

        setFirstColorNormal();
        setSecondColorSelected();
        setThirdColorNormal();
        setFourthColorNormal();
        setFifthColorNormal();

    }

    public void showThird() {

        setFirstColorNormal();
        setSecondColorNormal();
        setThirdColorSelected();
        setFourthColorNormal();
        setFifthColorNormal();

    }

    public void showFourth() {

        setFirstColorNormal();
        setSecondColorNormal();
        setThirdColorNormal();
        setFourthColorSelected();
        setFifthColorNormal();

    }

    public void showFifth() {

        setFirstColorNormal();
        setSecondColorNormal();
        setThirdColorNormal();
        setFourthColorNormal();
        setFifthColorSelected();

    }

    // 第一个按钮 点击
    public void setFirstColorSelected() {
        bottombar_iv_first.setImageDrawable(getResources().getDrawable(R.drawable.bot1_on));
        bottombar_tv_first.setTextColor(colorDisplay);
    }

    // 第一个按钮 默认
    public void setFirstColorNormal() {
        bottombar_iv_first.setImageDrawable(getResources().getDrawable(R.drawable.bot1));
        bottombar_tv_first.setTextColor(color);
    }

    // 第二个按钮 点击
    public void setSecondColorSelected() {
        bottombar_iv_second.setImageDrawable(getResources().getDrawable(R.drawable.bot2_on));
        bottombar_tv_second.setTextColor(colorDisplay);
    }

    // 第二个按钮 默认
    public void setSecondColorNormal() {
        bottombar_iv_second.setImageDrawable(getResources().getDrawable(R.drawable.bot2));
        bottombar_tv_second.setTextColor(color);
    }

    // 第三个按钮 点击
    public void setThirdColorSelected() {
        bottombar_iv_third.setImageDrawable(getResources().getDrawable(R.drawable.bot3_on));
        bottombar_tv_third.setTextColor(colorDisplay);
    }

    // 第三个按钮 默认
    public void setThirdColorNormal() {
        bottombar_iv_third.setImageDrawable(getResources().getDrawable(R.drawable.bot3));
        bottombar_tv_third.setTextColor(color);
    }

    // 第四个按钮 点击
    public void setFourthColorSelected() {
        bottombar_iv_fourth.setImageDrawable(getResources().getDrawable(R.drawable.bot4_on));
        bottombar_tv_fourth.setTextColor(colorDisplay);
    }

    // 第四个按钮 默认
    public void setFourthColorNormal() {
        bottombar_iv_fourth.setImageDrawable(getResources().getDrawable(R.drawable.bot4));
        bottombar_tv_fourth.setTextColor(color);
    }

    // 第五个按钮 点击
    public void setFifthColorSelected() {
        bottombar_iv_fifth.setImageDrawable(getResources().getDrawable(R.drawable.bot4_on));
        bottombar_tv_fifth.setTextColor(colorDisplay);
    }

    // 第五个按钮 默认
    public void setFifthColorNormal() {
        bottombar_iv_fifth.setImageDrawable(getResources().getDrawable(R.drawable.bot4));
        bottombar_tv_fifth.setTextColor(color);
    }


    /**
     * 设置初始化页面
     */
    public void setFirstButton(int iniPage) {
        switch (iniPage) {
            case 0:
                showFirst();
                break;
            case 1:
                showSecond();
                break;
            case 2:
                showThird();
                break;
            case 3:
                showFourth();
                break;
            case 4:
                showFifth();
                break;
        }
    }

    /**
     * 按钮点击接口
     */
    public interface OnBottomButtonClickListener {

        void firstClick();

        void secondClick();

        void thirdClick();

        void fourClick();

        void fifthClick();

    }

    public void setOnBottomButtonClickListener(OnBottomButtonClickListener onBottomButtonClickListener) {
        this.onBottomButtonClickListener = onBottomButtonClickListener;
    }


}
