package com.example.naocontroller;

import android.os.Process;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    public void sendMessage(String command, String args) {
        try {

            out.writeObject(new Object[] {command, args});

            Object[] answer = (Object[]) in.readObject();

            if(answer != null) {
                System.out.println("TEMP DATA: " + Arrays.toString(answer));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String command) {
        try {

            out.writeObject(new Object[] {command});

            Object[] answer = (Object[]) in.readObject();

            if(answer != null) {
                System.out.println("TEMP DATA: " + Arrays.toString(answer));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* TODO: Find a better way to do this.

            Do I need a reference to the created NaoThreadHandler?

            or perhaps a reference to the created Thread?
     */
    public static void startThreadedConnection(InetAddress serverAddress, int port) {
        try {
            System.out.println("trying to create a new thread");
            new Thread(new NaoThreadHandler(serverAddress, port)).start();
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}