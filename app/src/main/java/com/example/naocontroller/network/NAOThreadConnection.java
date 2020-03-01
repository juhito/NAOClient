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
    private String messageToNao;
    private Object[] answer;

    private volatile boolean running;
    private volatile boolean sendMessages;

    public NAOThreadConnection(InetAddress ip, int port, String command, String message) throws IOException {
        socket = new Socket(ip, port);
        commandToRun = command;
        if(!message.isEmpty()) messageToNao = message;
        else messageToNao = "";

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
                    if (messageToNao.isEmpty())
                        out.writeObject(new Object[]{commandToRun});
                    else out.writeObject(new Object[]{commandToRun, messageToNao});
                    answer = (Object[]) in.readObject();
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Object[] getAnswer() { return(answer); }
    public void disableMessages() { sendMessages = false; }
    public void enableMessages() { sendMessages = true; }
    public void shutdown() throws IOException {
        running = false;
        socket.close();
    }
}
