package server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import common.MessageInfo;


public class UDPServer {
    private DatagramSocket datagramSocket;
    private int port;
    private int totalMessages = -1;
    private int[] receivedMessages;
    private boolean close;

    private void run() throws SocketTimeoutException {
        int packageSize;
        byte[] packageData;
        DatagramPacket datagramPacket;
        // Receive the messages and process them by calling processMessage(...)
        System.out.println("Ready for receiving data...");
        while (!close) {
            //Receive request from client
            packageSize = 5000;
            packageData = new byte[5000];
            datagramPacket = new DatagramPacket(packageData, packageSize);
            try {
                datagramSocket.setSoTimeout(100000);
                datagramSocket.receive(datagramPacket);
            } catch (IOException e) {
                System.out.println("Error IO exception receiving datagramPacket.");
                System.out.println("Could be due to timeout.");
                System.out.println("Now closing server...");
                System.exit(-1);
            }
            processMessage(datagramPacket.getData());
        }
    }
    public void processMessage(byte[] data) {
        MessageInfo messageInfo = null;
        // Use the data to construct a new MessageInfo object
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(byteStream));
            messageInfo = (MessageInfo) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Could not find class match for transmitted message.");
        } catch (IOException e) {
            System.out.println("Error: IO exception creating ObjectInputStream.");
        }
        // On receipt of first message, initialize the receive buffer
        if (receivedMessages == null) {
            totalMessages = messageInfo.totalMessages;
            receivedMessages = new int[totalMessages];
        }
        // Log receipt of the message
        receivedMessages[messageInfo.messageNumber] = 1;
        // If this ois the last expected message, then identify any missing messages
        if (messageInfo.messageNumber + 1 == messageInfo.totalMessages) {
            close = true;
            String string = "Lost packet numbers: ";
            int count = 0;
            for (int i = 0; i < totalMessages; i++) {
                if (receivedMessages[i] != 1) {
                    count++;
                    string = string + " " + (i + 1) + ", ";
                }
            }
            if (count == 0) string = string + "None";
            System.out.println("Messages processed...");
            System.out.println("Of " + messageInfo.totalMessages + ", " + (messageInfo.totalMessages - count) + " received successfully...");
            System.out.println("Of " + messageInfo.totalMessages + ", " + count + " failed...");
            System.out.println(string);
            System.out.println("Test finished.");
        }
    }
    public UDPServer(int port) {
        // Initialize UDP socket for receiving data
        try {
            this.port = port;
            datagramSocket = new DatagramSocket(this.port);
        } catch (SocketException e) {
            System.out.println("Error: Could not create socket on port " + this.port);
            System.exit(-1);
        }
        // Make it so the server can run
        close = false;
    }
    public static void main(String args[]) {
        int recvPort;
        // Get the parameters from method main
        if (args.length < 1) {
            System.err.println("Error: Arguments required: recv-port");
            System.exit(-1);
        }
        recvPort = Integer.parseInt(args[0]);
        // Initialize Server object and start it by calling run()
        UDPServer udpServer = new UDPServer(recvPort);
        try {
            udpServer.run();
        } catch (SocketTimeoutException e) {
        }
    }
}
