package com.yulebaby.teacher.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yulebaby.teacher.GalleryActivity;
import com.yulebaby.teacher.OrderCustomDetailActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.adapter.MyDeletedViewAdapter;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.model.OrderDay;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.OrderCustomService;
import com.yulebaby.teacher.utils.AsyncTask;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;
import com.yulebaby.teacher.utils.ImageFetchFactory;
import com.yulebaby.teacher.utils.ImageFetcher;
import com.yulebaby.teacher.view.FixedGridLayout;
import com.yulebaby.teacher.view.ItemDate;
import com.yulebaby.teacher.view.ItemVisible;
import com.yulebaby.teacher.view.SwipeToDeleteListView;

public class OrderFragment extends Fragment implements View.OnClickListener,
		SwipeToDeleteListView.OnItemDeletedListener,
		SwipeToDeleteListView.OnItemDeletionConfirmedListener,
		AdapterView.OnItemClickListener {

	public static final int INDEX = 214;
	private static final String TAG = "OrderFragment";
	private static final String IMAGE_CACHE_DIR = "avatars";

	private View mLeftView, mRightView;
	private TextView tvTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<OrderFragment> mWeakFragment;

	private SwipeToDeleteListView listView;
	public ArrayList<OrderCustom> mCustomList;

	OrderCutsomAdapter adapter;
	private LoadOrderList mLoadTask;
	private ImageFetcher mImageFetcher;

	private LinearLayout mFixedLayoutController;
	private FixedGridLayout mGridLayout;
	private ItemVisible mItemVisible;
	private View.OnClickListener mDayOnclick;
	private int mSelectedIndex = 0;
	private LoadDays mLoadDays;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Commons.ORDER_DETAIL:
			case Commons.ORDER_STUDT:
				// System.out.println("Order detail");
				OrderCustom model = mCustomList.get(msg.arg1);
				goToOrderCustomDetail(model, msg.what);
				// break;

				// System.out.println("Order student");
				// goToOrderCustomDetail(model);
				break;
			case Commons.TAKE_POHOTO:
				System.out.println("Order take pohoto");
				goToGrallery(mCustomList.get(msg.arg1).getId() + "");
				break;

			default:
				break;
			}
		};
	};

	private class LoadDays extends AsyncTask<Object, Void, ModelData<OrderDay>> {
		private Context context;
		private ArrayList<OrderDay> days;
		private OrderFragment fragment;

		public LoadDays(OrderFragment fragment, ArrayList<OrderDay> days) {
			days.clear();
			this.context = fragment.getActivity();
			this.fragment = fragment;
			this.days = days;
		}

		@Override
		protected ModelData<OrderDay> doInBackground(Object... params) {
			ModelData<OrderDay> days = OrderCustomService.reserveDays(context);
			return days;
		}

		@Override
		protected void onPostExecute(ModelData<OrderDay> result) {
			if (mWeakActivity.get() == null
					|| mWeakActivity.get().isFinishing()) {
				return;
			}

			if (result == null) {
				Log.d(TAG, "onPostExecute result == null");
				Toast.makeText(context, App.mNetWorkIssue, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			switch (result.getResultCode()) {
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.OK:
				days.clear();
				days.addAll(result.getModels());
				fragment.refreshOrderDay(false);
				if (days.size() > 0)
					// fragment.loadHoursByDay(days.get(0).getDate());
					// Toast.makeText(context, result.getMessage(),
					// Toast.LENGTH_SHORT).show();
					break;
			case Response.ServerError:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.LogicalErrors:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	private class LoadOrderList extends
			AsyncTask<Object, Void, ModelData<OrderCustom>> {
		private Context context;
		private ArrayList<OrderCustom> orderList;
		private BaseAdapter adapter;
		private volatile boolean isFinish = false;

		public LoadOrderList(Context context, ArrayList<OrderCustom> swims,
				BaseAdapter adapter) {
			this.context = context;
			this.orderList = swims;
			this.adapter = adapter;
		}

		@Override
		protected ModelData<OrderCustom> doInBackground(Object... params) {
			ModelData<OrderCustom> swims = OrderCustomService.orderList(
					context, String.valueOf(params[0]));
			return swims;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mEmptyView.setVisibility(View.GONE);
		}

		public synchronized boolean isFinish() {
			return isFinish;
		}

		@Override
		protected void onPostExecute(ModelData<OrderCustom> result) {
			if (mWeakActivity.get() == null
					|| mWeakActivity.get().isFinishing()) {
				return;
			}
			if (mWeakFragment.get() == null)
				return;
			if (result == null) {
				isFinish = true;
				Log.d(TAG, "onPostExecute result == null");
				Toast.makeText(context, App.mNetWorkIssue, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			// mEmptyView.setVisibility(View.VISIBLE);
			switch (result.getResultCode()) {
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.OK:
				if (result.getModels().size() > 0) {
					// mEmptyView.setVisibility(View.GONE);
				}
				orderList.clear();
				orderList.addAll(result.getModels());
				System.out.println("return result listlenght:"
						+ orderList.size());

				adapter = new OrderCutsomAdapter();
				listView.setAdapter(adapter);

				break;
			case Response.ServerError:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.LogicalErrors:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			isFinish = true;
		}
	}

	class OrderCutsomAdapter extends BaseAdapter {
		// 定义Context
		private int size;

		public OrderCutsomAdapter() {
			size = mCustomList.size();
			System.out.println("customList.size():" + size);
		}

		// 获取图片的个数
		public int getCount() {
			return size;
		}

		// 获取图片在库中的位置
		public Object getItem(int position) {
			return mCustomList.get(position);
		}

		// 获取图片ID
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
			size = mCustomList.size();
			// System.out.println("notifyDataSetChanged :"+size);
		}

		@Override
		public void notifyDataSetInvalidated() {
			// TODO Auto-generated method stub
			super.notifyDataSetInvalidated();
			size = mCustomList.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// ImageView imageView;
			// if (null == mCustomList || mCustomList.size() == 0)
			// return null;
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.customorder_item, null);
				holder = new ViewHolder();
				holder.ivHeader = (ImageView) convertView
						.findViewById(R.id.custom_header);
				holder.tvName = (TextView) convertView
						.findViewById(R.id.tv_customname);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.order_content);
				holder.tvSurplusTime = (TextView) convertView
						.findViewById(R.id.order_surplus_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			OrderCustom model = mCustomList.get(position);

			holder.tvName.setText(model.getName());
			String reserveTime = getResources().getString(R.string.reserveTime);
			String strRTime = String
					.format(reserveTime, model.getReserveTime());
			holder.tvContent.setText(strRTime);
			// String remainTime =
			// getResources().getString(R.string.remainTime);
			// String strRETime = String.format(remainTime,
			// model.getRemainTime());
			holder.tvSurplusTime.setText(model.getRemainTime());

			mImageFetcher.loadImage(mCustomList.get(position).getImgUrl(),
					holder.ivHeader);

			// holder.tvContent.setText(text)

			return convertView;
		}

		class ViewHolder {
			TextView tvName;
			TextView tvContent;
			TextView tvSurplusTime;
			ImageView ivHeader;
			// TextView timeText;
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (mWeakActivity == null) {
			mWeakActivity = new WeakReference<YuLeActivity>(
					(YuLeActivity) activity);
		}
		mWeakActivity.get().getSlidingMenu().setSlidingEnabled(true);
	}

	protected void goToOrderCustomDetail(OrderCustom model, int type) {
		Intent intent = new Intent(this.getActivity(),
				OrderCustomDetailActivity.class);
		intent.putExtra("data", model);
		intent.putExtra("show_type", type);
		startActivityForResult(intent, 9999);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("onActivityResult requestCode:"+requestCode +" resultCode:"+resultCode);
		if (resultCode == 9998) {
			Bundle b = data.getExtras(); // data为B中回传的Intent
			String type = b.getString("type");// str即为回传的值"Hello, this is B speaking"
			int selection = b.getInt("selection");

			goTeachingView(type,selection);
			
			//System.out.println("type:" + type + "   selection:" + selection);
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}

	private void goTeachingView(String type, int selection) {
		((YuLeActivity) getActivity()).setTeaching(type, selection);
		((YuLeActivity) getActivity()).onViewChange(TeachingFragment.INDEX);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mWeakFragment == null) {
			mWeakFragment = new WeakReference<OrderFragment>(this);
		}
		mCustomList = new ArrayList<OrderCustom>();
		adapter = new OrderCutsomAdapter();
		mDays = new ArrayList<OrderDay>();
		mLoadDays = new LoadDays(this, mDays);
		mLoadDays.execute();
		mDayOnclick = new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Toast.makeText(getActivity(), "mDayOnclick visible cclick",
				// Toast.LENGTH_SHORT).show();

				// mOrderDetail.removeAllViews();
				isShowGrid = false;
				mGridLayout.removeAllViews();
				String tag = String.valueOf(v.getTag());
				mSelectedIndex = ((ItemDate) v).getIndex();
				// OrderFragment.this.loadHoursByDay(tag);
				// load date
				mItemVisible.turnToDowm();
				mItemVisible.setDate(mDays.get(mSelectedIndex).getMonth(),
						mDays.get(mSelectedIndex).getDay());

				mLoadTask = new LoadOrderList(getActivity(), mCustomList,
						adapter);
				mLoadTask.execute(mDays.get(mSelectedIndex).getDate());
			}
		};

		mLoadTask = new LoadOrderList(getActivity(), mCustomList, adapter);

		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				IMAGE_CACHE_DIR);

		cacheParams.setMemCacheSizePercent(getActivity(), 0.25f);
		mImageFetcher = ImageFetchFactory.getInstance().initImageFetcher(
				IMAGE_CACHE_DIR, getActivity(),
				getResources().getDimensionPixelOffset(R.dimen.listavatar));
		// mImageFetcher = new ImageFetcher(getActivity(),
		// getResources().getDimensionPixelSize(R.dimen.avatar));
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);

	}

	// private void initData() {
	// customList.add(new OrderCustom("张三", "","" ));
	// customList.add(new OrderCustom("王五", "", ""));
	// customList.add(new OrderCustom("赵六", "", ""));
	// customList.add(new OrderCustom("刘思", "", ""));
	// customList.add(new OrderCustom("张三", "", ""));
	//
	// }

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		adapter.notifyDataSetChanged();
		// mAdapter.notifyDataSetChanged();
	}

	private boolean isShowGrid = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		final DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_order, container,
				false);

		mFixedLayoutController = (LinearLayout) view
				.findViewById(R.id.order_date_controller);
		mGridLayout = (FixedGridLayout) view
				.findViewById(R.id.order_date_layout);
		mGridLayout.setCellHeight(getResources().getDimension(
				R.dimen.gridlayout_cell_height));
		System.out.println("displayMetrics.widthPixels:"
				+ displayMetrics.widthPixels);
		mGridLayout.setCellWidth(displayMetrics.widthPixels);
		mItemVisible = (ItemVisible) view.findViewById(R.id.order_indicator);

		mLeftView = view.findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_menu_titlebar);
		mRightView = view.findViewById(R.id.title_right);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.title_order);
		// mLeftView.setVisibility(View.GONE);
		mRightView.setVisibility(View.GONE);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		listView = (SwipeToDeleteListView) view.findViewById(R.id.list_order);
		listView.setAdapter(adapter);
		listView.setOnItemDeletedListener(this);
		listView.setOnItemClickListener(this);
		// listView.setOnItemLongClickListener(this);

		listView.setConfirmNeeded(true);
		listView.setDeletedViewAdapter(new MyDeletedViewAdapter(listView,
				mCustomList, mHandler));
		// listView.setOnItemDeletionConfirmedListener(this);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
				} else {
					mImageFetcher.setPauseWork(false);
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		mLoadTask.execute("");

		mItemVisible.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Toast.makeText(getActivity(), "item visible cclick",
				// Toast.LENGTH_SHORT).show();
				if (isShowGrid) {
					mGridLayout.removeAllViews();
					mGridLayout.requestLayout();
					mItemVisible.turnToDowm();
					isShowGrid = false;
				} else {
					refreshOrderDay(true);
					mGridLayout.requestLayout();
					mItemVisible.turnToUp();
					isShowGrid = true;
				}
			}
		});

		return view;
	}

	private ArrayList<OrderDay> mDays;

	private void refreshOrderDay(boolean show) {
		if (mWeakActivity.get() == null || mWeakActivity.get().isFinishing()) {
			return;
		}
		mGridLayout.removeAllViews();
		Context context = this.getActivity();
		int len = mDays.size();
		for (int i = 0; i < len; i++) {
			// System.out.println("add view index:"+i);
			OrderDay day = mDays.get(i);
			ItemDate item = new ItemDate(context, null);
			item.setMinWidth(mGridLayout.getCellWidth());
			// System.out.println("itemdate setMinWidth:"+mGridLayout.getCellWidth());
			// item.setMonth(day.getMonth());
			item.setWeekDay(day.getWeek());
			item.setDay(day.getDay());
			item.setOnClickListener(mDayOnclick);
			item.setTag(day.getDate());
			item.setIndex(i);
			item.clicked(mSelectedIndex == i);
			if (mSelectedIndex == i) {
				mItemVisible.setDate(day.getMonth(), day.getDay());
				mItemVisible.turnToDowm();
			}
			if (show) {

				mGridLayout.addView(item);
				// System.out.println("add view");
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLoadDays != null)
			mLoadDays.cancel(true);
		mLoadTask.cancel(true);
		mImageFetcher.closeCache();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			mWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			// Intent intent = new Intent(mWeakActivity.get(),
			// TextActivity.class);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			// mWeakActivity.get().startActivity(intent);
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// goToGrallery(mCustomList.get(position).getId()+"");

		OrderCustom model = mCustomList.get(position);
		goToOrderCustomDetail(model, position);

	}

	private void goToGrallery(String id) {
		Intent intent = new Intent(getActivity(), GalleryActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	@Override
	public void onItemDeletionConfirmed(SwipeToDeleteListView listView,
			int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemDeleted(SwipeToDeleteListView listView, int position) {
		// TODO Auto-generated method stub

	}

}
