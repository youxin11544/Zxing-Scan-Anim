package com.asdf.myapplication.scaning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 刻度尺 view
 */
public class CalibrationView extends View {
    private Context mContext;
    int mWidth;
    int mHeight;
    private int line_color = 0xFF9faba1;//线的颜色
    private float width_longer = 2f;//长刻度宽度
    private float width_shorter = 2f;//短刻度宽度
    private float length_longer = 50;//长刻度长度
    private float length_shorter = 30;//短刻度长度
    private float radius_size = 200;//整个圆弧的半径
    private int calibrationLineCount = 90;//圆圈数量
    public float getRadius_size() {
        return radius_size;
    }

    public void setRadius_size(float radius_size) {
        this.radius_size = radius_size;
        invalidate();
    }
    public CalibrationView(Context context) {
        this(context, null, 0);
    }
    public CalibrationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CalibrationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        length_longer = dp2px(context, 8f);
        length_shorter = dp2px(context, 5f);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        mHeight =  mWidth = Math.min(getMeasuredWidth(), getMeasuredWidth());
//        radius_size = mHeight/2-length_longer;//相当于默认从 view的顶部开始
        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        // 获取宽高参数
        mHeight = mWidth = Math.min(getWidth(), getHeight());
        drawCalibration(canvas);
    }
    private void drawCalibration(Canvas canvas) {
        // 画刻度
        Paint painDegree = new Paint();
        painDegree.setColor(line_color);
        painDegree.setAntiAlias(true);
        for (int i = 0; i < calibrationLineCount; i++) {
            float startY = 0;
            if (i % (calibrationLineCount/3) == 0) {
                painDegree.setStrokeWidth(width_longer);
                startY  = mHeight/2- radius_size-length_longer;
            } else {
                painDegree.setStrokeWidth(width_shorter);
                startY = mHeight/2- radius_size - length_shorter;
            }
            float startX = mWidth / 2;
            float stopX = mWidth / 2;
            float stopY = mHeight/2- radius_size;
            canvas.drawLine(startX, startY, stopX, stopY, painDegree);
            canvas.rotate(360 / calibrationLineCount, mWidth / 2, mHeight / 2);
        }
    }
    /**
     * dp转px
     */
    public  int dp2px(Context context, float dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
