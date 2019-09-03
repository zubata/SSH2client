package com.Clients;

import com.MyUserInfo;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class BaseClient {
    Session session;

    public BaseClient(String host, String user, String password) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user,host,22);
        session.setPassword(password);
        UserInfo my = new MyUserInfo();
        session.setUserInfo(my);
        //провека хоста в known_hosts пока отключена, пока ещё не до конца в этом разобрался
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    public void disconnect() {
        session.disconnect();
    }
}
