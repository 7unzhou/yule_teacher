package com.yulebaby.teacher.fragment;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yulebaby.teacher.GuideTitleActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.model.GuideDetail;


public class GuideDetailFragment extends Fragment {

	private static final String TAG = "GuideDetailFragment"; 
	public static final int INDEX = 100;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<GuideDetailFragment> mWeakFragment;
	private TextView mGuideView;
	private LoadHtml mHtmlTask;
	private GuideDetail mGuide;
	private int mIndex = 0;
	private TextView mTitleView;
	private TextView mNumberView;
	
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
			mGuide.setSpanned(result);
			mGuideView.setText(result);
		}
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode){
		case Activity.RESULT_OK:
			mIndex = data.getIntExtra(GuideTitleActivity.EXTRA_DATA, mIndex);
			showGuide();
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
			mWeakFragment = new WeakReference<GuideDetailFragment>(this);
		}
		mGuide = (GuideDetail)getArguments().getSerializable("data");
		mHtmlTask = new LoadHtml();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.guide_detail_fragment, container, false);
		mGuideView = (TextView)v.findViewById(R.id.guide);
		mTitleView = (TextView)v.findViewById(R.id.guide_title_detail);
		mNumberView = (TextView)v.findViewById(R.id.guide_number_detail);
		showGuide();
		return v;
	}
	
	private void showGuide(){
		if(mGuide.getSpanned() == null){
			String content = mGuide.getContent();
			mGuideView.setText(Html.fromHtml(content, new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					Drawable mImgLoading = getResources().getDrawable(R.drawable.empty_photo);
			        mImgLoading.setBounds(0, 0, 130, 130);
					return mImgLoading;
				}
			}, null));
			mHtmlTask.cancel(true);
			mHtmlTask = new LoadHtml();
			mHtmlTask.execute(content);
		}else{
			mGuideView.setText(mGuide.getSpanned());
		}
		mTitleView.setText(mGuide.getTitle());
		mNumberView.setText(mGuide.getIndex());
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        mHtmlTask.cancel(true);
    }
}
