/**
 * wechatdonal
 */
package com.donal.superne.app.config;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.donal.superne.app.Superne;

/**
 * wechat
 *
 * @author donal
 */
public interface AppActivitySupport
{

    /**
     * 获取BaseApplication.
     */
    public abstract Superne getSuperne();

    /**
     * 终止服务.
     */
    public abstract void stopService();

    /**
     * 开启服务.
     */
    public abstract void startService();

    /**
     * 校验网络-如果没有网络就弹出设置,并返回true.
     *
     * @return
     */
    public abstract boolean validateInternet();

    /**
     * 校验网络-如果没有网络就返回true.
     *
     * @return
     */
    public abstract boolean hasInternetConnected();

    /**
     *
     */
    public void openWirelessSet();

    /**
     * 退出应用.
     */
    public abstract void isExit();

    /**
     * 判断GPS是否已经开启.
     *
     * @return
     */
    public abstract boolean hasLocationGPS();

    /**
     * 判断基站是否已经开启.
     *
     * @return
     */
    public abstract boolean hasLocationNetWork();

    /**
     * 检查内存卡.
     */
    public abstract void checkMemoryCard();

    /**
     * 显示toast.
     *
     * @param text    内容
     * @param longint 内容显示多长时间
     */
    public abstract void showToast(String text, int longint);

    /**
     * 短时间显示toast.
     *
     * @param text
     */
    public abstract void showToast(String text);

    /**
     * @param message
     */
    public abstract void showDialog(String message);

    /**
     * @param message
     * @param positive
     * @param negative
     * @param dialogClickListener
     */
    public abstract void showDialog(String message, String positive, String negative, DialogClickListener dialogClickListener);

    /**
     * 获取进度条.
     *
     * @return
     */
    public abstract ProgressDialog getProgressDialog();

    /**
     * 返回当前Activity上下文.
     *
     * @return
     */
    public abstract Context getContext();

    /**
     * 获取当前登录用户的SharedPreferences配置.
     *
     * @return
     */
    public SharedPreferences getLoginUserSharedPre();


    /**
     * 发出Notification的method.
     *
     * @param iconId       图标
     * @param contentTitle 标题
     * @param contentText  你内容
     * @param activity
     */
    public void setNotiType(int iconId, String contentTitle,
                            String contentText, Class activity, String from);

    public interface DialogClickListener
    {
        public void ok();

        public void cancel();
    }

}
