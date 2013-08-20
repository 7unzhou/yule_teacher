package com.yulebaby.teacher.fragment;

import java.util.ArrayList;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.DaySalary;
import com.yulebaby.teacher.model.SalaryDetailModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.SalaryService;
import com.yulebaby.teacher.utils.AsyncTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SalaryOhterFragment extends Fragment implements
		View.OnClickListener {
	private static final String TAG = "SalaryOhterFragment";

	// private View mLeftView, mRightView;
	// private TextView tvTitle;

	ListView lv;
	private ArrayList<SalaryDetailModel> mList;
	DetailAdapter adapter;

	LoadContentList mLoadTask;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			default:
				break;
			}
		};
	};

	// String strMonth;
	String loadType;
	ContentValues salaryParams;

	public SalaryOhterFragment(ContentValues params, String type) {
		this.loadType = type;
		this.salaryParams = params;
	}

	private class LoadContentList extends
			AsyncTask<Object, Void, ModelData<SalaryDetailModel>> {
		private Context context;
		private ArrayList<SalaryDetailModel> mList;

		// private BaseAdapter adapter;

		public LoadContentList(Context context,
				ArrayList<SalaryDetailModel> swims) {
			this.context = context;
			this.mList = swims;
			// this.adapter = adapter;
		}

		@Override
		protected ModelData<SalaryDetailModel> doInBackground(Object... params) {

			String month = salaryParams.get("month").toString();
			ModelData<SalaryDetailModel> model = SalaryService.salaryDetail(
					context, month, loadType);

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
		protected void onPostExecute(ModelData<SalaryDetailModel> result) {

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
				System.out.println("return result listlenght:" + mList.size());
				adapter.list = mList;
				adapter.notifyDataSetChanged();
				//setListViewHeight(mList.size());
				lv.invalidate();
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList = new ArrayList<SalaryDetailModel>();
		adapter = new DetailAdapter(getActivity(), mList);
		mLoadTask = new LoadContentList(getActivity(), mList);

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

		mLoadTask.execute("");

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

	}

//	private int listDividerHeight = 5;
//	private void setListViewHeight(int itemLenght) {
//		System.out.println("itemLenght:"+itemLenght);
//		int totalHeight = 0;
//		for (int i = 0, len = itemLenght; i < len; i++) {
//			View listItem = adapter.getView(i, null, lv);
//			listItem.measure(0, 0); // 计算子项View 的宽高
//			int list_child_item_height = listItem.getMeasuredHeight()
//					+ lv.getDividerHeight();
//			// Log.w("ListView Child Height", ">>> child height="+
//			// list_child_item_height);
//			totalHeight += list_child_item_height; // 统计所有子项的总高度
//		}
//		lv.getLayoutParams().height = totalHeight - listDividerHeight;
//		// System.out.println("ListView height :" +totalHeight);
//	}

	private class DetailAdapter extends BaseAdapter {
		// 定义Context
		private Context mContext;
		private LayoutInflater inflater;
		// 定义整型数组 即图片源
		// private Integer[] mImageIds = {};

		public ArrayList<SalaryDetailModel> list;

		public DetailAdapter(Context c, ArrayList<SalaryDetailModel> list) {

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
				convertView = inflater.inflate(R.layout.detail_salary_item,
						null);
				holder = new ViewHolder();

				holder.tvContent = (TextView) convertView
						.findViewById(R.id.detailitem_content);
				holder.tvDay = (TextView) convertView
						.findViewById(R.id.detailitem_day);
				holder.tvReason = (TextView) convertView
						.findViewById(R.id.detailitem_reson);
				holder.tvWage = (TextView) convertView
						.findViewById(R.id.detailitem_wage);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SalaryDetailModel model = list.get(position);

			holder.tvContent.setText(model.getComment());
			holder.tvDay.setText(model.getWorkDate());
			holder.tvReason.setText(model.getReason());
			holder.tvWage.setText(model.getWages() + "");
			return convertView;
		}

		class ViewHolder {
			TextView tvDay;
			TextView tvReason;
			TextView tvContent;
			TextView tvWage;
		}
	}
}
