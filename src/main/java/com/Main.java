package com;

import com.Clients.*;
import com.jcraft.jsch.JSchException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static HashMap<String, String> mgwList;

    static {
        mgwList = new HashMap<>();
        mgwList.put("mgw601", "10.249.177.225");
        mgwList.put("mgw602", "10.249.177.233");
        mgwList.put("mgw603", "10.249.199.225");
        mgwList.put("mgw604", "10.249.199.233");
        mgwList.put("mgw605", "10.249.150.225");
        mgwList.put("mgw606", "10.249.150.233");
    }

    public static void main(String[] args) throws IOException, JSchException {
        String node = inputName();
        if (node == null) return;
        System.out.println("Cнять mgwssd (ввести y, если да)");
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        String answer = rd.readLine();
        if (answer.equals("y")) {
            rd.close();
            downloadMGWssd(node);
        } else {
            System.out.println("Ввести откуда взять файл (на удалённом сервере)");
            String source = rd.readLine();
            System.out.println("Ввести куда положить файл (на локальном компьютере, по умолчанию папка H:\\)");
            String destination = rd.readLine();
            rd.close();
            downloadFile(node, source, destination);
        }
    }

    //Ввод имени узла, по которому уже находится ip адрес узла
    public static String inputName() throws IOException {
        System.out.println("Ввести имя узла");
        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String name = rd.readLine().toLowerCase();
            if (name.equals("exit")) break;
            for (Map.Entry<String, String> s : mgwList.entrySet()) {
                if (s.getKey().equals(name)) return s.getValue();
            }
            System.out.println("Имя узла введено не верно, введите ещё раз, чтобы прервать ввод ввести exit ");
        }
        return null;
    }

    //Взятие mgwssd с sftp сервера нужного MGW, перед этим даётся команда на создание файла через SSH2
    public static void downloadMGWssd(String node) throws JSchException, IOException {
        SSHclient ssh = new SSHclient(node, "username", "password");
        System.out.println("mgwssd начинает собираться");
        String temp = ssh.sendCommand("start troubleshooting mgwssd full");
        int start = temp.indexOf("Created zip: ");
        String source = temp.substring(start + 13, start + 88);
        ssh.disconnect();
        System.out.println("mgwssd собрался");
        System.out.println("Файл будет взят из папки /var/mgwssd удалённого сервера и помещён в H:\\ на локальном компьютере, для изменения пути отправляйте пишите мне и добавлю такую опцию");
        SFTPclient sftp = new SFTPclient(node, "root", "atca#123");
        sftp.downloadFile(source, "H:\\");
        sftp.disconnect();
    }

    public static void downloadFile(String node, String source, String destination) throws JSchException {
        if (destination.isEmpty()) destination = "H:\\";
        System.out.println(String.format("Файл будет взят из папки %s удалённого сервера и помещён в %s на локальном компьютере", source, destination));
        SFTPclient sftp = new SFTPclient(node, "username", "password");
        sftp.downloadFile(source, "H:\\");
        sftp.disconnect();
    }
}

