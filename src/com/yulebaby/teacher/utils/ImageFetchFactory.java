package com.yulebaby.teacher.utils;

import java.util.HashMap;

import android.content.Context;

public class ImageFetchFactory {
	
	private HashMap<String, ImageFetcher> maps = new HashMap<String, ImageFetcher>();
	private static ImageFetchFactory mFactory;
	
	private ImageFetchFactory(){}

	public static synchronized ImageFetchFactory getInstance(){
		if(mFactory == null)
			mFactory = new ImageFetchFactory();
		return mFactory;
	}
	

	public synchronized ImageFetcher initImageFetcher(String key, Context context, int imageSize){
		if(maps.get(key) != null){
			return maps.get(key);
		}
		ImageFetcher imageFetcher = new ImageFetcher(context, imageSize);
		maps.put(key, imageFetcher);
		return imageFetcher;
	}
	
	public synchronized void removeAll(){
		maps.clear();
	}
}
