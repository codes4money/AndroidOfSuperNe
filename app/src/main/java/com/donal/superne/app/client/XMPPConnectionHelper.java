package com.donal.superne.app.client;

import android.content.Context;

import com.lidroid.xutils.util.LogUtils;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLError;
import org.jivesoftware.smack.sasl.SASLErrorException;

import java.io.IOException;

/**
 * Created by jianghaofeng on 14-7-5.
 */
public class XMPPConnectionHelper extends Thread
{

    /**
     * 最大连接次数
     */
    private static final int MAX_IDLE_BACKOFF = 10;

    /**
     * Max retries after for authentication error.
     */
    private static final int MAX_AUTH_ERRORS = 3;


    /**
     * Retry enabled flag.
     */
    protected boolean mRetryEnabled = true;

    private final Context mContext;

    protected XMPPConnection mConn;

    private ConnectionHelperListener mListener;
    private boolean                  mConnecting;
    private int                      mRetryCount;

    public XMPPConnectionHelper(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public synchronized void start()
    {
        mConnecting = true;
        super.start();
    }

    private void connectOnce(String userName, String password) throws IOException, XMPPException, SmackException
    {
        if (mConn != null)
        {
            try
            {
                mConn.disconnect();
            }
            catch (SmackException.NotConnectedException e)
            {
                e.printStackTrace();
            }
            mConn = null;
        }

        if (mConn == null || !mConn.isConnected())
        {
            mConn = new SuperneConnection();
            if (mListener != null)
            {
                mConn.addConnectionListener(mListener);
                mListener.created(mConn);
            }
        }
        mConn.connect();
        if (mListener != null)
        {
            mConn.addConnectionListener(mListener);
            mListener.connected(mConn);
        }
        mConn.login(userName, password, "android");
//        if (mListener != null)
//        {
//            mListener.authenticated(mConn);
//        }
    }

    @Override
    public void run()
    {
        connect();
    }

    public void connect()
    {
        String userName = Preferences.getPrefUserName();
        String password = Preferences.getPrefPassword();

        if (userName == null && password == null)
        {
            if (mListener != null)
                mListener.aborted(null);
            return;
        }
        while (mConnecting)
        {
            try
            {
                connectOnce(userName, password);
                mRetryCount = 0;
                break;
            }
            catch (Exception ie)
            {
                if (mConnecting)
                {
                    try
                    {
                        mConn.disconnect();
                    }
                    catch (Exception e)
                    {
                        // ignored
                    }
                    mConn = null;

                    if (ie instanceof SASLErrorException && ((SASLErrorException) ie)
                            .getSASLFailure().getSASLError() == SASLError.not_authorized &&
                            mRetryCount >= MAX_AUTH_ERRORS)
                    {

                        if (mListener != null)
                        {
                            mListener.authenticationFailed();
                            break;
                        }
                    }

                    if (mRetryEnabled)
                    {
                        try
                        {
                            // max reconnections - idle message center
                            if (mRetryCount >= MAX_IDLE_BACKOFF)
                            {
                                LogUtils.d("maximum number of reconnections - stopping message center");
                                if (mListener != null)
                                    mListener.aborted(ie);
                                break;
                            }

                            // exponential backoff :)
                            float time = (float) ((Math.pow(2, ++mRetryCount)) - 1) / 2;
                            LogUtils.d("retrying in " + time + " seconds (retry=" + mRetryCount + ")");
                            if (mListener != null)
                                mListener.reconnectingIn((int) time);

                            Thread.sleep((long) (time * 1000));
                            continue;
                        }
                        catch (InterruptedException intexc)
                        {
                            LogUtils.d("- interrupted.");
                            break;
                        }
                    }
                    else
                    {
                        // retry disabled - notify and exit
                        if (mListener != null)
                            mListener.connectionClosedOnError(ie);
                        break;
                    }
                }
            }
            mRetryCount = 0;
        }
        mConnecting = false;
    }

    public XMPPConnection getConnection()
    {
        return mConn;
    }

    public boolean isConnected()
    {
        return (mConn != null && mConn.isAuthenticated());
    }

    public boolean isConnecting()
    {
        return mConnecting;
    }

    public interface ConnectionHelperListener extends ConnectionListener
    {
        public void created(XMPPConnection connection);

        public void aborted(Exception e);

        public void authenticationFailed();
    }

    public void setListener(ConnectionHelperListener mListener)
    {
        this.mListener = mListener;
    }

    public void shutdown() throws SmackException.NotConnectedException
    {
        mConnecting = false;
        interrupt();

        if (mConn != null)
            mConn.disconnect();
    }
}
