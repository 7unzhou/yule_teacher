package com.yulebaby.teacher.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.yulebaby.teacher.GalleryActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.DaySalary;
import com.yulebaby.teacher.model.SalaryDetailModel;
import com.yulebaby.teacher.model.SalaryMonthModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.AblumListService;
import com.yulebaby.teacher.server.SalaryService;
import com.yulebaby.teacher.utils.AsyncTask;
import com.yulebaby.teacher.utils.ImageFetchFactory;
import com.yulebaby.teacher.utils.ImageFetcher;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SalaryCommissionFragment extends Fragment implements
		View.OnClickListener {
	private static final String TAG = "SalaryCommissionFragment";


	private ArrayList<DaySalary> mList;
	ListView lv;
	CommissionAdapter adapter;

	private int listDividerHeight = 5;

	LoadContentList mLoadTask;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			default:
				break;
			}
		};
	};

	private class LoadContentList extends
			AsyncTask<Object, Void, ModelData<DaySalary>> {
		private Context context;
		private ArrayList<DaySalary> mList = new ArrayList<DaySalary>();

		// private boolean isSearch;

		// private BaseAdapter adapter;

		public LoadContentList(Context context) {
			this.context = context;
			// this.mList = swims;
			// this.isSearch = isSearch;
			// this.adapter = adapter;
		}

		@Override
		protected ModelData<DaySalary> doInBackground(Object... params) {
			ModelData<DaySalary> model = null;
			if (typeLoad.equals(Commons.COMMISSION)) {

				model = SalaryService.commission(context,
						SalaryCommissionFragment.this.params.get("month")
								.toString());
			}
			if (typeLoad.equals(Commons.DETAIL)) {
				model = SalaryService.dayDetail(context,
						SalaryCommissionFragment.this.params.get("day")
								.toString());
			}

			return model;
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

		@Override
		protected void onPostExecute(ModelData<DaySalary> result) {

			if (result == null) {
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
				mList.clear();
				// if(!isSearch){
				// }
				mList.addAll(result.getModels());
				System.out.println(" result listlenght:" + mList.size());
				adapter.list = mList;
				adapter.notifyDataSetChanged();
				// int tempH = adapter.getItemHeight();
				// tempH = tempH+ mList.size()*5;
				setListViewHeight(mList.size());
				lv.invalidate();
				// System.out.println("lv height:" +
				// lv.getLayoutParams().height);
				// lv.getLayoutParams().height;
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

	ContentValues params;
	String strDay;
	String typeLoad;

	public SalaryCommissionFragment(ContentValues params, String type) {
		// this.strMonth = month;
		// this.strDay
		this.typeLoad = type;
		this.params = params;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList = new ArrayList<DaySalary>();
		adapter = new CommissionAdapter(getActivity(), mList);
		mLoadTask = new LoadContentList(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_salarycommission,
				container, false);

		lv = (ListView) view.findViewById(R.id.lv_commission);
		lv.setAdapter(adapter);

		// mLeftView = view.findViewById(R.id.title_left);
		// mRightView = view.findViewById(R.id.title_right);
		// tvTitle = (TextView) view.findViewById(R.id.tv_title);
		// tvTitle.setText(R.string.title_album);
		// // mLeftView.setVisibility(View.GONE);
		// mRightView.setVisibility(View.GONE);
		// mLeftView.setOnClickListener(this);
		// mRightView.setOnClickListener(this);

		mLoadTask.execute();

		return view;
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

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			// mWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			// Intent intent = new Intent(mWeakActivity.get(),
			// TextActivity.class);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			// mWeakActivity.get().startActivity(intent);
			break;

		}
	}

//	private void setListViewHeight(int paramInt1, int paramInt2) {
//		// item的高和个数乘机
//		float f1 = paramInt1 * paramInt2;
//		// DisplayMetrics获取屏幕分辨率
//		DisplayMetrics metrics = new DisplayMetrics();
//		getActivity().getWindowManager().getDefaultDisplay()
//				.getMetrics(metrics);
//		float f2 = metrics.scaledDensity;
//		int height = (int) (f1 * f2);
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT, height);
//		lv.setLayoutParams(layoutParams);
//	}

	private void setListViewHeight(int itemLenght) {
		int totalHeight = 0;
		for (int i = 0, len = itemLenght; i < len; i++) {
			View listItem = adapter.getView(i, null, lv);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int list_child_item_height = listItem.getMeasuredHeight()
					+ lv.getDividerHeight();
			// Log.w("ListView Child Height", ">>> child height="+
			// list_child_item_height);
			totalHeight += list_child_item_height; // 统计所有子项的总高度
		}
		lv.getLayoutParams().height = totalHeight - listDividerHeight;
		// System.out.println("ListView height :" +totalHeight);
	}

	private class CommissionAdapter extends BaseAdapter {
		// 定义Context
		private Context mContext;
		private LayoutInflater inflater;
		// 定义整型数组 即图片源
		// private Integer[] mImageIds = {};

		private int itemHeight;
		public ArrayList<DaySalary> list;

		public int getItemHeight() {
			return itemHeight;
		}

		public CommissionAdapter(Context c, ArrayList<DaySalary> list) {

			this.list = list;
			mContext = c;
			inflater = LayoutInflater.from(mContext);

		}

		// 获取图片的个数
		public int getCount() {
			return list.size();
		}

		// 获取图片在库中的位置
		public Object getItem(int position) {
			return position;
		}

		// 获取图片ID
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// ImageView imageView;
			if (null == list || list.size() == 0)
				return null;
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.commission_item, null);
				holder = new ViewHolder();

				holder.tvName = (TextView) convertView
						.findViewById(R.id.item_salary_name);
				holder.tvTotalCount = (TextView) convertView
						.findViewById(R.id.item_salary_count);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			DaySalary model = list.get(position);

			holder.tvName.setText(model.getDay());
			holder.tvTotalCount.setText(model.getTotal() + "");

			// System.out.println("itemHeight:" + convertView.getHeight());
			return convertView;
		}



		class ViewHolder {
			TextView tvTitle;
			TextView tvName;
			TextView tvTotalCount;

			// TextView timeText;
		}
	}
}
