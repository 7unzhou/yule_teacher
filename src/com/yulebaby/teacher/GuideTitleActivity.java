package com.yulebaby.teacher;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class GuideTitleActivity extends Activity implements OnItemClickListener, OnClickListener {

	public static final String EXTRA_DATA = "index";
	private ArrayList<String> mTitles;
	private ListView mGuideListView;
	private GuideAdapter mAdapter;
	private int mIndex = 0;
	private View mLeftView,mRightView;
	
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_fragment);
		mIndex = getIntent().getIntExtra(EXTRA_DATA, 0);
		mTitles = (ArrayList<String>)getIntent().getSerializableExtra("data");
		mGuideListView = (ListView)this.findViewById(R.id.guide_list);
		mAdapter = new GuideAdapter(this);
		mGuideListView.setAdapter(mAdapter);
		mGuideListView.setOnItemClickListener(this);
		mLeftView = findViewById(R.id.title_left);
		mRightView = findViewById(R.id.title_right);
		mLeftView.setOnClickListener(this);
		mLeftView.setBackgroundResource(R.drawable.selector_button_titlebar_menu);
		mRightView.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATA, position);
		this.setResult(RESULT_OK, intent);
		this.finish();
	}
	
	class GuideAdapter extends BaseAdapter {
		
		private int size;
		private Context context;

		public GuideAdapter(Context context) {
			size = mTitles.size();
			this.context = context;  
		}
		
		@Override
		public int getCount() {
			return size;
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			size = mTitles.size();
		}
		
		@Override
		public void notifyDataSetInvalidated() {
			super.notifyDataSetInvalidated();
			size = mTitles.size();
		}

		@Override
		public Object getItem(int position) {
			return mTitles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		class ViewHolder {
            TextView titleText;
            TextView numberText;
        }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.guide_item, null);
				holder = new ViewHolder();
                holder.titleText = (TextView) convertView.findViewById(R.id.guide_title);
                holder.numberText = (TextView) convertView.findViewById(R.id.guide_number);
                convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
//			if(position + 1 == mIndex){
//				convertView.setBackgroundColor(Color.RED);
//			}else{
//				convertView.setBackgroundResource(R.drawable.list);
//			}
			holder.titleText.setText(mTitles.get(position));
			holder.numberText.setText(String.valueOf(position + 1));
			return convertView;
		}
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.title_left:
			this.finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;
		}
	}
}
