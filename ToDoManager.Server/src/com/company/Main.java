package com.company;

import Settings.Config;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    private static ServerSocket serverSocket;
    private static List<Modification> Modifications = new ArrayList<>();
    private static List<UUID> Clients = new ArrayList<>();

    public static void main(String[] args)
    {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(Config.PORT, 0, InetAddress.getLocalHost());
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
                socket.setSoTimeout(Config.TIMEOUT);
                System.out.println("Client connected: " + socket.getInetAddress());
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new HostThread(socket, Clients, Modifications).start();
        }
    }
}