package com.yulebaby.teacher.view;

import com.yulebaby.teacher.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;


public class FixedGridLayout extends LinearLayout {
	private int mCellWidth;
	private int mCellHeight;
	private int mLeft = 10;
	private int mTop = 10;
	private int mColumn = 5;
//	private int mWidthSplite = 5;

	public FixedGridLayout(Context context, AttributeSet set) {
		super(context, set);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		setLayoutAnimation(new LayoutAnimationController(
				AnimationUtils.loadAnimation(this.getContext(), R.anim.push_in)));
	}

	public void setCellWidth(float px) {
		float w = px - (mColumn + 1) * mLeft;
		mCellWidth = (int)w / mColumn;
//		mCellWidth = mCellWidth - mWidthSplite;
		requestLayout();
	}

	public void setCellHeight(float px) {
		mCellHeight = (int) px;
		requestLayout();
	}
	
	public int getCellWidth(){
		return mCellWidth;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth,
				MeasureSpec.UNSPECIFIED);
		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight,
				MeasureSpec.EXACTLY);
		int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);
			child.measure(cellWidthSpec, cellHeightSpec);
		}
		int minCount = count > 0 ? count : 0;
		int line = (count/5) + 1; 
		if(minCount == 0){
			setMeasuredDimension(
					resolveSize(mCellWidth * minCount, widthMeasureSpec),
					mTop);
		}else{
			setMeasuredDimension(resolveSize(mCellWidth * minCount, widthMeasureSpec),
					resolveSize(mCellHeight * line, heightMeasureSpec) + mTop * (1 + line));
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int cellWidth = mCellWidth;
		int cellHeight = mCellHeight;
		int columns = (r - l) / cellWidth;
		if (columns < 0) {
			columns = 1;
		}
		int x = 0;
		int y = 0;
		int i = 0;
		int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);

			int w = child.getMeasuredWidth();
			int h = child.getMeasuredHeight();
			int left = mLeft + x ;
			int top = y + mTop;
			child.layout(left, top, left + w, top + h);
			if (i >= (columns - 1)) {
				// advance to next row
				i = 0;
				x = 0;
				y += cellHeight + mTop;
			} else {
				i++;
				x += cellWidth + mLeft;
			}
		}
	}
}
