package com.yulebaby.teacher.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;

public final class PhoneInfo {
	
	private static PhoneInfo phoneInfo;
	private Activity activity;
	private DisplayMetrics dm = new DisplayMetrics();
	private static int screenWidth, screenHeight;
	private WallpaperManager wallpaperManager;
	
	private PhoneInfo(Activity activity){
		this.activity = activity;
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm); 
		screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		wallpaperManager = WallpaperManager.getInstance(activity);
	}
	
	public static PhoneInfo getInstance(Activity activity){
		if(phoneInfo == null){
			return new PhoneInfo(activity);
		}
		return phoneInfo;
	}
	
	public static int getScreenWidth(){
		return screenWidth;
	}
	
	public static int getScreenHeight(){
		return screenHeight;
	}
	
	/**
	 * 换算 
	 */
	public int conversion(int math){
		return (int)(math * dm.density);
	}
	
	public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
	}
	
	/**
	 * 获取手机品牌 
	 */
	public String getBrand(){
		return android.os.Build.BRAND;
	}
	
	/**
	 * 获取手机机型
	 */
	public String getModel(){
		return android.os.Build.MODEL;
	}
	
	/**
	 * 获取手机imei
	 */
	public String getImei(){
		TelephonyManager tm = (TelephonyManager)(activity.getSystemService(Context.TELEPHONY_SERVICE));
		String imei = tm.getDeviceId();
		return imei;
	}
	
	public String getSimSerialNumber() {
		TelephonyManager tm = (TelephonyManager)(activity.getSystemService(Context.TELEPHONY_SERVICE));
		String sim = tm.getSimSerialNumber();
		return sim;
	}
	
	/**
	 * 获取手机号
	 */
	public String getPhoneNum(){
		TelephonyManager tm = (TelephonyManager)(activity.getSystemService(Context.TELEPHONY_SERVICE));
		String phoneNum = tm.getLine1Number();
//		if(phoneNum == null || phoneNum.trim().equals("")){ 
//    		phoneNum = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
//    	}
		return phoneNum;
	}
	
	/**
	 * 设置壁纸
	 */
	public boolean setWallpaper(Bitmap wallpaper){
		try {
			wallpaperManager.setBitmap(wallpaper);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 安装应用 
	 */
	public void install(String filePath){
//		String fileName = Environment.getExternalStorageDirectory() + "/lxm/a2.1.1.apk";
		String fileName = Environment.getExternalStorageDirectory() + filePath;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
		activity.startActivity(intent);
	}
	
	/**
	 * 卸载
	 */
	public void uninstall(String packageName, int requestCode){
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		activity.startActivityForResult(uninstallIntent, requestCode);
	}
	
	/**
	 * 获取sdk等级(10)
	 */
	public String getSDK(){
		return android.os.Build.VERSION.SDK;
	}
	
	/**
	 * 获取sdk版本(2.3.4)
	 */
	public String getRelease(){
		return android.os.Build.VERSION.RELEASE;
	}
	
	/**
	 * 获取屏幕宽度
	 */
	public int getWidth(){
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 */
	public int getHeight(){
		return dm.heightPixels;
	}
	
	/**
	 * 判断是否是手机号 
	 */
	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	/**
	 * 获取内存总大小
	 */
	public String getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;        
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte    
			localBufferedReader.close();
		}catch(IOException e){
			
		}
		return Formatter.formatFileSize(activity.getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化  
	}
	
	/**
	 * 获取当前可用内存大小
	 */
	public String getAvailMemory() {// 
		ActivityManager am = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);//mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(activity.getBaseContext(), mi.availMem);// 将获取的内存大小规格化  
	}
	
	/**
	 * 获取sdcard大小 
	 */
	public String getSDCardTotal(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) { 
			File sdcardDir = Environment.getExternalStorageDirectory(); 
			StatFs sf = new StatFs(sdcardDir.getPath()); 
			long blockSize = sf.getBlockSize(); 
			long blockCount = sf.getBlockCount(); 
			@SuppressWarnings("unused")
			long availCount = sf.getAvailableBlocks();
			return blockSize*blockCount/1024/1024+"MB";
//			Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB"); 
//			Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB"); 
		} 
		return null;
	}

}
