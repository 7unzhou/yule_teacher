package com.yulebaby.teacher.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.fragment.ExamFragment;
import com.yulebaby.teacher.model.ExamModel;

public class ExamAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	private LayoutInflater inflater;
	private Handler mHandler;
	// 定义整型数组 即图片源
	// private Integer[] mImageIds = {};

	private ArrayList<ExamModel> list;

	public ExamAdapter(Context c, ArrayList<ExamModel> list, Handler mHandler) {

		this.list = list;
		mContext = c;
		inflater = LayoutInflater.from(mContext);
		this.mHandler = mHandler;

	}

	public void setList(ArrayList<ExamModel> l) {
		this.list = l;
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
			convertView = inflater.inflate(R.layout.exam_list, null);
			holder = new ViewHolder();
			holder.dateText = (TextView) convertView
					.findViewById(R.id.tv_exam_date);
			// holder.ivState = (ImageView) convertView
			// .findViewById(R.id.iv_exam_state);
			holder.examTitle = (TextView) convertView
					.findViewById(R.id.tv_exam_name);
			holder.score = (TextView) convertView
					.findViewById(R.id.tv_exam_score);
			// holder.strShengcheng = mContext.getResources().getString(
			// R.string.exam_createtime);
			// holder.strCore = mContext.getResources().getString(
			// R.string.exam_core);
			holder.btExaming = (Button) convertView
					.findViewById(R.id.bt_examing);
			holder.tvState = (TextView) convertView
					.findViewById(R.id.tv_exam_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ExamModel model = list.get(position);
		// String strCtime =
		// String.format(holder.strShengcheng,model.getCreateTime());
		// holder.dateText.setText(strCtime);
		holder.dateText.setText(model.getCreateTime());
		holder.examTitle.setText(model.getType());
		// holder.state.setText(model.state);

		if (!model.isExaming()) {
			ColorStateList csl = (ColorStateList) mContext.getResources()
					.getColorStateList(R.color.scoregray);
			holder.score.setTextColor(csl);
			holder.score.setText("?");
			holder.tvState.setText("未作答");
			holder.btExaming.setText("答题");
			holder.btExaming.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Message msg = mHandler.obtainMessage();
					msg.obj = model;
					msg.what = ExamFragment.DOING_EXAM;
					mHandler.sendMessage(msg);

				}
			});
			// holder.ivState.setBackgroundColor(Color.YELLOW);
		} else {
			// String strSc = String.format(holder.strCore, model.getScore());
			float passScore = model.getTotalScore() * 0.6f;
			if (model.getScore() < passScore) {
				ColorStateList csl = (ColorStateList) mContext.getResources()
						.getColorStateList(R.color.scorered);

				holder.score.setTextColor(csl);
				holder.tvState.setText("未通过");

				// holder.btExaming.setBackgroundResource(R.drawable.bt_blue_small_normal);
			} else {
				ColorStateList csl = (ColorStateList) mContext.getResources()
						.getColorStateList(R.color.scoregreen);

				holder.score.setTextColor(csl);
				holder.tvState.setText("已通过");
			}
			// ColorStateList csl = (ColorStateList) mContext.getResources()
			// .getColorStateList(R.color.scoregray);
			ColorStateList csl = (ColorStateList) mContext.getResources()
					.getColorStateList(android.R.color.background_dark);
			holder.btExaming.setTextColor(csl);
			// holder.btExaming.setTextColor(android.R.color.darker_gray);
			holder.btExaming.setText("回顾");
			holder.btExaming.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Message msg = mHandler.obtainMessage();
					msg.obj = model;
					msg.what = ExamFragment.REVIEW_EXAM;
					mHandler.sendMessage(msg);

				}
			});
			holder.score.setText(model.getScore() + "");

			// holder.ivState.setBackgroundColor(Color.GRAY);
		}

		// holder.ivSalary.setLayoutParams(new LayoutParams(width, height))

		return convertView;
	}

	class ViewHolder {
		TextView dateText;
		TextView examTitle;
		TextView score;
		TextView tvState;
		Button btExaming;
		// TextView timeText;
	}

}
