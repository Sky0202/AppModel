package com.fanqie.appmodel.common.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanqie.appmodel.R;


/**
 * 箭头文字
 */
public class ArrowText extends RelativeLayout {

    private String greenTitle;
    private String mainTitle;
    private TextView tv_green;
    private TextView tv_main;
    private ImageView tv_pic;

    public ArrowText(Context context) {
        this(context, null);
    }

    public ArrowText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.customview_arrortext, this, true);
        tv_main = (TextView) findViewById(R.id.custom_arrortextview_tv_mian);
        tv_main.setText(mainTitle + "");
        tv_green = (TextView) findViewById(R.id.custom_arrortextview_tv_green);
        tv_green.setText(greenTitle + "");
        tv_pic = (ImageView) findViewById(R.id.iv_pic_arrortext);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.arrorTextView, defStyleAttr, 0);
        int indexCount = a.getIndexCount();

        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            switch (index) {

                case R.styleable.arrorTextView_blackTitle:
                    String string = a.getString(index);
                    if (!string.isEmpty()) {
                        tv_main.setText(string);
                    }
                    break;

                case R.styleable.arrorTextView_grayTitle:
                    String string1 = a.getString(index);
                    if (!string1.isEmpty()) {
                        tv_green.setText(string1);
                    } else {
                        tv_green.setText("");
                    }
                    break;

                case R.styleable.arrorTextView_titleImage:
                    int resourceId = a.getResourceId(index, 0);

                    if (resourceId != 0) {
                        tv_pic.setVisibility(VISIBLE);
                        tv_pic.setBackgroundResource(resourceId);
                    } else {
                        tv_pic.setVisibility(GONE);
                    }
                    break;

                case R.styleable.arrorTextView_grayColor:
                    int color = a.getColor(index, 0);
                    tv_green.setTextColor(color);
                    break;

            }
        }
        a.recycle();

    }

    //对外方法
    public void setGreenTitle(String greenTitle) {
        tv_green.setText(greenTitle);
    }

    //对外方法
    public void setMainTitle(String mainTitle) {
        tv_main.setText(mainTitle);
    }

    /**
     * 获取绿色数据
     */
    public String getGreenTitle() {
        return tv_green.getText().toString();
    }


}
