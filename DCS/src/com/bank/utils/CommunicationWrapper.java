/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kenny
 */
public class CommunicationWrapper {

    byte[] dataReceived = new byte[65536];
    byte[] dataToSend = new byte[65536];
    DatagramPacket packetReceived = new DatagramPacket(dataReceived, dataReceived.length);
    DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length);
    DatagramSocket udpSocket;
    private String textFile = "/com/bank/InterconnectionGraph.txt";

    /**
     * Create a new wrapper class for the network communication between either
     * client to server or server to server
     * 
     * @param port Port to be binded to the socket
     * @throws SocketException 
     */
    public CommunicationWrapper(int port) throws SocketException {
        udpSocket = new DatagramSocket(port);
    }

    /**
     * Accept data from client side and pass the data to the server for
     * processing
     *
     * @param data a json object to be sent to the server to process
     * @param branchURL the destination url
     * @param port server's port number
     * @see DatagramSocket
     */
    public void send(JSONObject data, InetAddress branchURL, int port) {
        try {
            String json = data.toString();
            dataToSend = json.getBytes();
            sendPacket.setData(dataToSend);
            sendPacket.setAddress(branchURL);
            sendPacket.setPort(port);
            udpSocket.send(sendPacket);
        } catch (UnknownHostException ex) {
            System.out.println("Unknown host name " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Receive data processed by server and returnthe data to the invoker
     *
     * @return a json object that contain the processed data
     * @see DatagramSocket
     */
    public JSONObject receive() {
        JSONObject json = new JSONObject();
        try {
            try {
                udpSocket.receive(packetReceived);
                String data = new String(packetReceived.getData());
                json = new JSONObject(data);
            } catch (JSONException ex) {
                json.put("error", true);
                json.put("message", ex.getMessage());
            } catch (SocketTimeoutException ex) {
                json.put("error", true);
                json.put("message", ex.getMessage());
            } catch (IOException ex) {
                json.put("error", true);
                json.put("message", ex.getMessage());
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    /**
     * Close the UDP datagram socket
     *
     * @see DatagramSocket
     */
    public void close() {
        udpSocket.close();
    }

    /**
     * Accept two parameters and check if the source branch can reach the 
     * destination branch or not by reading into the graph file
     *
     * @param source The source branch
     * @param destination The destination branch
     * @return true if the branch is reachable, false otherwise
     */
    public boolean isBranchReachable(String source, String destination) {
        boolean reachable = false;
        try {
            FileInputStream graphFile = new FileInputStream(new File(this.getClass().getResource(textFile).getPath()));
            DataInputStream input = new DataInputStream(graphFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                if (source.equals(data[0])) {
                    reachable = destination.equals(data[1]);
                    break;
                }
            }
        } catch (IOException ex) {
        }
        return reachable;
    }

    /**
     * Check the reachable neighbor of current branch by looking into the 
     * graph file
     *
     * @param branch The current branch
     * @return The neighbor of current branch
     */
    public String whoIsNeighbors(String branch) {
        String neighbor = null;
        try {
            FileInputStream graphFile = new FileInputStream(new File(this.getClass().getResource(textFile).getPath()));
            DataInputStream input = new DataInputStream(graphFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                if (branch.equalsIgnoreCase(data[0])) {
                    neighbor = data[1];
                    break;
                }
            }
        } catch (IOException ex) {
        }
        return neighbor;
    }
}
