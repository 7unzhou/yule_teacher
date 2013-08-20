package com.yulebaby.teacher;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.component.slidemenu.SlidingMenu;
import com.yulebaby.teacher.fragment.MenuFragment;
import com.yulebaby.teacher.fragment.TeachingFragment;
import com.yulebaby.teacher.model.AblumModel;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.ExamModel;
import com.yulebaby.teacher.model.ExamPaperModel;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.model.ReLessonModel;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.ExamService;
import com.yulebaby.teacher.server.LessonService;
import com.yulebaby.teacher.utils.AsyncTask;
import com.yulebaby.teacher.utils.ImageFetcher;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderCustomDetailActivity extends FragmentActivity implements
		View.OnClickListener {
	protected static final String TAG = "OrderCustomDetailActivity";

	private static final int SET_CUSTOMVIEW = 130;

	private static final int SET_LESSONVIEW = 131;

	private View mLeftView;
	private TextView tvTitle;
	private Button mRightView;
	OrderCustom ordercustom;
	private ImageView avater;
	private ImageFetcher mImageFetcher;

	int showType;
	ListView lessonLv;
	private ArrayList<ReLessonModel> lessonList;

	private Context context;
	View btCustom, btLesson, btPhoto;
	View customView, lessonView;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_LESSONVIEW:
				btCustom.setBackgroundResource(R.drawable.bg_tabbar_normal);
				btLesson.setBackgroundResource(R.drawable.bg_tabbar_select);
				setLessonInfo();
				if (lessonList == null || lessonList.size() <= 0) {
					new LessonLoad(context).execute();
				}
				break;
			case SET_CUSTOMVIEW:
				btCustom.setBackgroundResource(R.drawable.bg_tabbar_select);
				btLesson.setBackgroundResource(R.drawable.bg_tabbar_normal);
				setCustomInfo();
				break;

			default:
				break;
			}
		}
	};

	private class LessonLoad extends
			AsyncTask<Object, Void, ModelData<ReLessonModel>> {
		private Context context;
		private ArrayList<ReLessonModel> list;

		// private BaseAdapter adapter;

		public LessonLoad(Context context) {
			this.context = context;
		}

		@Override
		protected ModelData<ReLessonModel> doInBackground(Object... params) {
			ModelData<ReLessonModel> model = LessonService.lessList(context,
					ordercustom.getId() + "");
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
		protected void onPostExecute(ModelData<ReLessonModel> result) {
			// if (mWeakActivity.get() == null
			// || mWeakActivity.get().isFinishing()) {
			// return;
			// }
			// if (mWeakFragment.get() == null)
			// return;
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

				// mList.clear();
				lessonList.addAll(result.getModels());
				System.out.println("return result listlenght:"
						+ lessonList.size());
				LessonAdapter adapter = new LessonAdapter(context, lessonList);
				lessonLv.setAdapter(adapter);
				// adapter.list = lessonList;
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_customdetail);
		context = this;
		ImageCacheParams cacheParams = new ImageCacheParams(this,
				Commons.IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(this, 0.25f);
		mImageFetcher = new ImageFetcher(this, getResources()
				.getDimensionPixelSize(R.dimen.avatar));
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.addImageCache(this.getSupportFragmentManager(),
				cacheParams);

		lessonList = new ArrayList<ReLessonModel>();
		ordercustom = (OrderCustom) getIntent().getExtras().getSerializable(
				"data");

		showType = getIntent().getExtras().getInt("show_type");

		// new LoadExamPaper(this).execute();
		mLeftView = findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		mRightView = (Button) findViewById(R.id.title_right);
		tvTitle = (TextView) findViewById(R.id.tv_title);

		// examTitle = getIntent().getExtras().getString("exam_title");
		// exampaperId = getIntent().getExtras().getString("exampaper_id");
		tvTitle.setText(R.string.custom_title);

		// tvTitle.setText(R.string.title_salary);
		// mLeftView.setVisibility(View.GONE);
		mRightView.setVisibility(View.GONE);
		// mRightView.setText("相机");
		// mRightView.setBackgroundResource(R.drawable.selector_bt_icon_photo);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);

		customView = findViewById(R.id.ll_custonview);
		lessonView = findViewById(R.id.ll_lessonview);
		lessonLv = (ListView) findViewById(R.id.lv_studentplan);
		lessonLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(context,"go to " +
				// lessonList.get(position).getTitle(),Toast.LENGTH_SHORT).show();
				ReLessonModel leeson = lessonList.get(position);
				goTeachingView(leeson.getType(), leeson.getId());
				// lessonList.get(position).getType();
			}
		});

		TextView tvName = (TextView) findViewById(R.id.tv_name);
		tvName.setText(ordercustom.getName());
		
		
		TextView tvNick = (TextView) findViewById(R.id.tv_nick);
		tvNick.setText(ordercustom.getNick());
		
		
		TextView tvMonthage = (TextView) findViewById(R.id.tv_monthage);
		tvMonthage.setText(ordercustom.getMonthAge()+"");
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(ordercustom.getReserveDate());
		sb.append("  ");
		sb.append(ordercustom.getReserveTime());
		TextView tvTime = (TextView) findViewById(R.id.tv_reservetime);
		tvTime.setText(sb.toString());

		TextView tvType = (TextView) findViewById(R.id.tv_babytype);
		tvType.setText(ordercustom.getBabyType());
		
		TextView tvRemarks = (TextView) findViewById(R.id.tv_remarks);
		tvRemarks.setText(ordercustom.getRemarks());
		
//		sb.append(ordercustom.getRemainTime());
//		sb.append("\n");
//		sb.append("小名：");
//		sb.append(ordercustom.getNick());
//		sb.append("   ");
//		sb.append("月龄：");
//		sb.append(ordercustom.getMonthAge());
//		sb.append("  类型：");
//		sb.append(ordercustom.getBabyType());
//		sb.append("\n");
//		sb.append(ordercustom.getRemarks());

//		TextView tvCustomInfo = (TextView) findViewById(R.id.tv_info);
//		tvCustomInfo.setText(sb.toString());

		avater = (ImageView) findViewById(R.id.iv_header);
		mImageFetcher.loadImage(ordercustom.getImgUrl(), avater);
		btCustom = findViewById(R.id.bt_custominfo);
		btLesson = findViewById(R.id.bt_lessoninfo);
		btPhoto = findViewById(R.id.bt_photoinfo);

		btPhoto.setOnClickListener(this);
		btCustom.setOnClickListener(this);
		btLesson.setOnClickListener(this);
		// startTimer();

		if (showType == Commons.ORDER_STUDT) {
			mHandler.sendEmptyMessage(SET_LESSONVIEW);
		} else {
			setCustomInfo();

		}

	}

	protected void goTeachingView(int type, int selection) {
		Intent aintent = new Intent(OrderCustomDetailActivity.this,
				YuLeActivity.class);
		/* 将数据打包到aintent Bundle 的过程略 */
		String teachingType = TeachingFragment.TYPE__CHINESE;
		// }
		if (type == 1) {
			teachingType = TeachingFragment.TYPE__BILINGUAL;
		}
		if (type == 2) {
			teachingType = TeachingFragment.TYPE__WATER;
		}
		aintent.putExtra("selection", selection);
		aintent.putExtra("type", teachingType);
		setResult(9998, aintent);

		finish();

	}

	private void setCustomInfo() {
		customView.setVisibility(View.VISIBLE);
		lessonView.setVisibility(View.GONE);
	}

	private void setLessonInfo() {
		lessonView.setVisibility(View.VISIBLE);
		customView.setVisibility(View.GONE);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			// mWeakActivity.get().toggle(SlidingMenu.LEFT);
			break;
		case R.id.title_right:
		case R.id.bt_photoinfo:
			Intent intent = new Intent(this, GalleryActivity.class);
			intent.putExtra("id", ordercustom.getId() + "");
			startActivity(intent);
			finish();
			break;

		case R.id.bt_custominfo:
			mHandler.sendEmptyMessage(SET_CUSTOMVIEW);

			break;
		case R.id.bt_lessoninfo:

			mHandler.sendEmptyMessage(SET_LESSONVIEW);

			break;

		// case R.id.bt_photoinfo:
		//
		// //mHandler.sendEmptyMessage(set_photo);
		// Intent intent = new Intent(this, GalleryActivity.class);
		// intent.putExtra("id", ordercustom.getId() + "");
		// startActivity(intent);
		// break;

		}
	}

	private class LessonAdapter extends BaseAdapter {
		// 定义Context
		private Context mContext;
		private LayoutInflater inflater;
		// 定义整型数组 即图片源
		// private Integer[] mImageIds = {};

		public ArrayList<ReLessonModel> list;

		public LessonAdapter(Context c, ArrayList<ReLessonModel> list) {

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
				convertView = inflater.inflate(R.layout.lesson_item, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.album_item_title);
				holder.tvLessonTitle = (TextView) convertView
						.findViewById(R.id.tv_lessontitle);
				holder.tvLessonType = (TextView) convertView
						.findViewById(R.id.tv_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ReLessonModel model = list.get(position);

			// holder.tvTitle.setVisibility(View.GONE);
			if (position == 0) {
				if (model.isPre()) {
					holder.tvTitle.setText(R.string.lesson_pre);
				} else {
					holder.tvTitle.setText(R.string.lesson_last);
				}
				holder.tvTitle.setVisibility(View.VISIBLE);
			}
			if (position > 0) {
				if (model.isPre() == list.get(position - 1).isPre()) {
					holder.tvTitle.setVisibility(View.GONE);
				} else {
					if (model.isPre()) {
						holder.tvTitle.setText(R.string.lesson_pre);
					} else {
						holder.tvTitle.setText(R.string.lesson_last);
					}
					holder.tvTitle.setVisibility(View.VISIBLE);
				}
			}

			holder.tvLessonTitle.setText(model.getTitle());
			holder.tvLessonType.setText(model.getTypeName());

			return convertView;
		}

		class ViewHolder {
			TextView tvTitle;
			TextView tvLessonType;
			TextView tvLessonTitle;
			// TextView timeText;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mImageFetcher.closeCache();
	}
}
