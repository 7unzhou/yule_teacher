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
import com.yulebaby.teacher.model.SalaryDetailModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.AblumListService;
import com.yulebaby.teacher.server.SalaryService;
import com.yulebaby.teacher.utils.AsyncTask;
import com.yulebaby.teacher.utils.ImageFetchFactory;
import com.yulebaby.teacher.utils.ImageFetcher;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SalaryDetailFragment extends Fragment implements
		View.OnClickListener {
	private static final String TAG = "SalaryDetailFragment";

	private View mLeftView, mRightView;
	private TextView tvTitle;

	ListView lv;
	BabyAlbumAdapter adapter;

	ImageView btnClearSearch;
	private Button btSearch;
	private EditText etSearchKey;

	LoadContentList mLoadTask;

	private ArrayList<SalaryDetailModel> mList;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			default:
				break;
			}
		};
	};

	String strDay;

	public SalaryDetailFragment(String strDay) {
		this.strDay = strDay;
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

			//ModelData<SalaryDetailModel> model = SalaryService.salaryDetail(context, strDay);
			//ModelData<SalaryDetailModel> model = SalaryService.salaryDetail(context, strDay);

			return null;
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
				// adapter.list = mList;
				// adapter.notifyDataSetChanged();
				// lv.invalidate();
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
		final View view = inflater.inflate(R.layout.fragment_salarydetail,
				container, false);
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

	private class BabyAlbumAdapter extends BaseAdapter {
		// 定义Context
		private Context mContext;
		private LayoutInflater inflater;
		// 定义整型数组 即图片源
		// private Integer[] mImageIds = {};

		public ArrayList<AblumModel> list;

		public BabyAlbumAdapter(Context c, ArrayList<AblumModel> list) {

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
				convertView = inflater.inflate(R.layout.babyalbum_item, null);
				holder = new ViewHolder();
				holder.ivHeader = (ImageView) convertView
						.findViewById(R.id.iv_album_header);
				holder.tvName = (TextView) convertView
						.findViewById(R.id.album_babyname);
				holder.tvTotalCount = (TextView) convertView
						.findViewById(R.id.album_totalcount);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.album_item_title);
				holder.tvNew = (TextView) convertView
						.findViewById(R.id.album_newcount);
				holder.strNum = getResources().getString(R.string.ablum_num);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AblumModel model = list.get(position);

			// holder.tvTitle.setVisibility(View.GONE);
			if (position == 0) {
				if (model.isRecent()) {
					holder.tvTitle.setText(R.string.ablum_recent);
				} else {
					holder.tvTitle.setText(R.string.ablum_least);
				}
				holder.tvTitle.setVisibility(View.VISIBLE);
			}
			if (position > 0) {
				if (model.isRecent() == list.get(position - 1).isRecent()) {
					holder.tvTitle.setVisibility(View.GONE);
				} else {
					if (model.isRecent()) {
						holder.tvTitle.setText(R.string.ablum_recent);
					} else {
						holder.tvTitle.setText(R.string.ablum_least);
					}
					holder.tvTitle.setVisibility(View.VISIBLE);
				}
			}


			String newNum = String.format(holder.strNum, model.getTotal());
			holder.tvTotalCount.setText(newNum);
			holder.tvNew.setVisibility(View.GONE);
			if (model.isRecent() && model.getToday() > 0) {
				holder.tvNew.setText("+" + model.getTotal());
				holder.tvNew.setVisibility(View.VISIBLE);
			}
			// if (model.getToday() > 0) {
			// holder.tvNew.setText(model.newCount + "");
			// holder.tvNew.setVisibility(View.VISIBLE);
			// } else {
			// holder.tvNew.setVisibility(View.GONE);
			// }

			// System.out.println("search key :" + keyString);
			if (!TextUtils.isEmpty(keyString)
					&& model.getName().contains(keyString)) {

				// System.out.println(" is contain key");
				int index = model.getName().indexOf(keyString);
				int len = keyString.length();
				Spanned temp = Html.fromHtml(model.getName()
						.substring(0, index)
						+ "<u><font color=#f9bbbb>"
						+ model.getName().substring(index, index + len)
						+ "</font></u>"
						+ model.getName().substring(index + len,
								model.getName().length()));

				holder.tvName.setText(temp);

			} else {
				holder.tvName.setText(model.getName());
			}

			return convertView;
		}

		String keyString;

		public void setKeyString(String key) {
			this.keyString = key;
		}

		class ViewHolder {
			TextView tvTitle;
			TextView tvName;
			TextView tvTotalCount;
			TextView tvNew;
			ImageView ivHeader;
			String strNum;
			// TextView timeText;
		}
	}
}
