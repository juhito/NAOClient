package com.example.naocontroller.network;

import android.os.Process;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class NAOClient {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public NAOClient(InetAddress serverAddress, int port) {
        try {
            this.socket = new Socket(serverAddress, port);

            out = new ObjectOutputStream(this.socket.getOutputStream());
            in = new ObjectInputStream(this.socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object[] sendMessage(String command, String args) {
        try {
            out.writeObject(new Object[] {command, args});
            return((Object[]) in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(null);
    }

    public Object[] sendMessage(String command) {
        try {
            out.writeObject(new Object[] {command});
            return((Object[]) in.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(null);
    }

    public void printSocketInformation() {
        System.out.println(this.socket.getInetAddress().getHostAddress());
    }
}