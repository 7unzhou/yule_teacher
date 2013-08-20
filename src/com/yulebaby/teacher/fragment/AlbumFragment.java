package com.yulebaby.teacher.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yulebaby.teacher.GalleryActivity;
import com.yulebaby.teacher.R;
import com.yulebaby.teacher.YuLeActivity;
import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.AblumListService;
import com.yulebaby.teacher.utils.AsyncTask;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;
import com.yulebaby.teacher.utils.ImageFetchFactory;
import com.yulebaby.teacher.utils.ImageFetcher;

public class AlbumFragment extends Fragment implements View.OnClickListener {

	public static final int INDEX = 210;
	private static final String TAG = "AlbumFragment";
	private static final int SEARCH_ABLUMLIST = 1220;

	private View mLeftView, mRightView;
	private TextView tvTitle;
	private WeakReference<YuLeActivity> mWeakActivity;
	private WeakReference<AlbumFragment> mWeakFragment;

	private ArrayList<AblumModel> list;
	private ArrayList<AblumModel> searchList;
	ListView lv;
	BabyAlbumAdapter adapter;

	ImageView btnClearSearch;
	private Button btSearch;
	private EditText etSearchKey;

	LoadAlbumList mLoadTask;

	private ImageFetcher mImageFetcher;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEARCH_ABLUMLIST:
				hintSoftKeyBoard();
				searchAblumList();
				break;

			default:
				break;
			}
		};
	};

	private class LoadAlbumList extends
			AsyncTask<Object, Void, ModelData<AblumModel>> {
		private Context context;
		private ArrayList<AblumModel> mList;
		private boolean isSearch;

		// private BaseAdapter adapter;

		public LoadAlbumList(Context context, ArrayList<AblumModel> swims,
				boolean isSearch) {
			this.context = context;
			this.mList = swims;
			this.isSearch = isSearch;
			// this.adapter = adapter;
		}

		@Override
		protected ModelData<AblumModel> doInBackground(Object... params) {

			ModelData<AblumModel> swims;
			if (isSearch) {
				swims = AblumListService.searchAlbumList(context,
						String.valueOf(params[0]));
			} else {
				swims = AblumListService.albumList(context,
						String.valueOf(params[0]));
			}
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

		@Override
		protected void onPostExecute(ModelData<AblumModel> result) {
			if (mWeakActivity.get() == null
					|| mWeakActivity.get().isFinishing()) {
				return;
			}
			if (mWeakFragment.get() == null)
				return;
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
		if (mWeakActivity == null) {
			mWeakActivity = new WeakReference<YuLeActivity>(
					(YuLeActivity) activity);
		}
		mWeakActivity.get().getSlidingMenu().setSlidingEnabled(true);
	}

	protected void hintSoftKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mWeakFragment == null) {
			mWeakFragment = new WeakReference<AlbumFragment>(this);
		}

		list = new ArrayList<AblumModel>();
		searchList = new ArrayList<AblumModel>();
		adapter = new BabyAlbumAdapter(getActivity(), list);
		mLoadTask = new LoadAlbumList(getActivity(), list, false);

		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				Commons.IMAGE_CACHE_DIR);

		cacheParams.setMemCacheSizePercent(getActivity(), 0.25f);
		mImageFetcher = ImageFetchFactory.getInstance().initImageFetcher(
				Commons.IMAGE_CACHE_DIR, getActivity(),
				getResources().getDimensionPixelOffset(R.dimen.listavatar));
		// mImageFetcher = new ImageFetcher(getActivity(),
		// getResources().getDimensionPixelSize(R.dimen.avatar));
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		adapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Activity activity = this.getActivity();
		final View view = inflater.inflate(R.layout.fragment_album, container,
				false);
		mLeftView = view.findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_menu_titlebar);
		mRightView = view.findViewById(R.id.title_right);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.title_album);
		// mLeftView.setVisibility(View.GONE);
		mRightView.setVisibility(View.GONE);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		etSearchKey = (EditText) view.findViewById(R.id.edit_search);
		btSearch = (Button) view.findViewById(R.id.bt_search);
		btSearch.setOnClickListener(this);
		btnClearSearch = (ImageView) view.findViewById(R.id.btn_clean_search);
		btnClearSearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				etSearchKey.setText("");
				btnClearSearch.setVisibility(View.GONE);
				adapter.list = list;

				updateSearchHint();
				// adapter.isShowNum = false;
				adapter.notifyDataSetChanged();
				lv.invalidate();
			}
		});

		// etSearchKey.addTextChangedListener(new TextWatcher() {
		//
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// // TODO Auto-generated method stub
		// // Log.i("text changed", "start=" + start + "before" + before);
		// if (s.length() != 0) {
		// btnClearSearch.setVisibility(View.VISIBLE);
		// getSearchUser(s.toString());
		// adapter.setKeyString(etSearchKey.getText().toString());
		// adapter.list = searchList;
		// // adapter.isShowNum = true;
		// } else {
		// btnClearSearch.setVisibility(View.GONE);
		// adapter.list = list;
		// // adapter.isShowNum = false;
		// }
		// updateSearchHint();
		// adapter.notifyDataSetChanged();
		// lv.invalidate();
		// }
		//
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// // TODO Auto-generated method stub
		// // Log.i("before text changed", "start=" + start );
		//
		// }
		//
		// public void afterTextChanged(Editable s) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		lv = (ListView) view.findViewById(R.id.lv_babyalbum);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// if(is)

				Intent intent = new Intent(getActivity(), GalleryActivity.class);
				intent.putExtra("id", adapter.list.get(position).getId());
				// intent.putExtra("index", position);
				// intent.putExtra("babyname", customList.get(position).name);
				startActivity(intent);

			}
		});

		mLoadTask.execute("");

		return view;
	}

	// private void getSearchUser(String condition) {
	// searchList.clear();
	// if (condition == null || condition.equals(""))
	// return;
	// for (int i = 0; i < list.size(); i++) {
	// // System.out.println("condition:"+condition);
	// if (list.get(i).getName().contains(condition.replace(" ", ""))) {
	//
	// // System.out.println("add too search list");
	// searchList.add(list.get(i));
	// }
	// }
	// }

	public void updateSearchHint() {
		Resources res = getResources();
		// String text =
		// String.format(res.getString(R.string.album_search_hint),adapter.list.size());
		etSearchKey.setHint(res.getString(R.string.album_search_hint));
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

		case R.id.bt_search:
			mHandler.sendEmptyMessage(SEARCH_ABLUMLIST);
			// searchAblumList();

			break;
		}
	}

	private void searchAblumList() {
		String s = etSearchKey.getText().toString();
		if (s.length() != 0) {
			btnClearSearch.setVisibility(View.VISIBLE);
			adapter.setKeyString(etSearchKey.getText().toString());
			mLoadTask = new LoadAlbumList(getActivity(), searchList, true);
			mLoadTask.execute(s);
			// adapter.list = searchList;
			// adapter.isShowNum = true;
		} else {
			btnClearSearch.setVisibility(View.GONE);
			adapter.list = list;

			// adapter.isShowNum = false;
		}
		updateSearchHint();
		adapter.notifyDataSetChanged();
		lv.invalidate();

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
				holder.tvNewText = (TextView) convertView
						.findViewById(R.id.album_newcount1);
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

			mImageFetcher.loadImage(model.getImg(), holder.ivHeader);

			// String newNum = String.format(holder.strNum, model.getTotal());
			String newNum = model.getTotal();
			holder.tvTotalCount.setText(newNum);
			holder.tvNew.setVisibility(View.GONE);
			if (model.isRecent() && model.getToday() > 0) {
				holder.tvNew.setText("+" + model.getTotal());
				holder.tvNew.setVisibility(View.VISIBLE);
				holder.tvNewText.setVisibility(View.VISIBLE);
			} else {
				holder.tvNew.setVisibility(View.GONE);
				holder.tvNewText.setVisibility(View.GONE);
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
			TextView tvNewText;

			ImageView ivHeader;
			String strNum;
			// TextView timeText;
		}
	}

}
