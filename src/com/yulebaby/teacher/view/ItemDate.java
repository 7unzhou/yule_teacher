 package com.yulebaby.teacher.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulebaby.teacher.R;


public class ItemDate extends LinearLayout {
	
	private View view;
	private TextView mMonthText;
	private TextView mDayText;
	private String mMonth;
	private int mIndex = 0;
	private int color_gray;
	
	public ItemDate(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.order_date, null);
		color_gray = context.getResources().getColor(R.color.gray);
				
		mMonthText = (TextView)view.findViewById(R.id.order_date_month);
		mMonthText.setTextColor(Color.WHITE);
		mDayText = (TextView)view.findViewById(R.id.order_date_day);
		mDayText.setTextColor(Color.WHITE);
		this.addView(view);
		mMonth = context.getString(R.string.order_month);
	}
	
	public void setIndex(int index){
		mIndex = index;
	}
	
	public int getIndex(){
		return mIndex;
	}
	
	public void setMinWidth(int width){
		view.setMinimumWidth(width);
	}
	
	public void setMonth(String month){
		int m = Integer.valueOf(month);
		this.mMonthText.setText(m + mMonth);
	}
	
	public void setDay(String day){
		int d = Integer.valueOf(day);
		this.mDayText.setText(String.valueOf(d));
	}
	
	public void setWeekDay(String week){
		this.mMonthText.setText(week);
	}

	public void clicked(boolean click){
		if(click){
//			view.setBackgroundResource(R.drawable.date_select);
			mMonthText.setBackgroundResource(R.drawable.date_top_select);
			mDayText.setBackgroundResource(R.drawable.date_bottom_select);
			mDayText.setTextColor(color_gray);
		}else{
//			view.setBackgroundResource(R.drawable.date_normal);
			mMonthText.setBackgroundResource(R.drawable.date_top_normal);
			mDayText.setBackgroundResource(R.drawable.date_bottom_normal);
			mDayText.setTextColor(Color.WHITE);
		}
	}
}
