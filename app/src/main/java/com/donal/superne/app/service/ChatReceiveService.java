package com.donal.superne.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.donal.superne.app.R;
import com.donal.superne.app.manager.XmppConnectionManager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

public class ChatReceiveService extends Service {

    private Context context;
    private ChatListener chatListener;
    private NotificationManager notificationManager;
    public ChatReceiveService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void init() {
        XMPPTCPConnection conn = XmppConnectionManager.getInstance().getConnection();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        chatListener = new ChatListener();
        conn.addPacketListener(chatListener, new MessageTypeFilter(Message.Type.chat));
    }

    class ChatListener implements PacketListener {
        @Override
        public void processPacket(Packet packet) throws SmackException.NotConnectedException {

        }
    }

    private void setNotiType(String contentTitle,
                             String contentText, Class activity, String from) {
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
}
