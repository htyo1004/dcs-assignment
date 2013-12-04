/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public CommunicationWrapper(int port) throws SocketException {
        udpSocket = new DatagramSocket(port);
    }

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
            Logger.getLogger(CommunicationWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JSONObject receive() {
        JSONObject json = new JSONObject();
        try {
            udpSocket.receive(packetReceived);
            String data = new String(packetReceived.getData());
            json = new JSONObject(data);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public void close() {
        udpSocket.close();
    }

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
