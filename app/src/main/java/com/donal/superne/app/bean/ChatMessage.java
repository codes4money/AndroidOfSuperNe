package com.donal.superne.app.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by jianghaofeng on 14-7-6.
 */
@Table(name = "ChatMessage")
public class ChatMessage implements Serializable
{
    @Id(column = "_id")
    private long   id;
    /**
     * 消息时间
     */
    private String timestamp;

    /**
     * 发送者
     */
    private String sender;

    /**
     * 接受者
     */
    private String receiver;

    /**
     * 消息类型 1:文字，2：图片，3：语音，4：位置
     */
    private int messageFlag;

    /**
     * 消息是否发送成功
     */
    private boolean succeed;

    /**
     * 消息状态 1：自己发出 2：接受到的
     */
    private int status;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 位置消息的经纬度
     */
    private String location;

    /**
     * 语音消息长度
     */
    private String duration;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public int getMessageFlag()
    {
        return messageFlag;
    }

    public void setMessageFlag(int messageFlag)
    {
        this.messageFlag = messageFlag;
    }

    public boolean isSucceed()
    {
        return succeed;
    }

    public void setSucceed(boolean succeed)
    {
        this.succeed = succeed;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }
}
