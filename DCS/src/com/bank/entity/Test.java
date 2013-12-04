/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.entity;

import com.bank.utils.CommunicationWrapper;
import com.bank.utils.Operation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kenny
 */
public class Test {

    public static void main(String[] args) throws SocketException, JSONException, UnknownHostException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CommunicationWrapper cw = new CommunicationWrapper(5000);
        if (cw.isBranchReachable("6650", "6651")) {
            JSONObject j = new JSONObject();
            j.put("operation", Operation.WITHDRAW);
            String message;
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
            content.put("port", 5500);
            content.put("bCode", "");
            content.put("address", InetAddress.getLocalHost().getHostAddress());
            j.put("content", content);
            Branch b = new Branch();
            b.setBranchCode("6651");
            String ip = b.obtainBranchIp(MySQLConnection.getConnection());
            cw.send(j, InetAddress.getByName(ip), 5000);
            JSONObject js = cw.receive();
            System.out.println(js.toString());
        }else{
            System.out.println("Branch unreacbable");
        }
    }
}
