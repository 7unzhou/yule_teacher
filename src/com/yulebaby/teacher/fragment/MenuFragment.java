package com.yulebaby.teacher.fragment;

import com.yulebaby.teacher.LoginActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.YuLeAboutActivity;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.utils.ImageCache;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;
import com.yulebaby.teacher.utils.ImageFetchFactory;
import com.yulebaby.teacher.utils.ImageFetcher;
import com.yulebaby.teacher.utils.ImageWorker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MenuFragment extends Fragment implements View.OnClickListener {

	// private View menuIcon;
	private static final String IMAGE_CACHE_DIR = "avatars";

	private ImageView avater;
	private View menuExit;
	// private View menuMessage;
	private View menuTeaching;
	private View menuSalary;
	private View menuAttend;
	private View menuOrder;
	private View menuAlbum;
	private View menuExam;
	private View menuInteraction;
	private View menuManual;
	// private View menuI;

	private int mIndex;

	private ImageView avatar;
	private ImageFetcher mImageFetcher;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				IMAGE_CACHE_DIR);
		// ImageCacheParams cacheParams = new
		// ImageCacheParams(getActivity(),IMAGE_CACHE_DIR);
		// cacheParams.setMemCacheSizePercent(getActivity(), 0.25f);
		mIndex = getArguments().getInt("index");
		// mImageFetcher = new ImageFetcher(getActivity(),
		// getResources().getDimensionPixelSize(R.dimen.avatar));
		// mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		// mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),cacheParams);
		mImageFetcher = ImageFetchFactory.getInstance().initImageFetcher(
				IMAGE_CACHE_DIR, getActivity(),
				getResources().getDimensionPixelSize(R.dimen.avatar));
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.menu_layout, container,
				false);
		// menuIcon = view.findViewById(R.id.menu_logo);

		avatar = (ImageView) view.findViewById(R.id.iv_loginuser);
		mImageFetcher.loadImage(App.getInstance().getLogin().getImgUrl(),
				avatar);

		menuExit = view.findViewById(R.id.menu_exit);
		menuTeaching = view.findViewById(R.id.menu_teaching);
		menuSalary = view.findViewById(R.id.menu_salary);
		menuAlbum = view.findViewById(R.id.menu_album);
		menuAttend = view.findViewById(R.id.menu_attend);
		menuExam = view.findViewById(R.id.menu_exam);
		menuInteraction = view.findViewById(R.id.menu_interaction);
		menuOrder = view.findViewById(R.id.menu_order);
		menuManual = view.findViewById(R.id.menu_manual);

		menuManual.setOnClickListener(this);
		menuAlbum.setOnClickListener(this);
		menuAttend.setOnClickListener(this);
		menuExam.setOnClickListener(this);
		menuInteraction.setOnClickListener(this);
		menuOrder.setOnClickListener(this);

		menuSalary.setOnClickListener(this);
		menuTeaching.setOnClickListener(this);
		// menuIcon.setOnClickListener(this);
		menuExit.setOnClickListener(this);
		// menuMessage.setOnClickListener(this);
		setSelect(mIndex);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	public void onPause() {
		// Log.d(TAG, "ImageGridFragment onPause");
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	public void setSelect(int index) {

		menuTeaching.setBackgroundResource(R.drawable.selector_button_menu);
		menuSalary.setBackgroundResource(R.drawable.selector_button_menu);
		menuExam.setBackgroundResource(R.drawable.selector_button_menu);
		menuInteraction.setBackgroundResource(R.drawable.selector_button_menu);
		menuOrder.setBackgroundResource(R.drawable.selector_button_menu);
		menuManual.setBackgroundResource(R.drawable.selector_button_menu);

		menuAlbum.setBackgroundResource(R.drawable.selector_button_menu);
		menuAttend.setBackgroundResource(R.drawable.selector_button_menu);
		// menuGrowthStep.setBackgroundResource(R.drawable.selector_button_menu);
		switch (index) {

		case SalaryFragment.INDEX:
			menuSalary.setBackgroundResource(R.drawable.button_menu_down);
			break;
		case AlbumFragment.INDEX:
			menuAlbum.setBackgroundResource(R.drawable.button_menu_down);
			break;
		case AttendFragment.INDEX:
			menuAttend.setBackgroundResource(R.drawable.button_menu_down);
			break;
		case OrderFragment.INDEX:
			menuOrder.setBackgroundResource(R.drawable.button_menu_down);
			break;
		case ExamFragment.INDEX:
			menuExam.setBackgroundResource(R.drawable.button_menu_down);
			break;
		case InteractionFragment.INDEX:
			menuInteraction.setBackgroundResource(R.drawable.button_menu_down);
			break;
		case ManualFragment.INDEX:
			menuManual.setBackgroundResource(R.drawable.button_menu_down);
			break;
		case TeachingFragment.INDEX:
			menuTeaching.setBackgroundResource(R.drawable.button_menu_down);
			break;
		// case GrowthStepFragment.INDEX:
		// menuGrowthStep.setBackgroundResource(R.drawable.button_menu_down);
		// break;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		int index = -1;
		switch (id) {

		case R.id.menu_salary:
			index = SalaryFragment.INDEX;
			break;
		case R.id.menu_album:
			index = AlbumFragment.INDEX;
			break;
		case R.id.menu_attend:
			index = AttendFragment.INDEX;
			break;
		case R.id.menu_exam:
			index = ExamFragment.INDEX;
			break;
		case R.id.menu_interaction:
			index = InteractionFragment.INDEX;
			break;
		case R.id.menu_manual:
			index = ManualFragment.INDEX;
			break;
		case R.id.menu_order:
			index = OrderFragment.INDEX;
			break;

		case R.id.menu_teaching:
			index = TeachingFragment.INDEX;
			break;
		// case R.id.menu_growth_step:
		// index = GrowthStepFragment.INDEX;
		// break;
		case R.id.menu_logo:
			// Intent aboutIntent = new Intent(getActivity(),
			// YuLeAboutActivity.class);
			// this.startActivity(aboutIntent);
			return;
		case R.id.menu_exit:
			getActivity().finish();
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			intent.putExtra("auto", false);
			this.startActivity(intent);
			
			return;
		}
		setSelect(index);
		((YuLeActivity) getActivity()).onViewChange(index);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (avatar != null) {
			ImageWorker.cancelWork(avatar);
			avatar.setImageDrawable(null);
		}
		mImageFetcher.closeCache();
	}

}
