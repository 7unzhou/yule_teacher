package com.yulebaby.teacher;

import java.lang.ref.WeakReference;

import com.yulebaby.teacher.fragment.SalaryAddFragment;
import com.yulebaby.teacher.fragment.SalaryCommissionFragment;
import com.yulebaby.teacher.fragment.SalaryDetailFragment;
import com.yulebaby.teacher.fragment.SalaryOhterFragment;
import com.yulebaby.teacher.fragment.SalarySubFragment;
import com.yulebaby.teacher.model.Commons;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SalaryDetailActivity extends FragmentActivity implements
		View.OnClickListener {

	public static final String EXTRA_DATA = "data";
	private static final String TAG = "SalaryDetailActivity";
	private View mLeftView, mRightView;
	private TextView titleView;
	private WeakReference<SalaryDetailActivity> mWeakActivity;

	View contentView;
	String typeTag, strMonth, strDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_salarydetail);
		if (mWeakActivity == null) {
			mWeakActivity = new WeakReference<SalaryDetailActivity>(this);
		}
		mLeftView = findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		mRightView = findViewById(R.id.title_right);
		mLeftView.setOnClickListener(this);
		// mLeftView.setBackgroundResource(R.drawable.selector_button_titlebar_back);
		mRightView.setVisibility(View.INVISIBLE);
		titleView = (TextView) findViewById(R.id.tv_title);

		contentView = findViewById(R.id.salarydetail_content);

		typeTag = getIntent().getExtras().getString("detail_tag");
		strMonth = getIntent().getExtras().getString("month");
		strDay = getIntent().getExtras().getString("day");

		System.out.println("tagg===" + typeTag);
		showFragmentType(typeTag);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.title_left:
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;
		}
	}

	private void showFragmentType(String tagg) {

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		ContentValues params = new ContentValues();
		params.put("month", strMonth);
		// What if i press home ????????????????
		Fragment typeFragment = null;
		if (tagg.equals(Commons.ADD)) {
			titleView.setText(R.string.salary_add);
			// typeFragment = new SalaryAddFragment(strMonth);
			typeFragment = new SalaryOhterFragment(params, tagg);
		}
		if (tagg.equals(Commons.SUB)) {
			titleView.setText(R.string.salary_sub);
			// typeFragment = new SalarySubFragment(strMonth);
			typeFragment = new SalaryOhterFragment(params, tagg);
		}

		if (tagg.equals(Commons.OTHER)) {
			titleView.setText(R.string.salary_other);
			typeFragment = new SalaryOhterFragment(params, tagg);
		}

		if (tagg.equals(Commons.COMMISSION)) {
			titleView.setText(R.string.salary_commission);
			typeFragment = new SalaryCommissionFragment(params, tagg);
		}
		if (tagg.endsWith(Commons.DETAIL)) {
			titleView.setText(strDay);
			params.put("day", strDay);
			typeFragment = new SalaryCommissionFragment(params, tagg);
		}

		ft.replace(contentView.getId(), typeFragment, tagg);
		// ft.addToBackStack(null);

		ft.commit();
	}
}
