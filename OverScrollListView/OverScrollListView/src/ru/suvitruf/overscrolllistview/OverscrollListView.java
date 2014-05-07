/*
* Copyright 2014 Andrey Apanasik (aka Suvitruf)
* Github https://github.com/Suvitruf/Android-sdk-examples/tree/master/OverScrollListView
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package ru.suvitruf.overscrolllistview;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListView;


public class OverscrollListView extends ListView {
	
	
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 50;
	private static final int ANIMATION_DURATION = 300;
	private static final float SLOW_COEFFICIENT = 0.8F;
	
	private int mOverscrollDistance;
	private float mSlowCoefficient;


	

	private int mMaxYOverscrollDistance;
	private boolean mSlowEffect;
	private int mAnimationTime;



	private void setAttr(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.OverscrollListView);
		mOverscrollDistance = (int) a.getDimension(R.styleable.OverscrollListView_maxOverScrollDistance, MAX_Y_OVERSCROLL_DISTANCE);
		mSlowEffect = a.getBoolean(R.styleable.OverscrollListView_slowEffect, false);
		mAnimationTime = a.getInteger(R.styleable.OverscrollListView_animationTime, ANIMATION_DURATION);
		mSlowCoefficient = a.getFloat(R.styleable.OverscrollListView_slowCoefficient, SLOW_COEFFICIENT);
		a.recycle();
	}


	public OverscrollListView(Context context) {
		super(context);
		
		
		mOverscrollDistance = MAX_Y_OVERSCROLL_DISTANCE;
		mSlowEffect = false;
		mAnimationTime = ANIMATION_DURATION;
		mSlowCoefficient = SLOW_COEFFICIENT;
		initOverscrollListView();
	}

	public OverscrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAttr(context, attrs);
		initOverscrollListView();
	}

	public OverscrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setAttr(context, attrs);
		initOverscrollListView();
	}

	private void initOverscrollListView() {
		final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
		final float density = metrics.density;
		mMaxYOverscrollDistance = (int) (density * mOverscrollDistance);
	}
 
	
	public void setSlowEffect(boolean slowEffect){
		mSlowEffect = slowEffect;
	}
	
	public void setSlowCoefficient(int coef){
		mSlowCoefficient = coef;
	}
	

	public void setCollapseAnimationDuration(int duration){
		mAnimationTime = duration;
	}
	
	private double getOverScrollYWithSlow(float y){
		return Math.pow(Math.abs(y), mSlowCoefficient) * (y >0 ? 1 : -1);
	}
	
	private double getReverseOverScrollYWithSlow(float y){
		return Math.pow(Math.abs(y), 1/mSlowCoefficient) * (y >0 ? 1 : -1);
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

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float newY = ev.getRawY();
		
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			historicTime = System.currentTimeMillis();	
			firstY = ev.getRawY();
			clearColapseAnimation();
			lastScroll = 0;
			offset = 0;
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

			int oldLastScroll = lastScroll;
			lastScroll = (int) (firstY - newY);


			// fix for case when simple scroll transforming to overscroll (when touching down from middle/bottom)
			if (lastScroll < 0) 
				if(getScrollY() == 0){
					// we should get offset for future, if we started scrolling from middle/bottom
					if (getFirstVisiblePosition() == 0 && getChildAt(0) != null && getChildAt(0).getTop() == 0) {
						offset = oldLastScroll;
						lastScroll=-1;
					}
				}
				else
					lastScroll-=offset;
			
			// fix for case when simple scroll transforming to overscroll (when touching down from middle/top)
			if (lastScroll >= 0) {
				View v = getChildAt(getChildCount() - 1);
				if(getScrollY() == 0){
					// we should get offset for future, if we started scrolling from middle/top
					// here we have 2 cases:
					// 1: when we started scrolling from bottom position when LastVisibleItem is full visible
					// 2: when we started scrolling from middle/top position when LastVisibleItem is not full visible
					if (getLastVisiblePosition() == getCount() - 1) {
						if (offset == 0)
							if(v.getTop() + v.getHeight() == getHeight())
								offset = 0;
							else
								offset = oldLastScroll + v.getHeight();
						
						if(lastScroll - offset < 0)
							lastScroll = 0;
						else
							lastScroll = lastScroll - offset;
					}		
				}
				else{	
					lastScroll = lastScroll - offset;
				}
			}
					
			
			
			if(mSlowEffect){
				if (getOverScrollYWithSlow(lastScroll) < -mMaxYOverscrollDistance)
					lastScroll = -(int)getReverseOverScrollYWithSlow(mMaxYOverscrollDistance);
				if (getOverScrollYWithSlow(lastScroll) > mMaxYOverscrollDistance)
					lastScroll = (int)getReverseOverScrollYWithSlow(mMaxYOverscrollDistance);
				
			}
			else{
				if (lastScroll < -mMaxYOverscrollDistance)
					lastScroll = -mMaxYOverscrollDistance;
				if (lastScroll > mMaxYOverscrollDistance)
					lastScroll = mMaxYOverscrollDistance;
			}
			
			if (lastScroll < 0) {
				if (getFirstVisiblePosition() == 0 && getChildAt(0) != null && getChildAt(0).getTop() == 0) {
					pullDown(lastScroll, 0);
					return true;

				}
			} else {
				if (getLastVisiblePosition() == getCount() - 1 && lastScroll != 0) {
					pullDown(lastScroll, 0);
				
					return true;
				}

			}
		} else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {

			if (lastScroll < 0 && getFirstVisiblePosition() == 0) {				
				pullUp(lastScroll);
				
				return false;
			}
			if (lastScroll > 0 && getLastVisiblePosition() == getCount() - 1) {
				pullUp(lastScroll);
				return false;
			}

		}

		return super.dispatchTouchEvent(ev);
	}

	
	private void clearColapseAnimation() {
		Animation a = getAnimation();
		
		if(a!= null){
			this.clearAnimation();
			if(a instanceof ColapseAnimation){
				lastScroll = getScrollY();
				firstY +=  lastScroll;
				pullDown(this.lastScroll, 0);
			}
				
			
		}
	}

	private void pullDown(int deltaY, int lastY) {
		overScrollBy(0, mSlowEffect ? (int)getOverScrollYWithSlow(deltaY) : deltaY, 0, lastY, 5, 5, 0, mMaxYOverscrollDistance, true);

		
	}

	private void pullUp(int lastY) {
		Animation a = new ColapseAnimation((mSlowEffect) ? (int)getOverScrollYWithSlow(lastY):lastY, 0);
		a.setDuration(mAnimationTime);
		startAnimation(a);
	}

	private class ColapseAnimation extends Animation {
		private final int mStartY;
		private final int mDeltaY;
		public ColapseAnimation(int startY, int endY) {
			mStartY = startY;
			mDeltaY = endY - startY;
		}
		
		

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			int newDelta = (int) (mDeltaY * interpolatedTime);
			overScrollBy(0, newDelta, 0, mStartY, 5, 5, 0, mMaxYOverscrollDistance, true);
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}

}
