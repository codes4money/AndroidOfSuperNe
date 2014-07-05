package com.donal.superne.app.bean;

import org.jivesoftware.smackx.superne.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by donal on 14-7-4.
 */
public class UserListEntity implements Serializable
{

    private List<User> users;

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }

    @Override
    public String toString()
    {
        return "UserListEntity{" +
                "users=" + users +
                '}';
    }
}
