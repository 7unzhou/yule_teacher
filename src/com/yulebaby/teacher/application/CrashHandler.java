package com.yulebaby.teacher.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;




import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author zhoujunznhou
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static final String ErrorReportName = "errorlog.txt";
    /** 错误报告的文件长度 500K */
    private static final long FILELEGTH = 1024 * 500;

    private static String VERSION_NAME = "versionName: ";
    private static String VERSION_CODE = "versionCode: ";
    private static String STACK_TRACE = "STACK_TRACE: ";
    private static String PACKAGENAME = "packageName: ";
    private static String TIME = "Error_Time: ";
    /** 基带 */
    private static String BASEBAND = "baseBand: ";
    /** 设备名 */
    private static String MODEL = "model: ";
    /** 设备厂商名 */
    private static String BRAND = "brand: ";
    /** 设备的SDK版本号 */
    private static String SDKVERSION = "sdkversion: ";

    private static StringBuilder crashinfo;

    private static CrashHandler crashhandler;

    private CrashHandler() {
    };

    public static CrashHandler getInstance() {
        if (crashhandler == null) {
            crashhandler = new CrashHandler();
        }
        return crashhandler;
    }

    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        crashinfo = new StringBuilder();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.i("CrashHandler", "come into uncaughtException");
        // TODO Auto-generated method stub
        if (!handleException(ex) && null != mDefaultHandler) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    private boolean handleException(final Throwable ex) {
        Log.i("CrashHandler", "come into handleException");

        if (null == ex) {
            return true;
        }
        final String msg = ex.getLocalizedMessage();
        // 使用Toast来显示异常信息
        // 除了Toast外，还可以选择使用Notification来显示错误内容并让用户选择是否提交错误报告而不是自动提交。
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出错了：" + msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            };
        }.start();

        collectCrashDeviceInfo(mContext);
        saveCrashInfoToFile(ex);

        sendCrashReportsToServer(mContext);

        return true;
    }

    /**
     * 把错误报告发送给服务器.
     * 
     * @param ctx
     */
    public void sendCrashReportsToServer(Context ctx) {

        Log.i("CrashHandler", "come into sendCrashReportToServer");

        final File cr = new File(ctx.getFilesDir(), ErrorReportName);
        if (cr.length() >= FILELEGTH) {
            Log.i("CrashHandler", "sendCrashReportToServer");
            new Thread() {
                public void run() {
                    postReport(cr);
                    cr.delete(); // 删除已发送的报告
                };
            }.start();
        }

    }

    private void postReport(File file) {

        /*
         * DefaultHttpClient httpClient = new DefaultHttpClient();
         * HttpPost httpPost = new HttpPost(G.URL);
         * List<NameValuePair> nvps = new ArrayList<NameValuePair>();
         * nvps.add(new BasicNameValuePair("package_name", G.APP_PACKAGE));
         * nvps.add(new BasicNameValuePair("package_version", version));
         * nvps.add(new BasicNameValuePair("phone_model", phoneModel));
         * nvps.add(new BasicNameValuePair("android_version", androidVersion));
         * nvps.add(new BasicNameValuePair("stacktrace", stacktrace));
         * httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
         * // We don't care about the response, so we just hope it went
         * // well and on with it
         * httpClient.execute(httpPost);
         */
    }

    /**
     * 获取错误报告文件名
     * 
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx) {
        File filesDir = ctx.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(ErrorReportName);
            }
        };
        return filesDir.list(filter);
    }

    /**
     * 保存错误信息到文件中
     * 
     * @param ex
     * @return
     */
    private void saveCrashInfoToFile(Throwable ex) {

        Log.i("CrashHandler", "saveCrashInfoToFile");
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        crashinfo.append(STACK_TRACE + result + "\n \n");
        Log.i("STACK_TRACE", result);
        try {
            FileOutputStream trace = mContext.openFileOutput(ErrorReportName, Context.MODE_APPEND);
            trace.write(crashinfo.toString().getBytes());
            trace.flush();
            trace.close();

        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
        }
    }

    /**
     * 写入错误信息到异常日志
     * 
     * @param exString 要记录的错误信息
     */
    public void writeToErrorFile(String exString) {
        try {
            FileOutputStream trace = mContext.openFileOutput(ErrorReportName, Context.MODE_APPEND);
            trace.write(exString.getBytes());
            trace.flush();
            trace.close();
        } catch (Exception e) {
            Log.e(TAG, "An error occured while write To ErrorFile", e);
        }
    }

    /**
     * 收集程序崩溃的设备信息
     * 
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        Log.i("CrashHandler", "come into collectCrashDeviceInfo");
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                crashinfo.append(TIME + getTime() + "\n");
                crashinfo.append(VERSION_NAME + (pi.versionName == null ? "not set" : pi.versionName) + "\n");
                crashinfo.append(VERSION_CODE + String.valueOf(pi.versionCode) + "\n");
                crashinfo.append(PACKAGENAME + pi.packageName + "\n");
                crashinfo.append(BRAND + (Build.BRAND == null ? "unknown" : Build.BRAND) + "\n");
                crashinfo.append(MODEL + (Build.MODEL == null ? "unknown" : Build.MODEL) + "\n");
                crashinfo.append(BASEBAND + getBaseBand() + "\n");
                crashinfo.append(SDKVERSION + (Build.VERSION.RELEASE == null ? "unknown" : Build.VERSION.RELEASE)
                        + "\n");
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
    }

    private String getTime() {
        Date now = new Date();
        DateFormat format = DateFormat.getDateTimeInstance();
        String time = format.format(now);
        return time;
    }

    /**
     * 获取手机基带信息
     * 
     * @return String 基带信息
     */
    private static String getBaseBand() {
        String baseband = "unknow";
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Object invoker = clazz.newInstance();
            Method method = clazz.getMethod("get", new Class[] { String.class, String.class });
            Object result = method.invoke(invoker, new Object[] { "gsm.version.baseband", "no message" });
            baseband = (String) result;
            // Toast.makeText(this, (String)result, Toast.LENGTH_LONG).show();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return baseband;
    }

}
