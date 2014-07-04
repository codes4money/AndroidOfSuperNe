package com.donal.superne.app.manager;


import com.donal.superne.app.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.superne.Registration;
import org.jivesoftware.smackx.superne.ResultInfo;
import org.jivesoftware.smackx.superne.SuperneManager;
import org.jivesoftware.smackx.superne.User;

import java.io.IOException;
import java.util.List;

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

    private static synchronized XMPPTCPConnection init() {
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
//                ProviderManager.addIQProvider("result", "superne:iq:register", new RegistIQProvider());
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
        if (xmpptcpConnection != null)
        {
            try
            {
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
    public static void login(String account, String password, XMPPCallback callback) throws IOException, SmackException, XMPPException{
        String result;
        try
        {
            xmpptcpConnection.login(account, password, "Android");
            callback.onSuccess("");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            result = "登陆失败,错误:" + e.getMessage();
            callback.onFailure(result);
        }
        catch (SmackException e)
        {
            e.printStackTrace();
            result = "登陆失败,错误:" + e.getMessage();
            callback.onFailure(result);
        }
        catch (XMPPException e)
        {
            e.printStackTrace();
            if (e instanceof SASLErrorException)
            {
                result = "账号密码不正确，请核对后再试";
                callback.onFailure(result);
            }
            else
            {
                result = "登陆失败,错误:" + e.getMessage();
                callback.onFailure(result);
            }
        }
    }

    /**
     * 设置在线状态
     *
     * @throws SmackException.NotConnectedException
     */
    public void setAvailable() throws SmackException.NotConnectedException
    {
        Presence presence = new Presence(Presence.Type.available);
        xmpptcpConnection.sendPacket(presence);
    }

    /**
     * 账号注册
     * @param registration
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void register(Registration registration, XMPPCallback callback) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException
    {
        ResultInfo resultInfo =  SuperneManager.getInstance(xmpptcpConnection).register(registration);
        if (resultInfo.getErrorCode().equals("0")) {
            callback.onSuccess("");
        }
        else {
            callback.onFailure(resultInfo.getErrorMsg());
        }
    }

    /**
     * 更新个人资料
     * @param user
     * @param callback
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void updatePersonal(User user, XMPPCallback callback) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        ResultInfo resultInfo = SuperneManager.getInstance(xmpptcpConnection).updateUser(user);
        LogUtils.d(resultInfo.toString());
    }

    /**
     * 昵称搜索
     * @param name
     * @param page
     * @param callback
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void searchByName(String name, int page, XMPPCallback callback) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException{
        ResultInfo resultInfo = SuperneManager.getInstance(xmpptcpConnection).searchUserByUserName(name, page);
        if (resultInfo.getErrorCode().equals("0")) {
            List<User> users = new Gson().fromJson(resultInfo.getContent(),new TypeToken<List<User>>(){}.getType());
            callback.onSuccess(users);
        }
        else {
            callback.onFailure(resultInfo.getErrorMsg());
        }

    }

    /**
     * 账号搜索
     * @param username
     * @param page
     * @param callback
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void searchByUsername(String username, int page, XMPPCallback callback) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException{
        ResultInfo resultInfo = SuperneManager.getInstance(xmpptcpConnection).searchUserByUserName(username, page);
        if (resultInfo.getErrorCode().equals("0")) {
            List<User> users = new Gson().fromJson(resultInfo.getContent(),new TypeToken<List<User>>(){}.getType());
            callback.onSuccess(users);
        }
        else {
            callback.onFailure(resultInfo.getErrorMsg());
        }
    }

    /**
     * 申请加好友
     * @param user
     * @param callback
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void requestAddingUser(User user, XMPPCallback callback) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, SmackException.NotLoggedInException{
        Roster roster = xmpptcpConnection.getRoster();
        roster.createEntry(user.getUsername(), null, new String[] { "friends" });
    }

    public interface XMPPCallback {
        public void onSuccess(Object object);
        public void onFailure(Object object);
    }
}
