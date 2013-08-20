package com.yulebaby.teacher.adapter;

import java.util.ArrayList;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.model.DaySalary;
import com.yulebaby.teacher.model.SalaryModel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SalaryGridAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	private LayoutInflater inflater;
	// 定义整型数组 即图片源
	private Integer[] mImageIds = {};

	private ArrayList<DaySalary> list;
	private int maxSalary = 0;

	public SalaryGridAdapter(Context c, ArrayList<DaySalary> list) {
		// this.maxSalary = maxSalary;
		this.list = list;
		mContext = c;
		inflater = LayoutInflater.from(mContext);

	}

	public void setMaxSalary(int maxSalary) {
		this.maxSalary = maxSalary;
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
			convertView = inflater.inflate(R.layout.salary_cell, null);
			holder = new ViewHolder();
			holder.dateText = (TextView) convertView.findViewById(R.id.tv_date);
			holder.ivSalary = (ImageView) convertView
					.findViewById(R.id.iv_salery);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DaySalary item = list.get(position);

		if (null == item) {
			convertView.setVisibility(View.GONE);
		} else {

			int tempIndex = item.getDay().lastIndexOf("-");
			String day = item.getDay().substring(tempIndex + 1,
					item.getDay().length());
			holder.dateText.setText(day);

			//System.out.println("max" + maxSalary);
			//System.out.println("day total:" + item.getTotal());

			if (item.getTotal() == 0 || maxSalary <= 0) {
				holder.ivSalary.setBackgroundColor(Color.TRANSPARENT);
			} else {
				GradientDrawable grad = null;
				if (item.getTotal() > 0) {

					grad = new GradientDrawable(// 渐变色
							Orientation.TOP_BOTTOM, new int[] { Color.GREEN,
									Color.YELLOW });
				} else {
					grad = new GradientDrawable(// 渐变色
							Orientation.TOP_BOTTOM, new int[] { Color.RED,
									Color.YELLOW });
				}
				// getWindow().setBackgroundDrawable(grad);//设置渐变颜色

				// holder.ivSalary.setBackgroundColor(Color.BLUE);
				holder.ivSalary.setBackgroundDrawable(grad);
				float ivHeight = (float) Math.abs(item.getTotal()) / maxSalary;
				ivHeight=  holder.ivSalary.getLayoutParams().height * ivHeight;
				//System.out.println("ivheight:"+ivHeight);
				
				holder.ivSalary.getLayoutParams().height = (int) Math.ceil(ivHeight);

			}

			// if (position % 3 == 0) {

			// System.out.println("holder.ivSalary:" +
			// holder.ivSalary.getLayoutParams().height);
			// System.out.println("convertView height: " +
			// convertView.getLayoutParams().height);

			// }
			// holder.ivSalary.setLayoutParams(new LayoutParams(width, height))
		}
		return convertView;
	}

	class ViewHolder {
		TextView dateText;
		ImageView ivSalary;
		// TextView timeText;
	}

}
