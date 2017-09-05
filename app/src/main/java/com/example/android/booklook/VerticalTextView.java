package com.example.android.booklook;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Based on https://stackoverflow.com/questions/1258275/vertical-rotated-label-in-android/7855852#7855852
 */

public class VerticalTextView extends android.support.v7.widget.AppCompatTextView {

    boolean topDown;

    public VerticalTextView(Context ctxt, AttributeSet ats) {
        super(ctxt, ats);
        final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
            topDown = false;
        } else
            topDown = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected boolean setFrame(int left, int top, int right, int bottom) {
        return super.setFrame(left, top, left + (bottom - top), top + (right - left));
    }

    @Override
    public void draw(Canvas canvas) {
        if (topDown) {
            canvas.translate(getHeight(), 0);
            canvas.rotate(90);
        } else {
            canvas.translate(0, getWidth());
            canvas.rotate(-90);
        }
        canvas.clipRect(0, 0, getWidth(), getHeight(), android.graphics.Region.Op.REPLACE);
        super.draw(canvas);
    }
}