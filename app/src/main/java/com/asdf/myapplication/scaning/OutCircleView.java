package com.asdf.myapplication.scaning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 外圈 view
 */

public class OutCircleView extends View {
    private Context mContext;
    int mWidth;
    int mHeight;
    private float line_width = 2f;//线宽
    private int line_color = 0xFFb1bdaf;//线的颜色
    private int triangle_color = 0xFFb1bdaf;//线的颜色
    private float triangle_width = 12.0f;//等边三角形的边宽
    private float round_circle_radius_size = 3;//原点的大小
    private float radius_size = 200;//整个圆弧的半径

    public float getRadius_size() {
        return radius_size;
    }

    public void setRadius_size(float radius_size) {
        this.radius_size = radius_size;
        invalidate();
    }

    public OutCircleView(Context context) {
        this(context, null, 0);
    }
    public OutCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public OutCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        // Children are just made to fill our space.
        mHeight = mWidth = Math.min(getMeasuredWidth(), getMeasuredWidth());
//        radius_size= (mHeight/2)-round_circle_radius_size-2;//默认的圆比view一样大
        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        // 获取宽高参数
        mHeight = mWidth = Math.min(getWidth(), getHeight());
        for (int i = 0; i < 3; i++) {
            drawTriangle(canvas);
            canvas.rotate(360/3, mWidth / 2, mHeight / 2);
        }
    }

    /**画圆弧 画三角形*/
    private void drawTriangle(Canvas canvas) {
        //***********配置画笔*************/
        Paint paint = new Paint();    //采用默认设置创建一个画笔
        paint.setAntiAlias(true);//使用抗锯齿功能
        paint.setColor(line_color);    //设置画笔的颜色为绿色
        paint.setStyle(Paint.Style.STROKE);//设置画笔类型为STROKE类型（
        paint.setStrokeWidth(line_width);
        float cx = mWidth / 2;
        float cy = (mWidth / 2)-radius_size;
        canvas.drawCircle(cx, cy , round_circle_radius_size, paint);
        /***********绘制圆弧*************/
        RectF rectf_head =  new RectF(
                cy * 1,
                cy * 1,
                getMeasuredWidth() - cy * 1,
                getMeasuredHeight() - cy * 1);//确定外切矩形范围
        canvas.drawArc(rectf_head, -90, 60, false, paint);//绘制圆弧，不含圆心
        canvas.rotate(60, mWidth / 2, mHeight / 2);
        canvas.drawCircle(cx, cy , round_circle_radius_size, paint);
        canvas.rotate(30, mWidth / 2, mHeight / 2);
        Paint p = new Paint();
        p.setColor(triangle_color);
        Path path = new Path();
        float x1 = (mWidth / 2);
        float y1 = (float) ((triangle_width/2.0f)*Math.sqrt(3)) + ( (mWidth / 2)-radius_size ) ;
        float x2 = (mWidth / 2) + (triangle_width/2.0f);
        float y2 =  0 + ( (mWidth / 2)-radius_size ) ;
        float x3 = (mWidth / 2) - (triangle_width/2.0f);
        float y3 = + ( (mWidth / 2)-radius_size ) ;
        path.moveTo(x1,y1);// 此点为多边形的起点
        path.lineTo(x2,y2);
        path.lineTo(x3,y3);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);
        canvas.rotate(-90, mWidth / 2, mHeight / 2);
    }
    /**
     * dp转px
     */
    public  int dp2px(Context context, float dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
