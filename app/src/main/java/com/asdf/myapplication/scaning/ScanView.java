package com.asdf.myapplication.scaning;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.asdf.myapplication.R;

/**
 * Created by chengwan on 2017/3/20.
 */

public class ScanView extends FrameLayout {
    private Context mContext;
    private float GradientCircleView_width;
    private float CalibrationView_width;
    private float CalibrationView_hight;
    private float GradientCircleView_hight;
    private float OutCircleView_width;
    private float OutCircleView_hight;
    private CalibrationView mCalibrationView;
    private GradientCircleView mGradientCircleView;
    private OutCircleView mOutCircleView;
    private float CalibrationView_radius_size;
    private float GradientCircleView_center_circle_radius_size;
    private float OutCircleView_radius_size;
    private float InnerCircleView_width;
    private float InnerCircleView_hight;
    private float InnerCircleView_radius_size;
    private InnerCircleView mInnerCircleView;
    private AnimatorSet animatorSetEnd;
    private AnimatorSet animatorSetMatching;
    private AnimatorSet animatorSetStart;

    public ScanView(Context context) {
        super(context);
        mContext = context;
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(attrs);
    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.scanview);
        InnerCircleView_width = ta.getDimension(R.styleable.scanview_InnerCircleView_width, -1);
        InnerCircleView_hight = ta.getDimension(R.styleable.scanview_InnerCircleView_hight, -1);
        InnerCircleView_radius_size = ta.getDimension(R.styleable.scanview_InnerCircleView_radius_size, dp2px(50));


        CalibrationView_width = ta.getDimension(R.styleable.scanview_CalibrationView_width, -1);
        CalibrationView_hight = ta.getDimension(R.styleable.scanview_CalibrationView_hight, -1);
        CalibrationView_radius_size = ta.getDimension(R.styleable.scanview_CalibrationView_radius_size, dp2px(51));

        GradientCircleView_width = ta.getDimension(R.styleable.scanview_GradientCircleView_width, -1);
        GradientCircleView_hight = ta.getDimension(R.styleable.scanview_GradientCircleView_hight, -1);
        GradientCircleView_center_circle_radius_size = ta.getDimension(R.styleable.scanview_GradientCircleView_center_circle_radius_size, dp2px(58.5f));

        OutCircleView_width = ta.getDimension(R.styleable.scanview_OutCircleView_width, -1);
        OutCircleView_hight = ta.getDimension(R.styleable.scanview_OutCircleView_hight, -1);
        OutCircleView_radius_size = ta.getDimension(R.styleable.scanview_OutCircleView_radius_size, dp2px(65));

        ta.recycle();

        LayoutParams CalibrationView_params = new LayoutParams((int) CalibrationView_width, (int) CalibrationView_hight);
        LayoutParams InnerCircleView_params = new LayoutParams((int) InnerCircleView_width, (int) InnerCircleView_hight);
        LayoutParams GradientCircleView_params = new LayoutParams((int) GradientCircleView_width, (int) GradientCircleView_hight);
        LayoutParams OutCircleView_params = new LayoutParams((int) OutCircleView_width, (int) OutCircleView_hight);

        InnerCircleView_params.gravity = Gravity.CENTER;
        CalibrationView_params.gravity = Gravity.CENTER;
        GradientCircleView_params.gravity = Gravity.CENTER;
        OutCircleView_params.gravity = Gravity.CENTER;

        mInnerCircleView = new InnerCircleView(mContext);
        mCalibrationView = new CalibrationView(mContext);
        mGradientCircleView = new GradientCircleView(mContext);
        mOutCircleView = new OutCircleView(mContext);


        this.addView(mInnerCircleView, InnerCircleView_params);
        this.addView(mCalibrationView, CalibrationView_params);
        this.addView(mGradientCircleView, GradientCircleView_params);
        this.addView(mOutCircleView, OutCircleView_params);


        if (InnerCircleView_radius_size != -1) {
            mInnerCircleView.setRadius_size(InnerCircleView_radius_size);
        }
        if (CalibrationView_radius_size != -1) {
            mCalibrationView.setRadius_size(CalibrationView_radius_size);
        }
        if (GradientCircleView_center_circle_radius_size != -1) {
            mGradientCircleView.setCenter_circle_radius_size(GradientCircleView_center_circle_radius_size);
        }
        if (OutCircleView_radius_size != -1) {
            mOutCircleView.setRadius_size(OutCircleView_radius_size);
        }

    }

    /**
     * 开始扫描动画
     */
    public void startScanAnim() {
        startScanAnim(null);
    }
    public void startScanAnim(Animator.AnimatorListener listener) {
        mInnerCircleView.setVisibility(View.VISIBLE);
        mInnerCircleView.startScanAnim();
        mGradientCircleView.startScanAnim();
        animatorSetStart = new AnimatorSet();
        //开始是60度左右旋转
        animatorSetStart.playTogether(
                creatRotatingAnimation(mCalibrationView, 60),
                creatRotatingAnimation(mOutCircleView, -60));
        animatorSetStart.setDuration(2 * 1000);
        if (listener != null) animatorSetStart.addListener(listener);
        animatorSetStart.start();
    }

    /**
     * 匹配中动画
     */
    public void startScanMatchingAnim() {
        startScanMatchingAnim(null);
    }
    public void startScanMatchingAnim(Animator.AnimatorListener listener) {
        mInnerCircleView.stopScanAnim();
        mInnerCircleView.setVisibility(View.GONE);
        mGradientCircleView.startScanAnim();
        animatorSetMatching = new AnimatorSet();
        animatorSetMatching.playTogether(
                createScalAnimation(mCalibrationView, CalibrationView_radius_size, OutCircleView_radius_size, OutCircleView_radius_size - dp2px(4f)),//放大到外圆大小后缩小
                createScalAnimation(mOutCircleView, OutCircleView_radius_size, CalibrationView_radius_size)
        );
        animatorSetMatching.setDuration(2 * 1000);
        if (listener != null) animatorSetMatching.addListener(listener);
        animatorSetMatching.start();
    }

    /**
     * 扫描结束动画
     */
    public void startScanEndAnim() {
        startScanEndAnim(null);
    }
    public void startScanEndAnim(Animator.AnimatorListener listener) {
        mGradientCircleView.startScanAnim();
        animatorSetEnd = new AnimatorSet();
        animatorSetEnd.playTogether(
                createScalAnimation(mGradientCircleView, "center_circle_radius_size", GradientCircleView_center_circle_radius_size, GradientCircleView_center_circle_radius_size / 2),
                createScalAnimation(mCalibrationView, CalibrationView_radius_size, getMeasuredHeight() / 2),
                createScalAnimation(mOutCircleView, OutCircleView_radius_size, getMeasuredHeight() / 2),
                createAlphaAnimation(mGradientCircleView),
                createAlphaAnimation(mCalibrationView),
                createAlphaAnimation(mOutCircleView)

        );
        animatorSetEnd.setDuration(2 * 1000);
        if (listener != null) animatorSetEnd.addListener(listener);
        animatorSetEnd.start();
    }

    public void resetView() {
        try {
            clearAllAnim();
            mInnerCircleView.setRadius_size(InnerCircleView_radius_size);
            mCalibrationView.setRadius_size(CalibrationView_radius_size);
            mGradientCircleView.setCenter_circle_radius_size(GradientCircleView_center_circle_radius_size);
            mOutCircleView.setRadius_size(OutCircleView_radius_size);
            mInnerCircleView.setAlpha(1);
            mCalibrationView.setAlpha(1);
            mGradientCircleView.setAlpha(1);
            mOutCircleView.setAlpha(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有动画
     */
    public void clearAllAnim() {
        try {
            mInnerCircleView.stopScanAnim();
            mGradientCircleView.stopScanAnim();
            if (animatorSetEnd != null) animatorSetEnd.cancel();
            if (animatorSetMatching != null) animatorSetMatching.cancel();
            if (animatorSetStart != null) animatorSetStart.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObjectAnimator creatRotatingAnimation(View view, float values) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", 0F, values);//360度旋转
        rotateAnimator.setRepeatMode(ValueAnimator.REVERSE);
        rotateAnimator.setRepeatCount(1000);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return rotateAnimator;
    }

    public ObjectAnimator createScalAnimation(View view, String propertyName, float... values) {
        ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(view, propertyName, values);
        mObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return mObjectAnimator;
    }

    public ObjectAnimator createAlphaAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

    public ObjectAnimator createScalAnimation(View view, float... values) {
        return createScalAnimation(view, "radius_size", values);
    }

    /**
     * dp转px
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mContext.getResources().getDisplayMetrics());
    }
}
