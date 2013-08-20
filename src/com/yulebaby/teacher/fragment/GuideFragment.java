package com.yulebaby.teacher.fragment;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yulebaby.teacher.GuideTitleActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.model.GuideDetail;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.GuideService;


public class GuideFragment extends Fragment implements View.OnClickListener {

	private static final String TAG = "GuideFragment"; 
	public static final int INDEX = 100;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<GuideFragment> mWeakFragment;
	private LoadHtml mHtmlTask;
	private ArrayList<GuideDetail> mGuides;
	private ArrayList<String> mTitles = null;
	private LoadGuide mLoadTask;
	private int mIndex = 0;
	private View mLeftView,mRightView;
	
//	private TextView mGuideView;
//	private TextView mTitleView;
//	private TextView mNumberView;

	//private ViewPager mViewPager;
	//private PagerAdapter mPagerAdapter;
	private boolean left = false;
	private boolean right = false;
	private boolean isScrolling = false;
	private int lastValue = -1;
	private int mPageIndex = 0;
	
	
	private class LoadHtml extends AsyncTask<Object, Void, Spanned> {
    	public LoadHtml(){
    	}
		@Override
		protected Spanned doInBackground(Object... params) {
			return Html.fromHtml(String.valueOf(params[0]), new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					Drawable d = null;
					try {
						InputStream is = new DefaultHttpClient().execute(new HttpGet(source)).getEntity().getContent();
						Bitmap bm = BitmapFactory.decodeStream(is);
						d = new BitmapDrawable(bm);
						d.setBounds(0, 0, bm.getWidth(), bm.getHeight());
					} catch (Exception e) {e.printStackTrace();}
					return d;
				}
			}, null);
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		@Override
		protected void onPostExecute(Spanned result) {
			if(result == null){
				Log.d(TAG, "onPostExecute result == null");
				return;
			}
			mGuides.get(mIndex).setSpanned(result);
//			mGuideView.setText(result);
		}
    }
	
	private class LoadGuide extends AsyncTask<Object, Void, ModelData<GuideDetail>> {
    	private Context context;
    	private ArrayList<GuideDetail> guides;
    	
    	public LoadGuide(GuideFragment fragment, ArrayList<GuideDetail> guides){
    		this.context = fragment.getActivity();
    		this.guides = guides;
    	}
		@Override
		protected ModelData<GuideDetail> doInBackground(Object... params) {
			ModelData<GuideDetail> guides = GuideService.guide(context);
			return guides;
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		@Override
		protected void onPostExecute(ModelData<GuideDetail> result) {
			if(mWeakActivity.get() == null && mWeakActivity.get().isFinishing()){
				return;
			}
			if(mWeakFragment.get() == null){
				return;
			}
			if(result == null){
				Log.d(TAG, "onPostExecute result == null");
				//Toast.makeText(context, App.mNetWorkIssue, Toast.LENGTH_SHORT).show();
				return;
			}
			switch(result.getResultCode()){
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
				break;
			case Response.OK:
				guides.addAll(result.getModels());
				mTitles = new ArrayList<String>();
				for(GuideDetail guide : mGuides){
					mTitles.add(guide.getTitle());
				}
				showGuide();
//				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
				break;
			case Response.ServerError:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
				break;
			case Response.LogicalErrors:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
				break;
			}
		}
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode){
		case Activity.RESULT_OK:
			mIndex = data.getIntExtra(GuideTitleActivity.EXTRA_DATA, mIndex);
//			showGuide();
//			mViewPager.setCurrentItem(mIndex);
			break;
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(mWeakActivity == null){
			mWeakActivity = new WeakReference<YuLeActivity>((YuLeActivity)activity);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mWeakFragment == null){
			mWeakFragment = new WeakReference<GuideFragment>(this);
		}
		mGuides = new ArrayList<GuideDetail>(10);
		mHtmlTask = new LoadHtml();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.guide_detail_viewpager_fragment, container, false);
//		mViewPager = (ViewPager)v.findViewById(R.id.pager);
//		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//			@Override
//			public void onPageSelected(int arg0) {
//				mPageIndex = arg0;
//				if(mPageIndex == 0)
//					mWeakActivity.get().getSlidingMenu().setSlidingEnabled(true);
//			}
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				if (isScrolling) {
//					if (lastValue > arg2) {
//						// 递减，向右侧滑动
//						right = true;
//						left = false;
//					} else if (lastValue < arg2) {
//						// 递减，向右侧滑动
//						right = false;
//						left = true;
//					} else if (lastValue == arg2) {
//						right = left = false;
//						if(arg0 == 0)
//							mWeakActivity.get().getSlidingMenu().setSlidingEnabled(true);
//					}
//				}
//				lastValue = arg2;
//			}
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				if (arg0 == 1) {
//					isScrolling = true;
//				} else {
//					isScrolling = false;
//				}
//				if (arg0 == 2) {
//					if(mPageIndex == 0 && right == true){
//						mWeakActivity.get().getSlidingMenu().setSlidingEnabled(true);
//					}else{
//						mWeakActivity.get().getSlidingMenu().setSlidingEnabled(false);
//					}
//					Log.d("meityitianViewPager",
//							"ViewPager  onPageScrollStateChanged  direction left ? "
//									+ left);
//					Log.d("meityitianViewPager",
//							"ViewPager  onPageScrollStateChanged  direction right ? "
//									+ right);
//					right = left = false;
//				}
//			}
//		});
		mLeftView = v.findViewById(R.id.title_left);
		mRightView = v.findViewById(R.id.title_right);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);
		//mLeftView.setBackgroundResource(R.drawable.selector_button_titlebar_menu);
		//mRightView.setBackgroundResource(R.drawable.selector_button_titlebar_list);
		mLoadTask = new LoadGuide(this, mGuides);
		mLoadTask.execute();
		return v;
	}
	
	private void showGuide(){
//		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
//		for(GuideDetail guide : mGuides){
//			Fragment fragment = new GuideDetailFragment();
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("data", guide);
//			fragment.setArguments(bundle);
//			fragments.add(fragment);
//		}
		
//		mPagerAdapter = new PagerAdapter(this.getFragmentManager(), mViewPager, mGuides.size());
//		mViewPager.setAdapter(mPagerAdapter);
//		mPagerAdapter.notifyDataSetChanged();
		
//		GuideDetail guide = mGuides.get(mIndex);
//		if(guide.getSpanned() == null){
//			String content = guide.getContent();
//			mGuideView.setText(Html.fromHtml(content, new ImageGetter() {
//				@Override
//				public Drawable getDrawable(String source) {
//					Drawable mImgLoading = getResources().getDrawable(R.drawable.empty_photo);
//			        mImgLoading.setBounds(0, 0, 130, 130);
//					return mImgLoading;
//				}
//			}, null));
//			mHtmlTask.cancel(true);
//			mHtmlTask = new LoadHtml();
//			mHtmlTask.execute(content);
//		}else{
//			mGuideView.setText(guide.getSpanned());
//		}
//		mTitleView.setText(mGuides.get(mIndex).getTitle());
//		mNumberView.setText(mGuides.get(mIndex).getIndex());
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        mHtmlTask.cancel(true);
        mLoadTask.cancel(true);
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.title_left:
			mWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			if(mTitles != null && mTitles.size() > 0){
				Intent intent = new Intent(mWeakActivity.get(), GuideTitleActivity.class);
				intent.putExtra("data", mTitles);
				intent.putExtra(GuideTitleActivity.EXTRA_DATA, mIndex);
				this.startActivityForResult(intent, 100);
			}
			break;
		}
	}
	
	public class PagerAdapter extends FragmentStatePagerAdapter {
		private ViewPager mPager;
		private final int mSize;
		
		public PagerAdapter(FragmentManager fm, ViewPager vp, int size) {
			super(fm);
			mPager = vp;
			mPager.setAdapter(this);
			mSize = size;
		}

		@Override 
	    public int getItemPosition(Object object) { 
	        return POSITION_NONE; 
	    }
		
		@Override
		public Fragment getItem(int position) {
			GuideDetail guide = mGuides.get(position);
			Fragment fragment = new GuideDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("data", guide);
			fragment.setArguments(bundle);
			return fragment;
		}
		
		@Override
		public int getCount() {
			return mSize;
		}
	}
}
