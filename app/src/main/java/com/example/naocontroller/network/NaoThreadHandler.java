package com.example.naocontroller.network;

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
    private String commandToRun;
    private String messageToNao;
    private Object[] answer;

    private Boolean running = true;


    public NaoThreadHandler(InetAddress ip, int port, String command, String message) throws IOException {
        this.socket = new Socket(ip, port);
        this.commandToRun = command;
        if(!message.isEmpty()) this.messageToNao = message;
        else this.messageToNao = "";

        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());
    }

    @Override
    public void run() {
        while(this.running) {
            try {
                if(this.messageToNao.isEmpty()) out.writeObject(new Object[] {this.commandToRun});
                else out.writeObject(new Object[]{this.commandToRun, this.messageToNao});
                this.answer = (Object[]) this.in.readObject();
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

    public Object[] getAnswer() { return(this.answer); }
}
