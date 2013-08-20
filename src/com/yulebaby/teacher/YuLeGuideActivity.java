package com.yulebaby.teacher;

import java.util.ArrayList;
import java.util.List;

import com.yulebaby.teacher.fragment.TeachingFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class YuLeGuideActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private int[] mImg = new int[]{R.drawable.ui_introduce_1,R.drawable.ui_introduce_2
			,R.drawable.ui_introduce_3,R.drawable.ui_introduce_4};
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.yule_guide_activity);
		mViewPager = (ViewPager)this.findViewById(R.id.guide_pager);
		PagerAdapter adapter = new PagerAdapter(this.getSupportFragmentManager(),
				mViewPager);
		final ImageView imgOne = (ImageView)this.findViewById(R.id.one);
		final ImageView imgTwo = (ImageView)this.findViewById(R.id.two);
		final ImageView imgThree = (ImageView)this.findViewById(R.id.three);
		final ImageView imgFour = (ImageView)this.findViewById(R.id.four);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
//				Log.d(App.TAG, "onPageSelected arg0 -->" + arg0);
				if(arg0 == 4){
					gotoMainActivity();
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				imgOne.setImageResource(R.drawable.dot_normal);
				imgTwo.setImageResource(R.drawable.dot_normal);
				imgThree.setImageResource(R.drawable.dot_normal);
				imgFour.setImageResource(R.drawable.dot_normal);
				switch(arg0){
				case 0:
					imgOne.setImageResource(R.drawable.dot_select);
					break;
				case 1:
					imgTwo.setImageResource(R.drawable.dot_select);
					break;
				case 2:
					imgThree.setImageResource(R.drawable.dot_select);
					break;
				case 3:
					imgFour.setImageResource(R.drawable.dot_select);
					break;
				}
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
//				Log.d(App.TAG, "onPageScrollStateChanged arg0 -->" + arg0);
			}
		});
		for(int i=0,size=mImg.length;i<size;i++){
			adapter.addPage(new GuideFragment(i, mImg[i]));
		}
		adapter.addPage(new GuideFragment(-1, -1));
	}
	

	class GuideFragment extends Fragment{
		private int mIndex = 0;
		private int src;
		
		public GuideFragment(int index, int bitmapSrc) {
			mIndex = index;
			src = bitmapSrc;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.yule_guide_item, container, false);
			ImageView image = (ImageView)view.findViewById(R.id.guide_img);
			Button btn = (Button)view.findViewById(R.id.guide_btn);
			btn.setVisibility(View.INVISIBLE);
			if(mIndex == -1){
				image.setBackgroundColor(Color.TRANSPARENT);
			}else{
				image.setImageResource(src);
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						gotoMainActivity();
					}
				});
//				if(mIndex == 3){
//					btn.setVisibility(View.VISIBLE);
//				}
			}
			return view;
		}
	}
	
	
	private void gotoMainActivity(){
		
		Intent intent = new Intent(this, YuLeActivity.class);
		intent.putExtra("home", TeachingFragment.INDEX);
		this.startActivity(intent);
		
		
//		Intent intent = new Intent(this, MainActivity.class);
//		this.startActivity(intent);
		this.finish();
	}
	
	public class PagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> mFragments = new ArrayList<Fragment>();
		private ViewPager mPager;

		public PagerAdapter(FragmentManager fm, ViewPager vp) {
			super(fm);
			mPager = vp;
			mPager.setAdapter(this);
		}
		
		public void addPage(Fragment f){
			mFragments.add(f);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
	}
}
