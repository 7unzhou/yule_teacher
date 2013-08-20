/**
 * Copyright 2013 Yoann Delouis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yulebaby.teacher.adapter;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.yulebaby.teacher.R;
import com.yulebaby.teacher.model.Commons;
import com.yulebaby.teacher.model.OrderCustom;
import com.yulebaby.teacher.view.SwipeToDeleteListView;

public class MyDeletedViewAdapter implements
		SwipeToDeleteListView.DeletedViewAdapter {
	private SwipeToDeleteListView listView;
	List<OrderCustom> items;
	private Handler handler;

	public MyDeletedViewAdapter(SwipeToDeleteListView listView,
			List<OrderCustom> items, Handler mhandler) {
		this.listView = listView;
		this.items = items;
		this.handler = mhandler;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(listView.getContext(),
					R.layout.view_deletedview, null);
		}
		view.findViewById(R.id.deletedView_cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						listView.cancelDeletion(position);
					}
				});
		view.findViewById(R.id.tv_order_camera).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Message msg = handler.obtainMessage();
						msg.arg1 = position;
						msg.what = Commons.TAKE_POHOTO;
						handler.sendMessage(msg);

					}
				});

		view.findViewById(R.id.tv_order_detail).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Message msg = handler.obtainMessage();
						msg.arg1 = position;
						msg.what = Commons.ORDER_DETAIL;
						handler.sendMessage(msg);

					}
				});
		view.findViewById(R.id.tv_order_studt).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						Message msg = handler.obtainMessage();
						msg.arg1 = position;
						msg.what = Commons.ORDER_STUDT;
						handler.sendMessage(msg);

					}
				});

		return view;
	}
}
