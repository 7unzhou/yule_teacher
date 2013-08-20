package com.yulebaby.teacher.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.model.BabyAlbum;
import com.yulebaby.teacher.model.ExamModel;
import com.yulebaby.teacher.model.InteractionModel;
import com.yulebaby.teacher.model.OrderCustom;

public class OrderCutsomAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	private LayoutInflater inflater;
	// 定义整型数组 即图片源
	// private Integer[] mImageIds = {};

	public ArrayList<OrderCustom> list;

	public OrderCutsomAdapter(Context c, ArrayList<OrderCustom> list) {

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
		if (null ==list || list.size() == 0)
			return null;
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.customorder_item, null);
			holder = new ViewHolder();
			holder.ivHeader = (ImageView) convertView
					.findViewById(R.id.custom_header);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_customname);
			holder.tvContent = (TextView) convertView.findViewById(R.id.order_content);
			holder.tvSurplusTime = (TextView) convertView.findViewById(R.id.order_surplus_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		OrderCustom model = list.get(position);

		
		holder.tvName.setText(model.getName());
		//holder.tvContent.setText(text)
		
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
