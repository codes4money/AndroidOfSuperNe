package com.donal.superne.app.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;

import com.donal.superne.app.Superne;
import com.donal.superne.app.client.Preferences;
import com.donal.superne.app.client.SuperneConnection;
import com.donal.superne.app.client.XMPPConnectionHelper;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.manager.DBManager;
import com.donal.superne.app.ui.login.Login;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.superne.User;

import java.util.Collection;
import java.util.Iterator;

public class MessageCenterService extends IntentService implements XMPPConnectionHelper.ConnectionHelperListener
{
    private PowerManager.WakeLock mWakeLock;

    /**
     * The connection instance.
     */
    private SuperneConnection mConnection;

    /**
     * The connection helper instance.
     */
    private XMPPConnectionHelper mHelper;
    private boolean              doConnect;

    private LocalBroadcastManager mLocalBroadcastManager;

    private DbUtils mDb;


    public MessageCenterService()
    {
        super("MessageCenterService");
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();
            if (Constant.ACTION_RESTART.equals(action))
            {
                quit(true);
                doConnect = true;
            }
            else if (Constant.ACTION_LOGIN.equals(action))
            {
                createConnection();
            }
        }

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        SmackAndroid.init(getApplicationContext());
        PowerManager pwr = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pwr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Superne.class.getSimpleName());
        mWakeLock.setReferenceCounted(false);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }


    private synchronized void createConnection()
    {
        if (mConnection == null && mHelper == null)
        {
            mConnection = null;
            mWakeLock.acquire();
            mHelper = new XMPPConnectionHelper(this);
            mHelper.setListener(this);
            mHelper.start();
        }
    }

    @Override
    public void created(XMPPConnection connection)
    {
        mConnection = (SuperneConnection) connection;
        mConnection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
        PingManager.getInstanceFor(mConnection).setPingInterval(5000);

        PacketFilter filter;

//
//        filter = new PacketTypeFilter(Presence.class);
//        mConnection.addPacketListener(new PresenceListener(), filter);

        filter = new PacketTypeFilter(RosterPacket.class);
        mConnection.addPacketListener(new RosterListener(), filter);
    }

    @Override
    public void aborted(Exception e)
    {
        stopSelf();
    }

    @Override
    public void authenticationFailed()
    {

    }

    @Override
    public void connected(XMPPConnection xmppConnection)
    {

    }

    @Override
    public void authenticated(XMPPConnection xmppConnection)
    {
        // helper is not needed any more
        mHelper = null;

        mDb = DbUtils.create(this, Preferences.getPrefUserName());
        mDb.configAllowTransaction(true);
        mDb.configDebug(true);
        try
        {
            mDb.createTableIfNotExist(User.class);
        }
        catch (DbException e)
        {
            e.printStackTrace();
        }
        DBManager.setDb(mDb);
        broadcast(Constant.ACTION_CONNECTED);
        mWakeLock.release();
    }

    @Override
    public void connectionClosed()
    {

    }

    @Override
    public void connectionClosedOnError(Exception e)
    {

    }

    @Override
    public void reconnectingIn(int i)
    {

    }

    @Override
    public void reconnectionSuccessful()
    {

    }

    @Override
    public void reconnectionFailed(Exception e)
    {

    }


    private synchronized void quit(boolean restarting)
    {
        if (mHelper != null)
            mHelper.setListener(null);
        if (mConnection != null)
            mConnection.removeConnectionListener(this);

        if (mHelper != null)
        {
            new AbortThread(mHelper).start();
            mHelper = null;
        }

        if (mConnection != null)
        {
            try
            {
                mConnection.disconnect();
            }
            catch (SmackException.NotConnectedException e)
            {
                e.printStackTrace();
            }
            mConnection = null;
        }
        mWakeLock.release();
    }

    private static final class AbortThread extends Thread
    {
        private final XMPPConnectionHelper mHelper;

        public AbortThread(XMPPConnectionHelper helper)
        {
            mHelper = helper;
        }

        @Override
        public void run()
        {
            try
            {
                mHelper.shutdown();
            }
            catch (Exception e)
            {
                // ignored
            }
        }
    }

    private void broadcast(String action)
    {
        broadcast(action, null, null);
    }

    private void broadcast(String action, String extraName, String extraValue)
    {
        Intent i = new Intent(action);
        if (extraName != null)
            i.putExtra(extraName, extraValue);

//        mLocalBroadcastManager.sendBroadcast(i);
        sendBroadcast(i);
    }

    private static Intent getStartIntent(Context context)
    {
        final Intent intent = new Intent(context, MessageCenterService.class);
        return intent;
    }

    public static void start(Context context)
    {
        if (isNetworkConnectionAvailable(context))
        {
            final Intent intent = getStartIntent(context);
            context.startService(intent);
        }
        else
            LogUtils.e("network not available or background data disabled - abort service start");
    }

    public static void stop(Context context)
    {
        LogUtils.e("shutting down message center");
        context.stopService(new Intent(context, MessageCenterService.class));
    }

    public static void restart(Context context)
    {
        LogUtils.e("restarting message center");
        Intent i = new Intent(context, MessageCenterService.class);
        i.setAction(Constant.ACTION_RESTART);
        context.startService(i);
    }

    /**
     * Checks for network availability.
     */
    public static boolean isNetworkConnectionAvailable(Context context)
    {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getBackgroundDataSetting())
        {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }

    public static void login(final Context context)
    {
        Intent i = new Intent(context, MessageCenterService.class);
        i.setAction(Constant.ACTION_LOGIN);
        context.startService(i);
    }

    /**
     * Listener for roster iq stanzas.
     */
    private final class RosterListener implements PacketListener
    {
        @Override
        public void processPacket(Packet packet)
        {
            RosterPacket p = (RosterPacket) packet;
            Collection<RosterPacket.Item> items = p.getRosterItems();
            for (RosterPacket.Item item : items)
            {
                User user = new User();
                user.setName(item.getName());
                user.setUsername(StringUtils.parseName(item.getUser()));
                try
                {
                    mDb.saveOrUpdate(user);
                }
                catch (DbException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
