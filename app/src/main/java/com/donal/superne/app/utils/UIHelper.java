/**
 * 
 */
package com.donal.superne.app.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import com.donal.superne.app.BaseApplication;

/**
 * @author Donal Tong 
 * momoka
 * 2012-12-26
 */
public class UIHelper {
	public static final int WEIXIN_TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x01;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x02;
	
	public final static int LISTVIEW_COUNT = 10;
	
	public final static int MOMOKA_TEXT_FONTSSIZE = 13;
	public final static int LISTVIEW_EMOTION_SIZE = 20;
	
	/**
	 * 显示进度条
	 * 
	 * @param context
	 *            环境
	 * @param title
	 *            标题
	 * @param message
	 *            信息
	 * @return
	 */
//	public static ProgressDialog showProgress(final Context context,
//			CharSequence title, CharSequence message) {
//		try {
//			if (context == null) {
//				return null;
//			}
//			final ProgressDialog dialog = new ProgressDialog(context);
//			dialog.setTitle(title);
//			dialog.setMessage(message);
//			dialog.setCancelable(false);
//			dialog.show();
//			return dialog;
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	public static ProgressDialog showProgress(Context context,
//			CharSequence title, CharSequence message,
//			boolean cancelable) {
//		try {
//			if (context == null) {
//				return null;
//			}
//			final ProgressDialog dialog = new ProgressDialog(context);
//			dialog.setTitle(title);
//			dialog.setMessage(message);
//			dialog.setCancelable(cancelable);
//			new CountDownTimer(10000,1000) {
//				@Override
//				public void onTick(long millisUntilFinished) {
//
//				}
//				public void onFinish() {
//					try {
//						dialog.dismiss();
//	                } catch (Exception e) {
//
//	                }
//				}
//			}.start();
//			dialog.show();
//			return dialog;
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	public static ProgressDialog dismissProgress(ProgressDialog progressDialog) {
//		try {
//			if (progressDialog != null) {
//				progressDialog.dismiss();
//				progressDialog = null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return progressDialog;
//	}

	/**
	 * 清除app缓存
	 * @param activity
	 */
	public static void clearAppCache(Activity activity)
	{
		final BaseApplication ac = (BaseApplication)activity.getApplication();
		final Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {

			}
		};
		new Thread(){
			public void run() {
				android.os.Message msg = new android.os.Message();
				try {				
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
	            	msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
}
