package com.donal.superne.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.donal.superne.app.R;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.manager.XmppConnectionManager;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.superne.User;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatReceiveService extends Service implements RosterListener
{

    private Context context;
    private ChatListener chatListener;
    private NotificationManager notificationManager;

    public ChatReceiveService()
    {
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void init()
    {
        XMPPTCPConnection conn = XmppConnectionManager.getInstance().getConnection();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        chatListener = new ChatListener();
        conn.addPacketListener(chatListener, new MessageTypeFilter(Message.Type.chat));
        Roster roster = conn.getRoster();
        roster.addRosterListener(this);

    }

    class ChatListener implements PacketListener
    {
        @Override
        public void processPacket(Packet packet) throws SmackException.NotConnectedException
        {

        }
    }

    private void setNotiType(String contentTitle,
                             String contentText, Class activity, String from)
    {
//        JsonMessage msg = new JsonMessage();
//        Gson gson = new Gson();
//        msg = gson.fromJson(contentText, JsonMessage.class);
        Intent notifyIntent = new Intent(this, activity);
        notifyIntent.putExtra("to", from);
        PendingIntent appIntent = PendingIntent.getActivity(this, 0,
                notifyIntent, 0);
        Notification myNoti = new Notification();
        myNoti.flags = Notification.FLAG_AUTO_CANCEL;
        myNoti.icon = R.drawable.ic_launcher;
        myNoti.tickerText = contentTitle;
        myNoti.defaults |= Notification.DEFAULT_SOUND;
        myNoti.defaults |= Notification.DEFAULT_VIBRATE;
//        myNoti.setLatestEventInfo(this, contentTitle, msg.text, appIntent);
        notificationManager.notify(0, myNoti);
    }

    @Override
    public void entriesAdded(Collection<String> invites)
    {
        for (Iterator iter = invites.iterator(); iter.hasNext(); )
        {
            String fromUserJids = (String) iter.next();
            context.sendBroadcast(new Intent(Constant.ACTION_REQUEST_FROM_USER_ADD_FRIEND).putExtra("fromUserJid", fromUserJids));
        }
    }

    @Override
    public void entriesUpdated(Collection<String> invites)
    {
//        String fromUserJids = null;
//        for (Iterator iter = invites.iterator(); iter.hasNext();) {
//            fromUserJids = (String)iter.next();
//        }
//        if(fromUserJids != null){
//            final User user = new User();
//            user.setUsername(fromUserJids);
//            user.setName("");
//            final Handler handler = new Handler() {
//                @Override
//                public void handleMessage(android.os.Message msg) {
//                    switch (msg.what) {
//                        case 1:
//                            context.sendBroadcast(new Intent(Constant.ACTION_FRIEND_COMFIRE));
//                            break;
//                        case 0:
//                            break;
//                        case -1:
//                            break;
//                    }
//                }
//            };
//            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//            singleThreadExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    final android.os.Message msg = new android.os.Message();
//                    try {
//                        XmppConnectionManager.getInstance().requestAddingUser(user, new XmppConnectionManager.XMPPCallback() {
//                            @Override
//                            public void onSuccess(Object object) {
//                                msg.what = 1;
//                            }
//
//                            @Override
//                            public void onFailure(Object object) {
//                                msg.what = 0;
//                            }
//                        });
//                    }
//                    catch (Exception e) {
//                        msg.what = -1;
//                    }
//                    handler.sendMessage(msg);
//                }
//            });
//        }
    }

    @Override
    public void entriesDeleted(Collection<String> strings)
    {
        if (strings.size() > 0)
        {
            context.sendBroadcast(new Intent(Constant.ACTION_FRIEND_DELETE));
        }
    }

    @Override
    public void presenceChanged(Presence presence)
    {

    }

}
