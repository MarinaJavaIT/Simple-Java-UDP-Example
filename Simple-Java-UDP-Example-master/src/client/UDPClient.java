package client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {
    private DatagramSocket datagramSocket;

    public static void main(String[] args) {
        InetAddress serverAddr = null;
        int recvPort;
        int numberOfMassages;
        // Get the parameters of method main
        if (args.length < 3) {
            System.err.println("Arguments required: server name/IP, recv port, message count");
            System.exit(-1);
        }
        try {
            serverAddr = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
            System.exit(-1);
        }
        recvPort = Integer.parseInt(args[1]);
        numberOfMassages = Integer.parseInt(args[2]);
        //Construct UDP client class and try to send messages
        System.out.println("Constructing UPD client");
        UDPClient client = new UDPClient();
        System.out.println("Sending messages...");
        client.testLoop(serverAddr, recvPort, numberOfMassages);
    }
    public UDPClient() {
        //Initialise the UDP socket for sending data
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error creating socket for sending data.");
        }
    }

    private void testLoop(InetAddress serverAddr, int recvPort, int numberOfMassages) {
        MessageInfo messageInfo;
        ByteArrayOutputStream byteStream;
        ObjectOutputStream oos;
        //Sending messages to the server
        for (int i = 0; i < numberOfMassages; i++) {
            messageInfo = new MessageInfo("Data for transferring ",numberOfMassages, i);
            byteStream = new ByteArrayOutputStream(5000);
            try {
                oos = new ObjectOutputStream(new BufferedOutputStream(byteStream));
                oos.flush();//Flushes the stream. This will write any buffered output bytes and flush through to the underlying stream
                oos.writeObject(messageInfo);
                System.out.print(messageInfo);
                oos.flush();
            } catch (IOException e) {
                System.out.println("Error serializing object for transmission.");
                System.exit(-1);
            }
            //retrieves byte array
            byte[] sendBuf = byteStream.toByteArray();
            send(sendBuf, serverAddr, recvPort);// sending the data to server
        }
    }

    private void send(byte[] data, InetAddress destAddr, int destPort) {
        DatagramPacket datagramPacket;
        //build the datagram packet and send it to the server
        datagramPacket = new DatagramPacket(data, data.length, destAddr, destPort);
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            System.out.println("Error transmitting packet over network.");
            System.exit(-1);
        }
    }
}
