package com.qudiandu.smartreader.ui.main.view.viewhodler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.qudiandu.smartreader.R;
import com.qudiandu.smartreader.utils.ZYResourceUtils;
import com.qudiandu.smartreader.utils.ZYScreenUtils;

public class SRBookSpeedBar extends View {

    private OnProgressChangedListener mOnProgressChangedListener;
    private Paint mPaintPoint;
    private Paint mPaintBg;
    private Paint mPainBar;
    private Paint mPaintText;
    private int mStrokeWidth;
    private int mHeight;
    private int mPointHeight;
    private int mIndex;
    private Rect mRectText;
    private Bitmap mBitmapPoint;
    private String[] mProgress = new String[]{"慢速", "正常", "快速"};
    private float[] mProgressFloat = new float[]{0.5f, 1.0f, 1.5f};
    private float mOneStep;
    private float mPointX;
    private float mBarX;
    private float mOldPointX;
    private float mOldBarX;
    private boolean mIsMoving;
    private boolean mIsAnimating;
    private boolean mIsTouchPoint;
    private int mMargin;


    public SRBookSpeedBar(Context context) {
        super(context);
        init(context);
    }

    public SRBookSpeedBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SRBookSpeedBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mHeight = ZYScreenUtils.dp2px(context, 100);
        mStrokeWidth = ZYScreenUtils.dp2px(context, 4);
        mPaintPoint = new Paint();
        mPaintBg = new Paint();
        mPaintBg.setColor(ContextCompat.getColor(context, R.color.c5));
        mPaintBg.setStrokeWidth(mStrokeWidth);
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStrokeCap(Paint.Cap.ROUND);
        mPainBar = new Paint();
        mPainBar.setColor(ContextCompat.getColor(context, R.color.white));
        mPainBar.setStrokeWidth(mStrokeWidth);
        mPainBar.setAntiAlias(true);
        mPainBar.setStrokeCap(Paint.Cap.ROUND);
        mPaintText = new Paint();
        mPaintText.setColor(ContextCompat.getColor(context, R.color.white));
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.f4));
        mRectText = new Rect();
        mPaintText.getTextBounds(mProgress[1], 0, 2, mRectText);

        mBitmapPoint = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_time_point);
        mPointHeight = mBitmapPoint.getHeight();
        mHeight = mBitmapPoint.getHeight() + mRectText.height() + 10;
        mMargin = ZYScreenUtils.dp2px(context, 20);

    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        mOnProgressChangedListener = onProgressChangedListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textWidth = mRectText.width();
        float pointWidth = mBitmapPoint.getWidth();
        float width = getMeasuredWidth() - 2 * mMargin;
        mOneStep = (width - textWidth) / (mProgress.length - 1);
        if (!mIsMoving && !mIsAnimating) {
            getPointBarX();
        }
        float lineWidth = mOneStep * (mProgress.length - 1);
        if (mPointX < mMargin - pointWidth / 2 + textWidth / 2) {
            mPointX = mMargin - pointWidth / 2 + textWidth / 2;
        }
        if (mPointX > getMeasuredWidth() - mMargin - pointWidth / 2 - textWidth / 2) {
            mPointX = getMeasuredWidth() - mMargin - pointWidth / 2 - textWidth / 2;
        }
        if (mBarX < mMargin + textWidth / 2) {
            mBarX = mMargin + textWidth / 2;
        }
        if (mBarX > getMeasuredWidth() - mMargin - textWidth / 2) {
            mBarX = getMeasuredWidth() - mMargin - textWidth / 2;
        }
        canvas.drawLine(textWidth / 2 + mMargin, mPointHeight / 2,
                textWidth / 2 + lineWidth + mMargin, mPointHeight / 2, mPaintBg);
        canvas.drawLine(textWidth / 2 + mMargin, mPointHeight / 2,
                mBarX, mPointHeight / 2, mPainBar);
        canvas.drawBitmap(mBitmapPoint, mPointX, 0, mPaintPoint);
        for (int i = 0; i < mProgress.length; i++) {
            if (mIndex == i) {
                mPaintText.setColor(ZYResourceUtils.getColor(R.color.c5));
            } else {
                mPaintText.setColor(ZYResourceUtils.getColor(R.color.white));
            }
            canvas.drawText(mProgress[i], i * mOneStep + mMargin, mPointHeight + mRectText.height() * 2.0f / 3,
                    mPaintText);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouchPoint = event.getX() > mPointX && event.getX() < mPointX + mBitmapPoint.getWidth();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsTouchPoint) {
                    mIsMoving = true;
                    mPointX = event.getX() - mBitmapPoint.getWidth() / 2;
                    mBarX = event.getX();
                    getIndex();
                    if (mOnProgressChangedListener != null) {
                        mOnProgressChangedListener.onProgressChanged(mIndex, mProgressFloat[mIndex]);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsTouchPoint) {
                    mOldPointX = mPointX;
                    mOldBarX = mBarX;
                    getIndex();
                    getPointBarX();
                    if (mOnProgressChangedListener != null) {
                        mOnProgressChangedListener.onProgressChanged(mIndex, mProgressFloat[mIndex]);
                    }
                    mIsMoving = false;
                    doAnimation();
                }
                break;
        }
        return true;
    }

    private void doAnimation() {
        mIsAnimating = true;
        ValueAnimator pointAnimator = ValueAnimator.ofFloat(mOldPointX, mPointX);
        pointAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPointX = (float) animation.getAnimatedValue();
                mBarX = mPointX;
                invalidate();
            }
        });
        pointAnimator.setDuration(300);
        pointAnimator.setInterpolator(new DecelerateInterpolator());
        pointAnimator.start();
    }

    public void getIndex() {
        float index = (mBarX - mRectText.width() / 2 - mMargin) / mOneStep;
        mIndex = (int) (index);
        if (index - mIndex > 0.5f) {
            mIndex += 1;
        }
    }

    public void setProgress(float progress) {
        mIndex = getIndex(progress);
    }

    private void getPointBarX() {
        mPointX = mIndex * mOneStep - mBitmapPoint.getWidth() / 2 + mRectText.width() / 2 + mMargin;
        mBarX = mIndex * mOneStep + mRectText.width() / 2 + mMargin;
    }

    public int getIndex(float progress) {
        int index = 0;
        for (int i = 0; i < mProgressFloat.length; i++) {
            if (progress == mProgressFloat[i]) {
                index = i;
                break;
            }
        }

        return index;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int index, float progress);
    }
}
