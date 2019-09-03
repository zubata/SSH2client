package com.Clients;

import com.jcraft.jsch.*;

public class SFTPclient extends BaseClient {

    public SFTPclient(String host, String user, String password) throws JSchException {
        super(host,user,password);
    }

    public void downloadFile(String source,String destination) throws JSchException {
        System.out.println("Начинается скачивание mgwssd");
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        try {
            sftpChannel.get(source,destination);
            sftpChannel.exit();
        } catch (SftpException e) {
            System.out.println("Не найдено такого файла, необходимо проверить наличие папки на удалённом сервере");
        }
        System.out.println("Скачивание mgwssd закончено");
    }

}
