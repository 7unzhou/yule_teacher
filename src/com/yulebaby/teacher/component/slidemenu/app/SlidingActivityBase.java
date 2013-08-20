package com.yulebaby.teacher.component.slidemenu.app;

import com.yulebaby.teacher.component.slidemenu.SlidingMenu;

import android.view.View;
import android.view.ViewGroup.LayoutParams;


public interface SlidingActivityBase {
	
	public void setBehindLeftContentView(View v, LayoutParams p);
	
	public void setBehindRightContentView(View v, LayoutParams p);

	public SlidingMenu getSlidingMenu();
		
	public void toggle(int side);
	
	public void showAbove();
	
	public void showBehind(int side);
	
}
