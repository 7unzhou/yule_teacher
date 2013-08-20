package com.yulebaby.teacher.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.model.ExamAnswer;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AnswerAdapter extends BaseAdapter {
	// 定义Context
	private Context mContext;
	private LayoutInflater inflater;
	// 定义整型数组 即图片源
	// private Integer[] mImageIds = {};

	public static Map<String, Boolean> isSelected;
	// private static Map<Integer, Integer> solutionMap;
	// private static Map<Integer, Integer> myAnswerMap;
	private ArrayList<ExamAnswer> list;
	private String correctIndex = "", myIndex = "";

	public void setList(ArrayList<ExamAnswer> l) {
		this.list = l;
		init();
	}

	public AnswerAdapter(Context c, ArrayList<ExamAnswer> list) {

		this.list = list;
		mContext = c;
		inflater = LayoutInflater.from(mContext);
		init();
	}

	public void setSolution(String corretIndex, String myIndex) {
		this.correctIndex = corretIndex;
		this.myIndex = myIndex;
		// this.solutionMap = answerMap;
		// this.myAnswerMap = myAnswerMap;
		System.out.println("correctIndex:" + corretIndex);
		System.out.println("myIndex:" + myIndex);
	}

	// 初始化
	public void init() {
		// 这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。
		if (null == isSelected) {

			isSelected = new HashMap<String, Boolean>();
		}
		for (int i = 0; i < list.size(); i++) {
			isSelected.put(list.get(i).getId(), false);
		}
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
			convertView = inflater.inflate(R.layout.answer_item, null);
			holder = new ViewHolder();
			holder.cb = (CheckBox) convertView
					.findViewById(R.id.answer_item_cb);
			holder.tv = (TextView) convertView
					.findViewById(R.id.answer_item_tv);
			holder.tvCorrrect = (TextView) convertView
					.findViewById(R.id.answer_item_correct);
			holder.tvError = (TextView) convertView
					.findViewById(R.id.answer_item_error);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cb.setChecked(isSelected.get(list.get(position).getId()));

		holder.tv.setText(list.get(position).getAnswerText());
		if (!TextUtils.isEmpty(correctIndex) && !TextUtils.isEmpty(myIndex)) {
			String anId = list.get(position).getId();
			// System.out.println("anID:" + anId);
			// System.out.println("correctIndex:" + correctIndex);
			// System.out.println("myIndex:" + myIndex);
			if (correctIndex.equals(anId)) {
				if (correctIndex == myIndex) {
					// holder.tvCorrrect.setVisibility(View.VISIBLE);
					holder.tvError.setVisibility(View.GONE);
					holder.cb.setChecked(true);
					System.out.println("setCorrect for my choice");
				}

				holder.tvCorrrect.setVisibility(View.VISIBLE);
				System.out.println("tvCorrrect for my visible");
			}
			if (myIndex.equals(anId) && !myIndex.equals(correctIndex)) {
				holder.tvError.setVisibility(View.VISIBLE);
				holder.cb.setChecked(true);
				// holder.cb.setButtonDrawable(errorbg)
				System.out.println("tvError for my visible");
			} else if (myIndex.equals(anId)) {
				holder.cb.setChecked(true);
			}

		} else {
			holder.tvCorrrect.setVisibility(View.GONE);
			holder.tvError.setVisibility(View.GONE);
		}

		return convertView;
	}

	public class ViewHolder {
		public TextView tv = null;
		public CheckBox cb = null;
		public TextView tvCorrrect;
		public TextView tvError;
	}

}
