package com.donal.superne.app.manager;


import com.donal.superne.app.BaseApplication;
import com.donal.superne.app.model.register.RegistIQProvider;
import com.donal.superne.app.model.register.Registration;
import com.donal.superne.app.model.register.RegistrationIQ;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.IOException;
import java.security.Provider;

/**
 *
 * XMPP服务器连接工具类.
 *
 */
public class XmppConnectionManager {
	public static final String XMPP_HOST = "112.126.70.4";
	public static final int XMPP_PORT = 5222;
	public static String XMPP_SERVER_NAME = "superne";

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
        if (xmpptcpConnection == null) {
            try
            {
                LogUtils.d("sss");
                SmackAndroid.init(BaseApplication.getInstance());
                ConnectionConfiguration config = new ConnectionConfiguration(XMPP_HOST, XMPP_PORT, XMPP_SERVER_NAME);
                config.setDebuggerEnabled(true);
                config.setRosterLoadedAtLogin(false);
                config.setReconnectionAllowed(false);
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                config.setSendPresence(false);
                SmackConfiguration.setDefaultPacketReplyTimeout(5000);
                xmpptcpConnection = new XMPPTCPConnection(config);
                xmpptcpConnection.connect();// 连接到服务器
                ProviderManager.addIQProvider("result", "superne:iq:register", new RegistIQProvider());
                PingManager pingManager = PingManager.getInstanceFor(xmpptcpConnection);
                pingManager.setPingInterval(3000);
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
                xmpptcpConnection = null;
                xmppConnectionManager = null;
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

    /**
     * xmpp注册
     * @param registration
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void register(Registration registration) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException
    {
        RegistrationIQ iq = new RegistrationIQ();
        iq.setType(IQ.Type.GET);
        iq.setTo(xmpptcpConnection.getServiceName());
        iq.setMsg(new Gson().toJson(registration));
        RegistrationIQ packet = (RegistrationIQ) xmpptcpConnection.createPacketCollectorAndSend(iq).nextResultOrThrow();
        if (packet.getCode().equals("0"))
        {
            //注册成功
        }
        else
        {
            LogUtils.d(packet.getMsg());
        }
    }
}
