package com.fanqie.appmodel.common.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.fanqie.appmodel.R;


/**
 * Created by Administrator on 2017/5/12.
 */

public class CircleSubmitButton extends View {

    // 绘制矩形区域  精度为 float
    private RectF rectF = new RectF();

    // 绘制文字 矩形区域
    private Rect textRect = new Rect();

    // 两圆圆心之间的距离
    private int two_circle_distance;

    // 变半圆 矩形时 两圆圆心之间的距离
    private int default_two_circle_distance;

    // 矩形 初始宽高
    private int width;
    private int height;

    // 圆角 半径
    private int circleRadius = 10;

    //圆角矩形画笔
    private Paint rectPaint;

    //文字画笔
    private Paint textPaint;

    // 对勾（√）画笔
    private Paint okPaint;

    //动画执行时间
    private long duration;

    //view向上移动距离
    private float move_distance;

    // 矩形画笔颜色
    private int btnPaintClor;

    // 对勾的 颜色
    private int okPaintColor;

    // 文字的颜色
    private int textPaintColor;

    // 文字大小
    private float textSize;

    // 路径的长度
    private PathMeasure pathMeasure;

    // 是否开始绘制对勾
    private boolean startDrawOk;

    // 绘制对勾 的 路径
    private Path path;

    // 上移动画
    private ObjectAnimator animator_move_to_up;

    // 下移动画
    private ObjectAnimator animator_move_down;

    // 从 矩形到圆角矩形 的动画
    private ValueAnimator animator_rect_to_roundRect;

    // 从 圆角矩形到 圆形 的动画
    private ValueAnimator animator_roundRect_to_circle;

    // 绘制 对勾的 动画
    private ValueAnimator animator_draw_ok;

    // 动画集
    private AnimatorSet animatorSet = new AnimatorSet();

    // 按钮 文字
    private String buttonString;

    /**
     * 点击事件及动画事件完成的回调
     */
    private AnimationButtonListener animationButtonListener;

    public void setAnimationButtonListener(AnimationButtonListener listener) {
        this.animationButtonListener = listener;
    }

    public CircleSubmitButton(Context context) {
        super(context, null);
    }

    public CircleSubmitButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSubmitButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取自定义属性的值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleSubmitButton, defStyleAttr, 0);
        if (typedArray != null) {

            btnPaintClor = typedArray.getColor(R.styleable.CircleSubmitButton_buttonBackground, Color.BLUE);
            textPaintColor = typedArray.getColor(R.styleable.CircleSubmitButton_buttonTextColor, Color.WHITE);
            okPaintColor = typedArray.getColor(R.styleable.CircleSubmitButton_buttonOkColor, Color.GREEN);
            buttonString = typedArray.getString(R.styleable.CircleSubmitButton_buttonText);
            duration = (long) typedArray.getFloat(R.styleable.CircleSubmitButton_buttonAnimationDuration, 800);
            textSize = typedArray.getDimension(R.styleable.CircleSubmitButton_buttonTextSize, getResources().getDimension(R.dimen.dimen_x50));
            move_distance = typedArray.getFloat(R.styleable.CircleSubmitButton_buttonMoveupDistance, 300);

            typedArray.recycle();
        }

        initPaint();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationButtonListener != null)
                    animationButtonListener.onClickListener();
            }
        });

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationButtonListener != null) {
                    animationButtonListener.animationFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 创建时间：2017/5/12 11:13  描述：初始化画笔
     */
    private void initPaint() {

        // 矩形画笔
        rectPaint = new Paint();
        rectPaint.setStrokeWidth(4);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(btnPaintClor);

        // 对勾画笔
        okPaint = new Paint();
        okPaint.setStrokeWidth(10);
        okPaint.setStyle(Paint.Style.STROKE);
        okPaint.setStrokeCap(Paint.Cap.ROUND); // 笔触类型  始末点 为圆形
        okPaint.setStrokeJoin(Paint.Join.ROUND);  // 结合处的 状态, 为圆弧
        okPaint.setAntiAlias(true);
        okPaint.setColor(okPaintColor);

        // 文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textPaintColor);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    /**
     * 创建时间：2017/5/12 14:15  描述：画对勾√
     */
    private void initOk() {

        path = new Path();
        path.moveTo(width / 2 - height / 8 * 2 - 10, height / 16 * 7);
        path.lineTo(width / 2 - 10, height / 16 * 11);
        path.lineTo(width / 2 - 10 + height / 8 * 3, height / 5);

        pathMeasure = new PathMeasure(path, true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        default_two_circle_distance = (w - h) / 2;
        initOk();
        initAnimation();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRoundedRect(canvas);
        drawText(canvas);

        if (startDrawOk) {
            canvas.drawPath(path, okPaint);
        }
    }

    /**
     * 初始化所有动画
     */
    private void initAnimation() {

        buttonMoveDownAnimation();
        toCircleRectAnimation();
        toCircleAnimation();
        circleMoveUpAnimation();
        drawOkAnimation();

        animatorSet.playTogether(animator_move_down, animator_rect_to_roundRect);
        animatorSet.playSequentially(animator_roundRect_to_circle, animator_move_to_up, animator_draw_ok);

    }

    /**
     * 创建时间：2017/5/12 17:33  描述：绘制文本
     */
    private void drawText(Canvas canvas) {

        textRect.left = 0;
        textRect.top = 0;
        textRect.right = width;
        textRect.bottom = height;
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt(); // 得到系统默认字体属性
        int baseLine = (textRect.bottom + textRect.top - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
        canvas.drawText(buttonString, textRect.centerX(), baseLine, textPaint);

    }

    /**
     * 创建时间：2017/5/12 10:26  描述：绘制圆角矩形
     */
    private void drawRoundedRect(Canvas canvas) {

        rectF.left = two_circle_distance;
        rectF.top = 0;
        rectF.right = width - two_circle_distance;
        rectF.bottom = height;

        canvas.drawRoundRect(rectF, circleRadius, circleRadius, rectPaint);

    }

    /**
     * 创建时间：2017/5/13 10:28  描述：点击下移动画
     */
    private void buttonMoveDownAnimation() {

        animator_move_down = ObjectAnimator.ofFloat(this, "translationY", 0, 20, 0);
        animator_move_down.setDuration(500);

    }

    /**
     * 创建时间：2017/5/12 11:58  描述：从圆角矩形到半圆矩形的过渡动画
     */
    private void toCircleRectAnimation() {

        animator_rect_to_roundRect = ValueAnimator.ofInt(circleRadius, height / 2);
        animator_rect_to_roundRect.setDuration(duration);

        animator_rect_to_roundRect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleRadius = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    /**
     * 创建时间：2017/5/12 12:00  描述：从 半圆矩形到 圆形 的过渡动画
     */
    private void toCircleAnimation() {

        animator_roundRect_to_circle = ValueAnimator.ofInt(0, default_two_circle_distance);
        animator_roundRect_to_circle.setDuration(duration);
        animator_roundRect_to_circle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                two_circle_distance = (int) animation.getAnimatedValue();

                int alpha = 255 - (two_circle_distance * 255) / default_two_circle_distance;
                textPaint.setAlpha(alpha);

                invalidate();
            }
        });
    }

    /**
     * 创建时间：2017/5/12 14:00  描述：圆形上移动画
     */
    private void circleMoveUpAnimation() {

        // 以 view 的左上角为原点 取得的值   (属性动画)
        float currentTranslationY = this.getTranslationY();
        animator_move_to_up = ObjectAnimator.ofFloat(this, "translationY", currentTranslationY, currentTranslationY - move_distance);
        animator_move_to_up.setDuration(duration);
        animator_move_to_up.setInterpolator(new BounceInterpolator());

    }

    /**
     * 创建时间：2017/5/12 16:07  描述：绘制对勾动画
     */
    private void drawOkAnimation() {

        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(okPaintColor);

        animator_draw_ok = ValueAnimator.ofFloat(1, 0);

        animator_draw_ok.setDuration(duration);

        animator_draw_ok.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                startDrawOk = true;
                float value = (float) animation.getAnimatedValue();
                DashPathEffect dashPathEffect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()},
                        value * pathMeasure.getLength());
                okPaint.setPathEffect(dashPathEffect);
                invalidate();
            }
        });

    }

    public void start() {

        animatorSet.start();

    }

    public interface AnimationButtonListener {

        /**
         * 按钮点击事件
         */
        void onClickListener();

        /**
         * 动画完成回调
         */
        void animationFinish();

    }

}
