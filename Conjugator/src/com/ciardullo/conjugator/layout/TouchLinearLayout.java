package com.ciardullo.conjugator.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Implements onInterceptTouchEvent to capture swipes
 * in FlashCardActivity. Used in flashcd.xml
 */
public class TouchLinearLayout extends LinearLayout {

	public TouchLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchLinearLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
}
