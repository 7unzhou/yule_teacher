package com.yulebaby.teacher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yulebaby.teacher.R;

public class ItemVisible extends LinearLayout {

	private View view;
	private ImageView mIndicatorImage;
	private TextView mDateText;
	private TextView mTagText;

	public ItemVisible(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.order_date_drop,
				null);
		mIndicatorImage = (ImageView) view
				.findViewById(R.id.order_date_indicator);
		mDateText = (TextView) view
				.findViewById(R.id.order_date_indicator_date);
		mTagText = (TextView) view.findViewById(R.id.order_date_indicator_tag);
		this.addView(view);
	}

	public void turnToDowm() {
		mIndicatorImage.setImageResource(R.drawable.icon_drop_down);
	}

	public void turnToUp() {
		mIndicatorImage.setImageResource(R.drawable.icon_drop_up);
	}

	public void setDate(String month, String day) {
		int m = Integer.valueOf(month);
		int d = Integer.valueOf(day);
		mDateText.setText(m + "." + d);
	}
}
