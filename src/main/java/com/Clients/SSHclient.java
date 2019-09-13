package com.Clients;

import com.jcraft.jsch.*;

import java.io.*;

public class SSHclient extends BaseClient {
    private FileOutputStream out;

    public SSHclient(String host, String user, String password) throws JSchException {
        super(host,user,password);
        try {
            out = new FileOutputStream("C:\\Users\\oleg.zubkov\\логи.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Файл, куда записывать логи, не найден");
        }
    }

    public String sendCommand(String command) throws JSchException, IOException {
        Channel channel=session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        InputStream fromSSH = channel.getInputStream();
        channel.connect(3000);

        StringBuilder temp = new StringBuilder();
        int readByte = 0;

        while(readByte != -1)
        {
            readByte = fromSSH.read();
            temp.append((char)readByte);
        }
        String result = temp.toString();
        out.write(result.getBytes());

        channel.disconnect();
        return result;
    }

    @Override
    public void disconnect() {
        try {
            session.disconnect();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
