package app;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.FileDownloadConfiguration.Builder;

import com.anke.vedio.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *itations under the License.
 *******************************************************************************/


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import cn.bmob.v3.BmobConfig;
import rongyun.DemoContext;
import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;
import okhttp.OkHttpUtils;
import okio.Buffer;

public class BaseApp extends Application {
	/**
	 */
	public static String APPID = "6e33c5d8aa716849ba1e3fe944fa628d";
	private String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
            "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
            "DTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFowRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
            "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3\n" +
            "DQEBAQUAA4GNADCBiQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb2\n" +
            "9bSotqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/XgY5iFqv6\n" +
            "D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGLMB8GA1UdIwQYMBaAFHle\n" +
            "tne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDov\n" +
            "LzE5Mi4xNjguOS4xNDkvY3JsMS5jcmwwCwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Pt\n" +
            "x1GITGOFLABDWDANBgkqhkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV\n" +
            "23XQ96HU8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQenvQ\n" +
            "og555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n" +
            "-----END CERTIFICATE-----";
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		iniBmob();
//		initConfig(getApplicationContext());
//		Bmob.initialize(getApplicationContext(), APPID);
		OkHttpUtils.getInstance().setCertificates(new InputStream[]{
                new Buffer()
                        .writeUtf8(CER_12306)
                        .inputStream()});
        OkHttpUtils.getInstance().debug("testDebug").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
		initFileDownloader();
		initRongYun();
	}
	private void iniBmob() {
		//提供以下两种方式进行初始化操作：
//		//第一：设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
		BmobConfig config =new BmobConfig.Builder(this)
//		//设置appkey
		.setApplicationId(APPID)
//		//请求超时时间（单位为秒）：默认15s
		.setConnectTimeout(30)
//		//文件分片上传时每片的大小（单位字节），默认512*1024
		.setUploadBlockSize(1024*1024)
//		//文件的过期时间(单位为秒)：默认1800s
		.setFileExpiration(5500)
		.build();
		Bmob.initialize(config);
	}
	private void initRongYun() {
		if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
				"io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(getApplicationContext());
			if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
				DemoContext.init(this);
			}
		}
	}
	/**
	 * 获得当前进程的名字
	 *
	 * @param context
	 * @return
	 */
	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
	private void initFileDownloader() {
		 // 1.create FileDownloadConfiguration.Builder
		 Builder builder = new FileDownloadConfiguration.Builder(this);
		 // 2.config FileDownloadConfiguration.Builder
		 builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
				 "FileDownloader"); // config the download path
		 // builder.configFileDownloadDir("/storage/sdcard1/FileDownloader");
		 // allow 3 download tasks at the same time
		 builder.configDownloadTaskSize(3);
		 // config retry download times when failed
		 builder.configRetryDownloadTimes(5);
		 // enable debug mode
		 //builder.configDebugMode(true);
		 // config connect timeout
		 builder.configConnectTimeout(25000); // 25s
		 // 3.init FileDownloader with the configuration
		 FileDownloadConfiguration configuration = builder.build(); // build FileDownloadConfiguration with the builder
		 FileDownloader.init(configuration);
	    }
//	    // release FileDownloader
//	 private void releaseFileDownloader() {
//	        FileDownloader.release();
//	}
	 @Override
	    public void onTerminate() {
	        super.onTerminate();
	        // release FileDownloader
//	        releaseFileDownloader();
	    }
	/**
	 * 初始化文件配置
	 * @param context
	 */
	public static void initConfig(Context context) {
//		BmobConfiguration config = new BmobConfiguration.Builder(context).customExternalCacheDir("Smile").build();
//		BmobPro.getInstance(context).initConfig(config);
	}
	


	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(60 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}