package com.yulebaby.teacher.fragment;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yulebaby.teacher.DoAppraisalActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.db.YuleDBService;
import com.yulebaby.teacher.model.AppriTemplate;
import com.yulebaby.teacher.view.SwipeToDeleteListView;
import com.yulebaby.teacher.view.SwipeToDeleteListView.OnItemDeletedListener;


public class AppraTempFragment extends Fragment implements
		OnItemDeletedListener, View.OnClickListener {

	// public static final int INDEX = 210;
	private static final String TAG = "AppraTempFragment";

	private static final int DELETED_ITEM = 120;

	private static final int REFRESH_TEMPLATE = 122;

	private SwipeToDeleteListView lvAppraisal;
	List<AppriTemplate> list;
	AppraiTempAdapter mAdapter;
	YuleDBService dbService;
	//
	// private WeakReference<YuLeActivity> mWeakActivity;
	// private WeakReference<AppraTempFragment> mWeakFragment;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DELETED_ITEM:
				int id = msg.arg1;
				dbService.delete(id);
				
				break;
			case REFRESH_TEMPLATE:
				getDateTask();
				mAdapter.list = list;
				mAdapter.notifyDataSetChanged();
				lvAppraisal.invalidate();
				break;
			default:
				break;
			}
		};
	};

	boolean isQuote;

	public AppraTempFragment(boolean isQuote) {
		super();
		this.isQuote = isQuote;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// if (mWeakActivity == null) {
		// mWeakActivity = new WeakReference<YuLeActivity>(
		// (YuLeActivity) activity);
		// }
		// mWeakActivity.get().getSlidingMenu().setSlidingEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (mWeakFragment == null) {
		// mWeakFragment = new WeakReference<AppraTempFragment>(this);
		// }
		// mLoadTask = new LoadMessage(getActivity(), mMessages, mAdapter);
		list = new LinkedList<AppriTemplate>();
		dbService = new YuleDBService(getActivity());

		getDateTask();
	}

	/**
	 * 
	 * @Title: getDateTask
	 * @Description: 请求服务器获取轨迹信息
	 * @throws
	 */
	private void getDateTask() {
		// isLoading = true;
		// rlLoading.setVisibility(View.VISIBLE);
		// new Thread() {
		// public void run() {
		list = dbService.getScollDate(0, YuleDBService.MAXNUM);

		//System.out.println("get database list size:" + list.size());
		if (null != list && list.size() > 0) {
			// handler.sendEmptyMessage(SELECTDATEOK);
		} else {
			// handler.sendEmptyMessage(GET_MSGISNULL);
		}

		// };
		// }.start();
	}

	// private void initData() {
	// list.add("在澳洲的海边，你看不到房子，看不到很多的人，你唯一看到的只是大海躺在那里，海鸥在飞，海风在吹。");
	// list.add("你看不到房子，看不到很多的人，你唯一看到的只是大海躺在那里，海鸥在飞，海风在吹。");
	// list.add("看不到很多的人，你唯一看到的只是大海躺在那里，海鸥在飞，海风在吹。");
	// list.add("你唯一看到的只是大海躺在那里，海鸥在飞，海风在吹。");
	// list.add("在澳洲的海边，你看不到房子，看不到很多的人，你唯一看到的只是大海躺在那里，海鸥在飞，海风在吹。");
	//
	// }

	@Override
	public void onResume() {
		super.onResume();
		// mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(REFRESH_TEMPLATE);
		super.onStart();
		System.out.println("must go to refresh data");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_appratemp,
				container, false);

		lvAppraisal = (SwipeToDeleteListView) view
				.findViewById(R.id.lv_appraisal);
		mAdapter = new AppraiTempAdapter(getActivity(), list);
		lvAppraisal.setAdapter(mAdapter);
		lvAppraisal.setOnItemDeletedListener(this);
		if (isQuote) {
			lvAppraisal.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// 判断空，我就不判断了。。。。
					Intent data = new Intent(getActivity(),
							DoAppraisalActivity.class);
					data.putExtra("sms_content", list.get(position)
							.getContent());
					// 请求代码可以自己设置，这里设置成20
					getActivity().setResult(456, data);
					// System.out.println("return 456:"+list.get(position));
					getActivity().finish();

				}
			});
		}
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
			// getActivity().finish();
			// smWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
			// createNewFragment(new
			// TemplateAddFragment(R.layout.fragment_addtemplate));

			break;

		}
	}

	public void createNewFragment(Fragment newFragment) {
		Fragment fragment = getFragmentManager().findFragmentByTag(getTag());
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();

		fragmentTransaction.replace(fragment.getId(), newFragment, newFragment
				.getClass().getSimpleName());
		fragmentTransaction.addToBackStack(null);
		// fragmentTransaction.show(newFragment);
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}

	class AppraiTempAdapter extends BaseAdapter {
		// 定义Context
		private Context mContext;
		private LayoutInflater inflater;
		// 定义整型数组 即图片源
		private Integer[] mImageIds = {};

		private List<AppriTemplate> list;

		public AppraiTempAdapter(Context c, List<AppriTemplate> list) {

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
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.template_item, null);
				holder = new ViewHolder();
				holder.appraiText = (TextView) convertView
						.findViewById(R.id.apprais_temp);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.ivSalary.setLayoutParams(new LayoutParams(width, height))
			holder.appraiText.setText(list.get(position).getContent());

			return convertView;
		}

		class ViewHolder {
			TextView appraiText;
		}

	}

	@Override
	public void onItemDeleted(SwipeToDeleteListView listView, int position) {
		// mHandler.sendEmptyMessage(DELETED_ITEM);
		Message msg = mHandler.obtainMessage();
		msg.what = DELETED_ITEM;
		msg.arg1 = list.get(position).getId();
		mHandler.sendMessage(msg);
		
		list.remove(position);
		//mAdapter.list = list;
		mAdapter.notifyDataSetChanged();
		//lvAppraisal.invalidate();

	}

}
