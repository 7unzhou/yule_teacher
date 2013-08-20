package com.yulebaby.teacher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yulebaby.teacher.application.App;
import com.yulebaby.teacher.model.AblumMember;
import com.yulebaby.teacher.model.YPhoto;
import com.yulebaby.teacher.net.ModelData;
import com.yulebaby.teacher.net.Response;
import com.yulebaby.teacher.server.AblumListService;
import com.yulebaby.teacher.server.AlMemberService;
import com.yulebaby.teacher.utils.AsyncTask;
import com.yulebaby.teacher.utils.ImageCache.ImageCacheParams;
import com.yulebaby.teacher.utils.ImageFetcher;
import com.yulebaby.teacher.utils.ImageWorker;

public class GalleryActivity extends FragmentActivity implements
		AdapterView.OnItemClickListener, View.OnClickListener {
	protected static final String TAG = "GalleryActivity";
	private static final String IMAGE_CACHE_DIR = "avatars";
	protected static final int UPDATE_TEXTVIEW = 120;
	private Button mLeftView, mRightView;
	private TextView tvTitle;

	// private OrderCustom mSwim;
	private int mIndex;
	private ImageView avater;
	TextView name;
	GridView mGridView;

	private TextView tvUploadCout;
	private TextView tvPhotoCount;

	// private ImageFetcher mImageFetcher;
	// private ArrayList<AblumMember> members;
	private AblumMember member;
	private String memberId = "1470";

	private ArrayList<YPhoto> mAlbums;
	private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageAdapter mAdapter;
	private ImageFetcher mImageFetcher;
	private LoadAlums mLoadTask;

	Bitmap photoBm;
	String FileName;

	private Button picButton;
	AlertDialog.Builder alertDialog;
	Context dialogContext;
	ListAdapter digAdapter;
	private final static File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	// File mFile;
	File uploadFile;
	private final static File POI_IMAGE_TMP = new File(
			Environment.getExternalStorageDirectory() + "/uploadtemp");

	private final static int TAKE_PHOTO_CAMERA = 1000;
	private final static int TAKE_PHOTO_GRALLERY = 1001;
	private final static int CROP_PHOTO = 10002;

	File mCurrentPhotoFile;

	View updateView;
	ImageView ivPohoto;

	View rotateLeft, rotateRight;

	// String examTitle;
	private class LoadAlums extends
			AsyncTask<Object, Void, ModelData<AblumMember>> {
		private Context context;
		// private ArrayList<AblumMember> members;
		private ArrayList<YPhoto> albums;
		private BaseAdapter adapter;

		public LoadAlums(Context context, ArrayList<YPhoto> albums,
				BaseAdapter adapter) {
			this.context = context;
			this.albums = mAlbums;
			this.adapter = adapter;
			// this.members = members;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mEmptyView.setVisibility(View.GONE);
		}

		@Override
		protected ModelData<AblumMember> doInBackground(Object... params) {
			ModelData<AblumMember> albums = null;
			if (isUploadPhoto) {
				System.out.println("AblumListService.uploadSubmit");
				albums = AblumListService.uploadSubmit(context, memberId, "",
						uploadFile);
			} else {
				System.out.println("AblumListService.albumMember");
				albums = AlMemberService.albumMember(context, memberId);
			}

			return albums;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(ModelData<AblumMember> result) {
			// if(mWeakActivity.get() == null &&
			// mWeakActivity.get().isFinishing()){
			// return;
			// }
			// if(mWeakFragment.get() == null){
			// return;
			// }
			boolean isShow = isUploadPhoto;
			isUploadPhoto = false;
			if (result == null) {
				Toast.makeText(context,
						getResources().getString(R.string.submit_fail),
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "onPostExecute result == null");
				return;
			}
			// mEmptyView.setVisibility(View.VISIBLE);
			switch (result.getResultCode()) {
			case Response.NotLogin:
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT)
						.show();
				break;
			case Response.OK:
				if (isShow) {
					GalleryActivity.this.finish();
					return;
				}
				if (result.getModels().size() > 0) {
					// mEmptyView.setVisibility(View.GONE);
				}
				member = result.getModels().get(0);
				mAlbums.addAll(member.getPhotoList());
				adapter.notifyDataSetChanged();
				mHandler.sendEmptyMessage(UPDATE_TEXTVIEW);
				System.out.println("result.getModels().get(0).getPhotoList():"
						+ result.getModels().get(0).getPhotoList().size());
				// adapter = new ImageAdapter(GalleryActivity.this);
				// mGridView.setAdapter(adapter);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ImageCacheParams cacheParams = new ImageCacheParams(this,
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(this, 0.25f);
		mImageFetcher = new ImageFetcher(this, getResources()
				.getDimensionPixelSize(R.dimen.avatar));
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.addImageCache(this.getSupportFragmentManager(),
				cacheParams);
		// mSwim = (OrderCustom) getIntent().getSerializableExtra("data");
		memberId = getIntent().getStringExtra("id");
		// mIndex = getIntent().getIntExtra("index", -1);

		mImageThumbSize = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_spacing);
		mAdapter = new ImageAdapter(this);
		// members = new ArrayList<AblumMember>();
		mAlbums = new ArrayList<YPhoto>();
		mLoadTask = new LoadAlums(this, mAlbums, mAdapter);

		setContentView(R.layout.activity_gallery);
		avater = (ImageView) this.findViewById(R.id.iv_custom_header);
		name = (TextView) this.findViewById(R.id.tv_customname);

		rotateLeft = findViewById(R.id.bt_rotateleft);
		rotateRight = findViewById(R.id.bt_rotateright);
		rotateLeft.setOnClickListener(this);
		rotateRight.setOnClickListener(this);

		mLeftView = (Button) findViewById(R.id.title_left);
		mLeftView.setBackgroundResource(R.drawable.icon_back_titlebar);
		mRightView = (Button) findViewById(R.id.title_right);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		// examTitle = getIntent().getExtras().getString("babyname");
		tvTitle.setText(R.string.title_album);
		// tvTitle.setText(R.string.title_salary);
		// mLeftView.setVisibility(View.GONE);
		mLeftView.setOnClickListener(this);
		mRightView.setOnClickListener(this);
		mRightView.setVisibility(View.INVISIBLE);

		tvUploadCout = (TextView) findViewById(R.id.tv_newuploadcount);
		tvPhotoCount = (TextView) findViewById(R.id.tv_photocount);

		updateView = findViewById(R.id.rl_updateview);
		findViewById(R.id.bt_updatepohoto).setOnClickListener(this);
		ivPohoto = (ImageView) findViewById(R.id.iv_pohotoview);

		mGridView = (GridView) findViewById(R.id.gridView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView,
					int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					mImageFetcher.setPauseWork(true);
				} else {
					mImageFetcher.setPauseWork(false);
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (mAdapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(mGridView
									.getWidth()
									/ (mImageThumbSize + mImageThumbSpacing));
							if (numColumns > 0) {
								final int columnWidth = (mGridView.getWidth() / numColumns)
										- mImageThumbSpacing;
								mAdapter.setNumColumns(numColumns);
								mAdapter.setItemHeight((int) (columnWidth * 1.2));
								if (BuildConfig.DEBUG) {
									Log.d(TAG,
											"onCreateView - numColumns set to "
													+ numColumns);
								}
							}
						}
					}
				});
		mLoadTask.execute();

		dialogContext = new ContextThemeWrapper(this,
				android.R.style.Theme_Light);
		String[] choices = getResources().getStringArray(R.array.choices);

		digAdapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);
		PHOTO_DIR.mkdir();
		POI_IMAGE_TMP.mkdir();
		// this.tempFile = new File(Environment.getExternalStorageDirectory()+
		// "/poitmp/" + "userheadtmp.jpg");
		// mFile = new File(POI_IMAGE_TMP, getFileName());
		uploadFile = null;

		picButton = (Button) findViewById(R.id.bt_uploadphoto);
		picButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePhotoDialog();
			}
		});

	}

	private void takePhotoDialog() {
		alertDialog = new AlertDialog.Builder(dialogContext);
		alertDialog.setSingleChoiceItems(digAdapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: {
							if (Environment.getExternalStorageState().equals(
									Environment.MEDIA_MOUNTED)) {
								doTakePhoto();
							} else {
								Toast.makeText(getApplicationContext(),
										"没有SDcard", Toast.LENGTH_LONG).show();
							}
							break;
						}
						case 1: {
							pickFromGrallery();
							break;
						}
						}
					}

				});
		alertDialog.create().show();
	}

	@Override
	public void onPause() {
		// Log.d(TAG, "ImageGridFragment onPause");
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		// 计时器暂停
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (avater != null) {
			ImageWorker.cancelWork(avater);
			avater.setImageDrawable(null);
		}
		mImageFetcher.closeCache();
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TEXTVIEW:
				mImageFetcher.loadImage(member.getImgUrl(), avater);

				name.setText(member.getName());

				String text = getResources().getString(R.string.photo_total);
				text = text.format(text, member.getTotal());
				tvPhotoCount.setText(text);
				tvUploadCout.setText(member.getToday() + "");
				// .setText(count + "");
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Message msg = mHandler.obtainMessage();
		switch (id) {
		case R.id.title_left:
			if (!isCancel) {

				this.finish();
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			} else {
				setUploadHide();
			}
			break;
		case R.id.title_right:

			takePhotoDialog();
			// Intent intent = new Intent(mWeakActivity.get(),
			// TextActivity.class);
			// intent.putExtra(TextActivity.INDEX, TextActivity.INDEX_FEEDBACK);
			// mWeakActivity.get().startActivity(intent);
			break;

		case R.id.bt_rotateleft:
			left();
			break;
		case R.id.bt_rotateright:
			right();
			break;

		case R.id.bt_updatepohoto:
			uploadPhoto();
			break;

		}
	}

	boolean isUploadPhoto;

	private void uploadPhoto() {
		Toast.makeText(this, "开始上传照片长度：" + uploadFile.length() + " byte",
				Toast.LENGTH_SHORT).show();
		mLoadTask.cancel(true);
		isUploadPhoto = true;
		mLoadTask = new LoadAlums(this, mAlbums, mAdapter);
		mLoadTask.execute();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// // TODO Auto-generated method stub

	}

	private class ImageAdapter extends BaseAdapter {

		private final Context mContext;
		private int mItemHeight = 0;
		private int mNumColumns = 0;
		private int mActionBarHeight = 0;
		private GridView.LayoutParams mImageViewLayoutParams;

		public ImageAdapter(Context context) {
			super();
			mContext = context;
			mImageViewLayoutParams = new GridView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}

		@Override
		public int getCount() {
			return mAlbums.size() + mNumColumns;
		}

		@Override
		public Object getItem(int position) {
			return position < mNumColumns ? null : mAlbums.get(
					position - mNumColumns).getSmallUrl();
		}

		@Override
		public long getItemId(int position) {
			return position < mNumColumns ? 0 : position - mNumColumns;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return (position < mNumColumns) ? 1 : 0;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		class ViewHolder {
			ImageView avatarImage;
			// ImageView tagImage;
			TextView title;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			if (position < mNumColumns) {
				if (convertView == null) {
					convertView = new View(mContext);
				}
				convertView.setLayoutParams(new AbsListView.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
				return convertView;
			}

			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(GalleryActivity.this)
						.inflate(R.layout.gallery_fragment_item, null);
				holder = new ViewHolder();
				holder.avatarImage = (ImageView) convertView
						.findViewById(R.id.gallery_img);
				// holder.tagImage = (ImageView)
				// convertView.findViewById(R.id.gallery_tag);
				holder.title = (TextView) convertView
						.findViewById(R.id.gallery_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.tagImage.setVisibility(View.INVISIBLE);
			// if(isShowFlag){
			// if(!mAlbums.get(position - mNumColumns).isRead()){
			// holder.tagImage.setVisibility(View.VISIBLE);
			// }
			// }

			holder.title.setText(mAlbums.get(position - mNumColumns).getTime());
			convertView.setLayoutParams(mImageViewLayoutParams);
			mImageFetcher.loadImage(mAlbums.get(position - mNumColumns)
					.getSmallUrl(), holder.avatarImage);
			return convertView;
		}

		public void setItemHeight(int height) {
			if (height == mItemHeight) {
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new GridView.LayoutParams(
					LayoutParams.MATCH_PARENT, mItemHeight);
			mImageFetcher.setImageSize(height);
			notifyDataSetChanged();
		}

		public void setNumColumns(int numColumns) {
			mNumColumns = numColumns;
		}

		public int getNumColumns() {
			return mNumColumns;
		}
	}

	// 从照相机获取图片
	private void doTakePhoto() {
		try {

			if (!PHOTO_DIR.exists()) {
				boolean iscreat = PHOTO_DIR.mkdirs();// 创建照片的存储目录
				Log.e("GalleryActivity", "" + iscreat);
			}
			FileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, FileName);
			// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,
			// null);
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, TAKE_PHOTO_CAMERA);
		} catch (Exception e) {
			Log.e("UserInfoHeadManagerActivity", "fail to connect Camera", e);
		}
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	// 从相册获取
	private void pickFromGrallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra("return_data", true);
		startActivityForResult(intent, TAKE_PHOTO_GRALLERY);

	}

	// private String getFileName() {
	// SimpleDateFormat format = new SimpleDateFormat(
	// "'IMG'_yyyy_MM_dd_HH_mm_ss");
	// return format.format(new Date(System.currentTimeMillis())) + ".jpg";
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case TAKE_PHOTO_CAMERA: {
			setUploadView();
			System.out.println("return from TAKE_PHOTO_CAMERA");
			// Bitmap bm =
			// BitmapFactory.decodeFile(uploadFile.getAbsolutePath());
			File tempFile = new File(PHOTO_DIR, FileName);
			// uploadFile = compressImage(tempFile);
			compressImage(tempFile);
			uploadFile = tempFile;
			if (tempFile.exists()) {
				// 此外，你还可以使用BitmapFactory的decodeResource方法获得一个Bitmap对象
				// 使用decodeResource方法时传入的是一个drawable的资源id
				// 还有一个decodeStream方法，这个方法传入一个图片文件的输入流即可！
				BitmapFactory.Options options = new BitmapFactory.Options();
				// 宽高为原来的二分之一，即图片为原来的四分之一
				options.inSampleSize = 2;// 图片大小，设置越大，图片越不清晰，占用空间越小
				photoBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(),options);
				// 设置ImageView的显示图片
				ivPohoto.setImageBitmap(photoBm);
				//photoBm.recycle();
			} else {
				Toast.makeText(this, "文件不存在！", Toast.LENGTH_SHORT).show();
				return;
			}

			break;
		}
		case TAKE_PHOTO_GRALLERY: {

			setUploadView();
			updateView.setVisibility(View.VISIBLE);

			Uri uri = data.getData();
			// uri content://media/external/images/media/516
			// poi_photo.setImageURI(uri);
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			cursor.moveToFirst();
			String imgPath = cursor.getString(1); // 图片文件路径
			cursor.close();
			// POI_IMAGE_TMP.mkdir();
			System.out.println("return from TAKE_PHOTO_GRALLERY");
			try {
				// uploadFile = compressImage(new File(imgPath));
				compressImage(new File(imgPath));
				uploadFile = new File(imgPath);
				// Bitmap bm =
				// BitmapFactory.decodeFile(uploadFile.getAbsolutePath());

				BitmapFactory.Options options = new BitmapFactory.Options();
				// 宽高为原来的二分之一，即图片为原来的四分之一
				options.inSampleSize = 2;// 图片大小，设置越大，图片越不清晰，占用空间越小

				photoBm = BitmapFactory.decodeFile(imgPath, options);

				ivPohoto.setImageBitmap(photoBm);
				//photoBm.recycle();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(GalleryActivity.this, "err_pohototype_msg",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	boolean isCancel;
	private void setUploadView() {
		updateView.setVisibility(View.VISIBLE);
		isCancel = true;
		//mLeftView.setText(R.string.dialog_cancel);
		mRightView.setText(R.string.dialog_replace);
		mRightView.setVisibility(View.VISIBLE);
	}

	private void setUploadHide() {
		updateView.setVisibility(View.GONE);
		isCancel = false;
		//mLeftView.setText(R.string.title_bt_back);
		mRightView.setVisibility(View.GONE);
		// mRightView.setText(R.string.dialog_replace);
	}

	public File compressImage(File file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		// 缩放比
		int be = (int) (options.outWidth / (float) 1090);
		System.out.println("suofangbi:" + be);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		if (null == bitmap) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w + "   " + h);
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			File f = new File(POI_IMAGE_TMP, file.getName());
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(f);
				if (f.getName().toUpperCase().endsWith("PNG")) {
					bitmap.compress(CompressFormat.PNG, 80, fos);
				} else if (f.getName().toUpperCase().endsWith("JPEG")) {
					bitmap.compress(CompressFormat.JPEG, 80, fos);
				} else {
					bitmap.compress(CompressFormat.JPEG, 80, fos);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//bitmap.recycle();
			return f;
		} else {
			Toast.makeText(GalleryActivity.this, "R.string.nosdcard",
					Toast.LENGTH_LONG).show();
			return null;
		}

	}

	private float scaleW = 1;// 横向缩放系数，1表示不变
	private float scaleH = 1;// 纵向缩放系数，1表示不变
	private float curDegrees = 90;// 当前旋转度数

	// 左转事件处理
	private void left() {
		int bmpW = photoBm.getWidth();
		int bmpH = photoBm.getHeight();
		// 设置图片放大比例
		double scale = 1;
		// 计算出这次要放大的比例
		scaleW = (float) (scaleW * scale);
		scaleH = (float) (scaleH * scale);

		// 产生reSize后的Bitmap对象
		// 注意这个Matirx是android.graphics底下的那个
		Matrix mt = new Matrix();
		mt.postScale(scaleW, scaleH);
		// 设置旋转角度
		// 如果是设置为0则表示不旋转
		// 设置的数是负数则向左转
		// 设置的数是正数则向右转
		mt.setRotate(curDegrees = curDegrees - 90);
		Bitmap resizeBmp = null;
		try {

			resizeBmp = Bitmap
					.createBitmap(photoBm, 0, 0, bmpW, bmpH, mt, true);
			ivPohoto.setImageBitmap(resizeBmp);

		} catch (Exception e) {
			// photoBm = compressImage(file)
			// resizeBmp = Bitmap.createBitmap(photoBm, 0, 0, bmpW, bmpH,
			// mt,true);

			System.out.println("图片选择 error");
		}
	}

	// 右转事件处理
	private void right() {
		int bmpW = photoBm.getWidth();
		int bmpH = photoBm.getHeight();
		// 设置图片放大比例
		double scale = 1;
		// 计算出这次要放大的比例
		scaleW = (float) (scaleW * scale);
		scaleH = (float) (scaleH * scale);

		// 产生reSize后的Bitmap对象
		// 注意这个Matirx是android.graphics底下的那个
		Matrix mt = new Matrix();
		mt.postScale(scaleW, scaleH);
		mt.setRotate(curDegrees = curDegrees + 90);
		Bitmap resizeBmp = Bitmap.createBitmap(photoBm, 0, 0, bmpW, bmpH, mt,
				true);

		ivPohoto.setImageBitmap(resizeBmp);
	}
}
