package com.fanqie.appmodel.common.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanqie.appmodel.R;


/**
 * 箭头文字
 */
public class ArrowLayout extends RelativeLayout {

    private ImageView ivPicLeft;
    private TextView tv_arrow_title;
    private TextView tv_arrow_desc;
    private String title;
    private String desc;
    private int img;
    private int descColor;
    private View line;
    private boolean hasLine;

    public ArrowLayout(Context context) {
        this(context, null);
    }

    public ArrowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_arrow, this, true);
        view.setClickable(true);
        view.setFocusable(true);
        ivPicLeft = (ImageView) view.findViewById(R.id.iv_pic_left);
        tv_arrow_title = (TextView) view.findViewById(R.id.tv_arrow_title);
        tv_arrow_desc = (TextView) view.findViewById(R.id.tv_arrow_desc);
        line = view.findViewById(R.id.view_line);

        // 获取自定义属性的值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArrowLayout, defStyleAttr, 0);
        if (typedArray != null) {

            title = typedArray.getString(R.styleable.ArrowLayout_arrowTitle);
            desc = typedArray.getString(R.styleable.ArrowLayout_arrowDesc);
            img = typedArray.getResourceId(R.styleable.ArrowLayout_arrowTitleImg, 0);
            descColor = typedArray.getColor(R.styleable.ArrowLayout_arrowDescColor, getResources().getColor(R.color.color_gray));
            // 显示分割线
            hasLine = typedArray.getBoolean(R.styleable.ArrowLayout_arrowLine, true);
            typedArray.recycle();

        }

        tv_arrow_title.setText(title.isEmpty() ? "" : title);
        tv_arrow_desc.setText(desc == null ? "" : desc);
        tv_arrow_desc.setTextColor(descColor);
        line.setVisibility(hasLine ? VISIBLE : GONE);
        if (img != 0) {
            ivPicLeft.setVisibility(VISIBLE);
            ivPicLeft.setBackgroundResource(img);
        } else {
            ivPicLeft.setVisibility(GONE);
        }

    }

    /**
     * 创建时间：2017/11/6 17:35  描述：设置预描述
     */
    public void setArrowDesc(String arrowDesc) {
        tv_arrow_desc.setText(arrowDesc);
    }

    /**
     * 创建时间：2017/11/6 17:35  描述：设置左边文字
     */
    public void setArrowTitle(String arrowTitle) {
        tv_arrow_title.setText(arrowTitle);
    }

    /**
     * 创建时间：2017/11/6 17:36  描述：获取选择的数据
     */
    public String getArrowDesc() {
        return tv_arrow_desc.getText().toString();
    }

}
