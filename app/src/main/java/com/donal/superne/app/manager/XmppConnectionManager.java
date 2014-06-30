package com.donal.superne.app.manager;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.IOException;

/**
 *
 * XMPP服务器连接工具类.
 *
 */
public class XmppConnectionManager {
	public static final String XMPP_HOST = "112.126.70.4";
	public static final int XMPP_PORT = 5228;
	public static String XMPP_SERVER_NAME = "super";

    private static XmppConnectionManager xmppConnectionManager;

    private static XMPPTCPConnection xmpptcpConnection;


    private XmppConnectionManager() {
        init();
    }

    public static XmppConnectionManager getInstance() {
        if (xmppConnectionManager == null) {
            xmppConnectionManager = new XmppConnectionManager();
        }
        return xmppConnectionManager;
    }

    public static synchronized XMPPTCPConnection init() {
        if (xmpptcpConnection == null || !xmpptcpConnection.isAuthenticated()) {
            try
            {
                openConnection();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (XMPPException e)
            {
                e.printStackTrace();
            }
            catch (SmackException e)
            {
                e.printStackTrace();
            }
        }
        return xmpptcpConnection;
    }

    /**
    * xmpp连接
    */
    public static void openConnection() throws IOException, XMPPException, SmackException {
        if (xmpptcpConnection != null) {
            xmpptcpConnection.isConnected();
            xmpptcpConnection = null;
        }
//        SmackAndroid.init();
        ConnectionConfiguration config = new ConnectionConfiguration(XMPP_HOST, XMPP_PORT, XMPP_SERVER_NAME);
        config.setDebuggerEnabled(true);
        config.setRosterLoadedAtLogin(false);
        config.setReconnectionAllowed(false);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setSendPresence(false);
        SmackConfiguration.setDefaultPacketReplyTimeout(5000);
        xmpptcpConnection = new XMPPTCPConnection(config);
        xmpptcpConnection.connect();// 连接到服务器
        PingManager pingManager = PingManager.getInstanceFor(xmpptcpConnection);
        pingManager.setPingInterval(3000);
    }

    /**
     * 返回一个有效的xmpp连接,如果无效则返回空.
     */
    public XMPPTCPConnection getConnection() {
        if (xmpptcpConnection == null) {
            throw new RuntimeException("请先初始化XMPPConnection连接");
        }
        return xmpptcpConnection;
    }

    public void disconnect() {
        if (xmpptcpConnection != null) {
            try {
                xmpptcpConnection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * xmpp登录
     * @param account
     * @param password
     * @throws IOException
     * @throws SmackException
     * @throws XMPPException
     */
    public static void login(String account, String password) throws IOException, SmackException, XMPPException{
        xmpptcpConnection.login(account, password, "Android");
    }

    /**
     * 设置在线状态
     *
     * @throws SmackException.NotConnectedException
     */
    public static void setAvailable() throws SmackException.NotConnectedException
    {
        Presence presence = new Presence(Presence.Type.available);
        xmpptcpConnection.sendPacket(presence);
    }
}
