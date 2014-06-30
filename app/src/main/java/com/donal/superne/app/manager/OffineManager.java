package com.donal.superne.app.manager;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.offline.packet.OfflineMessageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghaofeng on 14-5-27.
 */
public class OffineManager extends OfflineMessageManager
{
    XMPPConnection mConnection;
    private PacketFilter packetFilter;
    private final static String namespace = "http://jabber.org/protocol/offline";

    public OffineManager(XMPPConnection connection)
    {
        super(connection);
        this.mConnection = connection;
        packetFilter =
                new AndFilter(new PacketExtensionFilter("offline", namespace),
                              new PacketTypeFilter(Message.class));
    }

    @Override
    public List<Message> getMessages() throws SmackException.NoResponseException,
            XMPPException.XMPPErrorException, SmackException.NotConnectedException
    {
        List<Message> messages = new ArrayList<Message>();
        OfflineMessageRequest request = new OfflineMessageRequest();
        request.setFetch(true);
        PacketCollector messageCollector = mConnection.createPacketCollector(packetFilter);
        mConnection.createPacketCollectorAndSend(request).nextResultOrThrow();

        Message message = (Message) messageCollector.nextResult();
        while (message != null)
        {
            messages.add(message);
            message = (Message) messageCollector.nextResult();
        }
        // Stop queuing offline messages
        messageCollector.cancel();
        return messages;
    }
}
