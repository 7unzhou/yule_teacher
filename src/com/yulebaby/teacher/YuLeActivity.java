package com.yulebaby.teacher;

import com.yulebaby.teacher.application.PhoneInfo;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.component.slidemenu.app.SlidingFragmentActivity;
import com.yulebaby.teacher.fragment.AlbumFragment;
import com.yulebaby.teacher.fragment.AttendFragment;
import com.yulebaby.teacher.fragment.ExamFragment;
import com.yulebaby.teacher.fragment.GuideFragment;
import com.yulebaby.teacher.fragment.InteractionFragment;
import com.yulebaby.teacher.fragment.ManualFragment;
import com.yulebaby.teacher.fragment.MenuFragment;
import com.yulebaby.teacher.fragment.OrderFragment;
import com.yulebaby.teacher.fragment.SalaryFragment;
import com.yulebaby.teacher.fragment.TeachingFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class YuLeActivity extends SlidingFragmentActivity {

	private int mViewIndex = 0;
	private int mIndex;
	private Fragment mCurrentFragment;
	private MenuFragment mMenuFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mIndex = getIntent().getIntExtra("home", GuideFragment.INDEX);

		addLeft(mIndex);

		addBody(mIndex);

		setSlidingActionBarEnabled(true);

		SlidingMenu sm = getSlidingMenu();
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		int width = PhoneInfo.getInstance(this).getWidth()
				- (int) getResources().getDimension(R.dimen.menu_width);
		sm.setShadowDrawable(R.drawable.shadow, SlidingMenu.LEFT);
		sm.setBehindOffset(width, SlidingMenu.LEFT);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		int index = intent.getIntExtra("home", GuideFragment.INDEX);
		onViewChange(index);
		if (mMenuFragment != null) {
			mMenuFragment.setSelect(index);
		}
	}

	private void addLeft(int index) {
		FrameLayout left = new FrameLayout(this);
		left.setId("Menu".hashCode());
		setBehindLeftContentView(left);
		mMenuFragment = new MenuFragment();
		final Bundle args = new Bundle();
		args.putInt("index", index);
		mMenuFragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace("Menu".hashCode(), mMenuFragment).commit();
	}

	private void addBody(int index) {
		LinearLayout body = new LinearLayout(this);
		body.setId("BODY".hashCode());
		body.setLayoutParams(new LayoutParams(-1, -1));
		setContentView(body);
		// getSupportFragmentManager().beginTransaction()
		// .replace("BODY".hashCode(), new GuideFragment()).commit();
		onViewChange(index);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mCurrentFragment.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 添加标题栏的事件
	 * */
	public void onViewChange(int index) {
		if (index == mViewIndex) {
			getSlidingMenu().showAbove();
			return;
		}
		mViewIndex = index;
		mIndex = index;
		switch (mViewIndex) {
		case GuideFragment.INDEX:
			mCurrentFragment = new GuideFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();
			break;
		case TeachingFragment.INDEX:
			mCurrentFragment = new TeachingFragment(this.type,selection);
			getSupportFragmentManager().beginTransaction().replace("BODY".hashCode(), mCurrentFragment).commitAllowingStateLoss();
			break;
		case SalaryFragment.INDEX:
			mCurrentFragment = new SalaryFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();

			break;

		case AlbumFragment.INDEX:
			mCurrentFragment = new AlbumFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();

			break;
		case OrderFragment.INDEX:
			mCurrentFragment = new OrderFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();

			break;
		case ManualFragment.INDEX:
			mCurrentFragment = new ManualFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();

			break;

		case InteractionFragment.INDEX:
			mCurrentFragment = new InteractionFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();

			break;
		case ExamFragment.INDEX:
			mCurrentFragment = new ExamFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();

			break;
		case AttendFragment.INDEX:
			mCurrentFragment = new AttendFragment();
			getSupportFragmentManager().beginTransaction()
					.replace("BODY".hashCode(), mCurrentFragment).commit();

			break;

		}
		getSlidingMenu().showAbove();
	}
	
	String type;
	int selection=0;
	public void setTeaching(String type,int selection){
		this.type = type;
		this.selection  = selection;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getSlidingMenu().isBehindShowing()) {
				getSlidingMenu().showAbove();
				return true;
			} else {
				this.toggle(SlidingMenu.LEFT);
				//this.finish();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

}
