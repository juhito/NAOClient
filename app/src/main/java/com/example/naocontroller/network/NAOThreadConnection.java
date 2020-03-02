package com.example.naocontroller.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class NAOThreadConnection implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String commandToRun;
    private Object[] commandArgs;
    private Object answer;

    private volatile boolean running;
    private volatile boolean sendMessages;

    public NAOThreadConnection(InetAddress ip, int port, String command, Object ... args) throws IOException {
        socket = new Socket(ip, port);
        commandToRun = command;
        if(args.length >= 1) commandArgs = args;
        else commandArgs = null;

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        running = true;
        sendMessages = true;
    }

    @Override
    public void run() {
        while(socket.isConnected() || running) {
            try {
                if(sendMessages) {
                    if (commandArgs != null)
                        out.writeObject(new Object[]{commandToRun, commandArgs});
                    else out.writeObject(new Object[]{commandToRun});
                    answer = in.readObject();
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Object getAnswer() { return(answer); }
    public void disableMessages() { sendMessages = false; }
    public void enableMessages() { sendMessages = true; }
    public void shutdown() throws IOException {
        running = false;
        socket.close();
    }
}
