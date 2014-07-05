package com.donal.superne.app.client;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by jianghaofeng on 14-7-5.
 */
public class SuperneConnection extends XMPPTCPConnection
{
    private final static ConnectionConfiguration config;

    static
    {
        config = new ConnectionConfiguration("112.126.70.4", 5222);
        config.setDebuggerEnabled(true);
        config.setRosterLoadedAtLogin(true);
        config.setReconnectionAllowed(false);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setSendPresence(false);
        SmackConfiguration.setDefaultPacketReplyTimeout(5000);
    }

    public SuperneConnection()
    {
        super(config);
    }
}
