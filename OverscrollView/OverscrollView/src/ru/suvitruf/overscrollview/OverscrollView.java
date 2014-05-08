package ru.suvitruf.overscrollview;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ScrollView;

public class OverscrollView extends ScrollView {
 
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 50;
    private static final int ANIMATION_DURATION = 300;
    private static final float SLOW_COEFFICIENT = 0.8F;
    private static final boolean HAS_SLOW_EFFECT = true;

    private int mOverscrollDistance;
    private float mSlowCoefficient;
 
    private int mMaxYOverscrollDistance;
    private boolean mSlowEffect;
    private int mAnimationTime;
 
    private void setAttr(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs,R.styleable.OverscrollView);
        mOverscrollDistance = (int) a.getDimension(R.styleable.OverscrollView_oslvMaxOverScrollDistance,    MAX_Y_OVERSCROLL_DISTANCE);
        mSlowEffect = a.getBoolean(R.styleable.OverscrollView_oslvSlowEffect,    HAS_SLOW_EFFECT );
        mAnimationTime = a.getInteger(    R.styleable.OverscrollView_oslvAnimationTime,ANIMATION_DURATION);
        mSlowCoefficient = a.getFloat(R.styleable.OverscrollView_oslvSlowCoefficient,SLOW_COEFFICIENT);
        a.recycle();
    } 
 
    public OverscrollView(Context context) {
        super(context);
       

        mOverscrollDistance = MAX_Y_OVERSCROLL_DISTANCE;
        mSlowEffect = false;
        mAnimationTime = ANIMATION_DURATION;
        mSlowCoefficient = SLOW_COEFFICIENT;
        initOverscrollListView();
    }

    public OverscrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttr(context, attrs);
        initOverscrollListView();
    }

    public OverscrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttr(context, attrs);
        initOverscrollListView();
    }

    private void initOverscrollListView() {
        final DisplayMetrics metrics = getContext().getResources()
                .getDisplayMetrics();
        final float density = metrics.density;
        mMaxYOverscrollDistance = (int) (density * mOverscrollDistance);
       
       
    }

    public void setSlowEffect(boolean slowEffect) {
        mSlowEffect = slowEffect;
    }

    public void setSlowCoefficient(int coef) {
        mSlowCoefficient = coef;
    }

    public void setCollapseAnimationDuration(int duration) {
        mAnimationTime = duration;
    }

    private double getOverScrollYWithSlow(float y) {
        return Math.pow(Math.abs(y), mSlowCoefficient) * (y > 0 ? 1 : -1);
    }

    private double getReverseOverScrollYWithSlow(float y) {
        return Math.pow(Math.abs(y), 1 / mSlowCoefficient) * (y > 0 ? 1 : -1);
    }
 
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
            int scrollY, int scrollRangeX, int scrollRangeY,
            int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
 
       
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                mMaxYOverscrollDistance, isTouchEvent);
    }
   
    private float firstY;
    private int lastScroll = 0;
    long historicTime;
    private int offset = 0;
    int oldLastScroll = 0;
    int lastY = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float newY = ev.getRawY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            historicTime = System.currentTimeMillis();
            firstY = ev.getRawY();
            clearColapseAnimation();
            lastScroll = 0;
            offset = 0;
            oldLastScroll = 0;
            lastY = 0;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
 
            lastScroll = (int) (firstY - newY);
            // fix for case when simple scroll transforming to overscroll (when
            // touching down from middle/bottom)
            if (lastScroll < 0)
                if (getScrollY() <= 0) {
                    // we should get offset for future, if we started scrolling
                    // from middle/bottom
                    if(offset == 0){
                        offset = oldLastScroll;
                        lastScroll -=  offset;
                    }
                    else
                        lastScroll -= offset;
                   
                }

            // fix for case when simple scroll transforming to overscroll (when
            // touching down from middle/top)
            if (lastScroll > 0) {
                // ScrollView has always 1 child, so...
                if (getScrollY() + getHeight()>= getChildAt(0).getHeight()) {
                    if(offset == 0){
                        offset = oldLastScroll;
                        lastScroll -= offset;
                    }
                     else
                            lastScroll -= offset;
                }
               
            }
           
            oldLastScroll = lastScroll;
            if (mSlowEffect) {
                if (getOverScrollYWithSlow(lastScroll) < -mMaxYOverscrollDistance)
                    lastScroll = -(int) getReverseOverScrollYWithSlow(mMaxYOverscrollDistance);
                if (getOverScrollYWithSlow(lastScroll) > mMaxYOverscrollDistance)
                    lastScroll = (int) getReverseOverScrollYWithSlow(mMaxYOverscrollDistance);

            } else {
                if (lastScroll < -mMaxYOverscrollDistance)
                    lastScroll = -mMaxYOverscrollDistance;
                if (lastScroll > mMaxYOverscrollDistance)
                    lastScroll = mMaxYOverscrollDistance;
            }

            if (lastScroll < 0) {
               
                if (getScrollY() <= 0) {
                    pullDown(lastScroll, 0);
                    return true;

                }
            } else {
               
               
            	// ScrollView has always 1 child, so...
                if (getScrollY() + getHeight()  >= getChildAt(0).getHeight()) {
                    if(lastY == 0)
                        lastY = getScrollY();
                    pullDown(lastScroll , lastY);
                    return true;
                }

            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (getScrollY() <= 0) {
                pullUp(lastScroll, 0);
 
                return false;
            }
            // ScrollView has always 1 child, so...
            if (getScrollY() + getHeight()  >= getChildAt(0).getHeight()) {               
                pullUp(lastScroll, lastY);
                return false;
            }

        }

        return super.dispatchTouchEvent(ev);
    }

    private void clearColapseAnimation() {
        Animation a = getAnimation();

        if (a != null) {
            this.clearAnimation();
            if (a instanceof ColapseAnimation) {
                lastScroll = getScrollY();
                firstY += lastScroll;
                pullDown(this.lastScroll, 0);
            }

        }
    }


    private void pullDown(int deltaY, int lastY) {
        overScrollBy(0, mSlowEffect ? (int) getOverScrollYWithSlow(deltaY)
                : deltaY, 0, lastY, 0, getChildAt(0).getHeight(), 0, mMaxYOverscrollDistance, true);
       
    }

    private void pullUp(int lastScroll, int endY) {
        Animation a = new ColapseAnimation(
                ((mSlowEffect) ? (int) getOverScrollYWithSlow(lastScroll) : lastScroll) + endY, endY, getChildAt(0).getHeight());
        a.setDuration(mAnimationTime);
        startAnimation(a);
    }

    private class ColapseAnimation extends Animation {
        private final int mStartY;
        private final int mDeltaY;
        private final int scrollRangeY;

        public ColapseAnimation(int startY, int endY, int scrollRangeY) {
            mStartY = startY;
            mDeltaY = endY - startY;
            this.scrollRangeY = scrollRangeY;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newDelta = (int) (mDeltaY * interpolatedTime);
            overScrollBy(0, newDelta, 0, mStartY, 5, scrollRangeY, 0,
                    mMaxYOverscrollDistance, true);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}