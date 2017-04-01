package com.asdf.myapplication.scaning;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class InnerCircleView extends View {
    Paint mPaint;
    Context mContext;
    int rectColor = 0x66bab30b;
    int circleColor = 0xFFbab30b;//这个颜色暂时没用
    int mWith;
    int mHeight;
    private float radius_size = 200;//整个圆弧的半径
    private float rect_width = 2*radius_size;//截取矩形的宽度
    private float rect_height = 25;//截取矩形的高度
    int bitmap_width = (int) (2*radius_size);//画布大小
    int bitmap_height = (int) (2*radius_size);
    private float rect_offset = 0;//截取矩形的偏移量
    public float getRadius_size() {
        return radius_size;
    }

    public void setRadius_size(float radius_size) {
        this.radius_size = radius_size;
        invalidate();
    }
    public InnerCircleView(Context context) {
        super(context);
        init(context);
    }

    public InnerCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InnerCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        mWith =  mHeight = Math.min(getMeasuredWidth(), getMeasuredWidth());
//        radius_size = mHeight/2-length_longer;//相当于默认从 view的顶部开始
        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWith, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        initTransitionAnimation();

    }

    private Bitmap drawRectBm() {
//        int a = 0xFFbab30b;
//        int b = 0x4D000000;
        //参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像.
//        LinearGradient lg=new LinearGradient(
//                0,
//                (bitmap_height-rect_height)/2+rect_offset,
//                0,
//                bitmap_height/2+rect_height/2+rect_offset,
//                a,
//                b,
//                Shader.TileMode.CLAMP);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(rectColor);
//        paint.setShader(lg);
        Bitmap bm = Bitmap.createBitmap(bitmap_width, bitmap_height, Bitmap.Config.ARGB_8888);
        Canvas cavas = new Canvas(bm);
        cavas.drawRect(
                new RectF(
                        0,
                (bitmap_height-rect_height)/2+rect_offset,
                rect_width,
                bitmap_height/2+rect_height/2+rect_offset),
                paint);
        return bm;
    }

    private Bitmap drawCircleBm() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        Bitmap bm = Bitmap.createBitmap(bitmap_width, bitmap_height, Bitmap.Config.ARGB_8888);
        Canvas cavas = new Canvas(bm);
        cavas.drawCircle(bitmap_width/2, bitmap_height/2, radius_size, paint);
        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int sc = canvas.saveLayer(0, 0, mWith, mHeight, null, Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        int y = (mWith-bitmap_width)/2;
        int x = (mHeight-bitmap_height)/2;
        PorterDuff.Mode mode =  PorterDuff.Mode.SRC_IN;
        mPaint.setXfermode(null);
        canvas.drawBitmap(drawCircleBm(), x, y, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(drawRectBm(), x, y, mPaint);
        mPaint.setXfermode(null);
        // 还原画布
        canvas.restoreToCount(sc);
    }
    private ValueAnimator transitionAnimator;
    public void startScanAnim() {
        if (transitionAnimator != null) {
            if (!transitionAnimator.isRunning()) {
                transitionAnimator.start();
            }
        }

    }

    public void stopScanAnim() {
        if (transitionAnimator != null) {
            if (transitionAnimator.isRunning()) {
                transitionAnimator.cancel();
            }
        }

    }
    private static final long ROTATING_ANIMATION_DURATION =  1500L;//动画1圈时间
    /**
     * 初始化动画
     */
    public void initTransitionAnimation() {
        transitionAnimator = ValueAnimator.ofFloat(-170,170);
        transitionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rect_offset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        transitionAnimator.setDuration(ROTATING_ANIMATION_DURATION);
        transitionAnimator.setRepeatMode(ValueAnimator.REVERSE);
        transitionAnimator.setRepeatCount(10000);
        transitionAnimator.setInterpolator(new LinearInterpolator());
    }

}