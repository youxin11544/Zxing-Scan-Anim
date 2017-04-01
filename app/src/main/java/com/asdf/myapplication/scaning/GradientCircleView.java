package com.asdf.myapplication.scaning;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by chanwan on 2017/3/20 0020.
 */

public class GradientCircleView extends View {
    private Context mContext;
    int mWidth;
    int mHeight;
    private float round_circle_radius_size = 2.5f;//圆圈的半径大小
    private float center_circle_radius_size = 200;//中
    // 心圆的半径的大小
    private ValueAnimator rotateAnimator;
    private float rotateAngle;
    private static final long ROTATING_ANIMATION_DURATION = 8 * 1000L;//动画1圈时间
    float gradient_circle_width = 80;//环形的宽度
    /*百分比*/
    private int percent = 50;
    /*渐变圆周颜色数组*/
    private int[] gradientColorArray = new int[]{Color.parseColor("#8002e2ea"),
            Color.parseColor("#6602e2ea"),
            Color.parseColor("#4D02e2ea"),
            Color.parseColor("#3302e2ea"),
            Color.parseColor("#00000000")};
    private int roundDotCount = 90;//圆圈数量

    public float getCenter_circle_radius_size() {
        return center_circle_radius_size;
    }

    public void setCenter_circle_radius_size(float center_circle_radius_size) {
        this.center_circle_radius_size = center_circle_radius_size;
        invalidate();
    }

    public GradientCircleView(Context context) {
        this(context, null, 0);
    }

    public GradientCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gradient_circle_width = dp2px(context, 12f);
        mContext = context;
        initRotatingAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        // Children are just made to fill our space.
        mHeight = mWidth = Math.min(getMeasuredWidth(), getMeasuredWidth());
//        center_circle_radius_size = mHeight / 2 - gradient_circle_width / 2;//相当于默认从 view的顶部开始
        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取宽高参数
        mHeight = mWidth = Math.min(getWidth(), getHeight());
        canvas.save();
        canvas.rotate(rotateAngle, mWidth / 2, mHeight / 2);
        drawRoundDot(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate(-rotateAngle, mWidth / 2, mHeight / 2);
        drawGradientCircle(canvas);
    }

    /**
     * 绘制圆点
     */
    private void drawRoundDot(Canvas canvas) {
        Paint paintPointer = new Paint();
        paintPointer.setAntiAlias(true);
        paintPointer.setStyle(Paint.Style.FILL);
        float average = (1.0f) / (roundDotCount * 1.0f);
        for (int i = 0; i < roundDotCount; i++) {
            // 画圆心 73,172,102
            float alphaScale = 0.7f;//渐变系数
            float alpha = (i * 1.0f) * average*alphaScale;
            int mColor = Color.argb((int) (0 + 255 * alpha), 2, 226, 234);//圆圈颜色 2,226,234
            paintPointer.setColor(mColor);
            float cx = mWidth / 2;
            float cy = (mWidth / 2) - center_circle_radius_size;
            canvas.drawCircle(cx, cy, round_circle_radius_size, paintPointer);
            canvas.rotate(360 / roundDotCount, mWidth / 2, mHeight / 2);
        }
    }

    /**
     * 绘制颜色渐变圆环
     */
    private void drawGradientCircle(Canvas canvas) {
        Paint gradientCirclePaint = new Paint();
        gradientCirclePaint.setStyle(Paint.Style.STROKE);
        gradientCirclePaint.setAntiAlias(true);
        gradientCirclePaint.setStrokeWidth(gradient_circle_width);

        float circlePadding = (mWidth / 2) - center_circle_radius_size;

        LinearGradient linearGradient = new LinearGradient(
                0,
                0,
                0,
                getMeasuredHeight() - circlePadding,
                gradientColorArray, null, Shader.TileMode.CLAMP);
        gradientCirclePaint.setShader(linearGradient);

        canvas.drawArc(
                new RectF(circlePadding * 1,
                        circlePadding * 1,
                        getMeasuredWidth() - circlePadding * 1,
                        getMeasuredHeight() - circlePadding * 1),
                -90, (float) (percent / 100.0) * 360, false, gradientCirclePaint);
    }

    public void startScanAnim() {
        if (rotateAnimator != null) {
            if (!rotateAnimator.isRunning()) {
                rotateAnimator.start();
            }
        }

    }

    public void stopScanAnim() {
        if (rotateAnimator != null) {
            if (rotateAnimator.isRunning()) {
                rotateAnimator.cancel();
            }
        }

    }

    /**
     * 初始化动画
     */
    public void initRotatingAnimation() {
        rotateAnimator = ValueAnimator.ofFloat(0.0f, 360f * 1);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.setDuration(ROTATING_ANIMATION_DURATION);
        rotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotateAnimator.setRepeatCount(10000);
        rotateAnimator.setInterpolator(new LinearInterpolator());
    }

    /**
     * dp转px
     */
    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
