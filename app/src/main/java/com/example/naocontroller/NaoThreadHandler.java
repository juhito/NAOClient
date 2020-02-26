package com.example.naocontroller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class NaoThreadHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Boolean running = true;


    public NaoThreadHandler(InetAddress ip, int port) throws IOException {
        this.socket = new Socket(ip, port);

        out = new ObjectOutputStream(this.socket.getOutputStream());
        in = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void run() {
        System.out.println("New thread created successfully");

        while(running) {
            try {
                out.writeObject(new Object[] {"getTempData"});

                Object[] answer = (Object[]) in.readObject();

                if(answer != null) {
                    System.out.println("TEMP DATA: " + Arrays.toString(answer));
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void pauseThread() {
        this.running = false;
    }

    public void continueThread() {
        this.running = true;
    }

}
