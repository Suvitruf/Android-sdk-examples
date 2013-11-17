package ru.suvitruf.flowlayoutexample.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import ru.suvitruf.flowlayoutexample.R;

/**
 * @author Andrey Apanasik
 *
 */
public class FlowLayout extends ViewGroup {
	// padding bettwen elements
    private int PAD_H, PAD_V; 
    private int mHeight;

    public FlowLayout(Context context) {
        super(context);
        setPaddings(0,0);
    }

    protected void setPaddings(int V, int H){
    	 PAD_H = H;
         PAD_V = V;
    }
    
    protected void setPaddings(Context ctx, AttributeSet attrs){
		TypedArray a = ctx
				.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
		String H = a.getString(R.styleable.FlowLayout_paddingH);
		String V = a.getString(R.styleable.FlowLayout_paddingV);
		// LOG.d("H = " + H + "V=" + V);
		if (H == null || V == null)
			setPaddings(V == null ? 0 : Integer.parseInt(V), H == null ? 0 :Integer.parseInt(H));
		else {
			setPaddings(Integer.parseInt(V), Integer.parseInt(H));
			a.recycle();
		}
    	   
    }
    
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaddings(context,attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
        setPaddings(context,attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);
        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();
        int childHeightMeasureSpec;
        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST)
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        else
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        mHeight = 0;
        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if(child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), childHeightMeasureSpec);
                final int childw = child.getMeasuredWidth();
                mHeight = Math.max(mHeight, child.getMeasuredHeight() + PAD_V);
                if(xpos + childw > width) {
                    xpos = getPaddingLeft();
                    ypos += mHeight;
                }
                xpos += childw + PAD_H;
            }
        }
        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = ypos + mHeight;
        } else if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if(ypos + mHeight < height) {
                height = ypos + mHeight;
            }
        }
        height += 5; // Fudge to avoid clipping bottom of last row.
        setMeasuredDimension(width, height);
    } 

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();
        for(int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if(child.getVisibility() != GONE) {
                final int childw = child.getMeasuredWidth();
                final int childh = child.getMeasuredHeight();
                if(xpos + childw > width) {
                    xpos = getPaddingLeft();
                    ypos += mHeight;
                }
                child.layout(xpos, ypos, xpos + childw, ypos + childh);
                xpos += childw + PAD_H;
            }
        }
    } 

}

