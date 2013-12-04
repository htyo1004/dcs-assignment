/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.server;

import com.bank.utils.CommunicationWrapper;
import com.bank.utils.Operation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Moofie
 */
public class BankServer{

    public BankServer() {
    }
    
    public static void main(String[] args) throws IOException, JSONException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter port number: ");
        int socket = Integer.parseInt(br.readLine());
        CommunicationWrapper cw = new CommunicationWrapper(socket);
        System.out.println(cw.whoIsNeighbors("B2"));
        System.out.println("Enter operation");
        System.out.println("1. Send item");
        System.out.println("2. Receive Item");
        System.out.println("3. Exit");
        String option = br.readLine();
        switch(option){
            case "1":
                System.out.println("Enter receiver's port no:");
                int port = Integer.parseInt(br.readLine());
                System.out.println("Enter something:");
                String message;
                JSONObject json = new JSONObject();
                json.put("operation", Operation.WITHDRAW);
                JSONObject content = new JSONObject();
                System.out.println("Enter account number: ");
                message = br.readLine();
                content.put("accNo", message);
                System.out.println("Enter IC number: ");
                message = br.readLine();
                content.put("icNo", message);
                System.out.println("Enter Amount to withdraw : ");
                message = br.readLine();
                content.put("amount", Double.parseDouble(message));
                content.put("port", socket);
                content.put("address", InetAddress.getLocalHost().getHostAddress());
                json.put("content", content);
                cw.send(json, InetAddress.getLocalHost(), port);
                JSONObject result = cw.receive();
                System.out.println(result.toString());
                break;
            case "2":
                System.out.println("waiting for message..");
                JSONObject json2 = cw.receive();
                System.out.println(json2.toString());
                break;
            case "3":
                System.exit(1);
                break;
        }
    }
}
