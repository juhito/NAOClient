package com.example.naocontroller.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class BroadcastClient {
    private DatagramSocket socket;

    public BroadcastClient() throws Exception {
        this.socket = new DatagramSocket();
        this.socket.setBroadcast(true);
    }

    public InetAddress sendBroadcast() {
        try {
            byte[] messageData = "IM_LOOKING_FOR_NAO".getBytes();

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while(interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if(networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for(InterfaceAddress i : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = i.getBroadcast();
                    if(broadcast == null)
                        continue;

                    // Send the broadcast package
                    try {
                        DatagramPacket messagePacket = new DatagramPacket(messageData, messageData.length, broadcast, 8889);
                        this.socket.send(messagePacket);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("Request packet sent to: " + broadcast.getHostAddress() + "; interface: " +
                            networkInterface.getDisplayName());
                }
            }

            System.out.println("Done looping over all network interfaces. Now waiting for a reply.");

            byte[] responseBuffer = new byte[15000];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            this.socket.receive(responsePacket);


            String message = new String(responsePacket.getData()).trim();
            System.out.println("Broadcast response from server: " + message + "; ip: " +
                    responsePacket.getAddress().getHostAddress());

            if (message.equals("YOU_FOUND_ME")) {
                this.socket.close();
                return (responsePacket.getAddress());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return(null);
    }
}
