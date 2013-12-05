/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Kenny
 */
public class FormattedMessage {

    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");

    public FormattedMessage() {
    }

    public String formatMessage(String operation, String message) {
        String temp = String.format("%-12s%-15s%-1s", format.format(new Date()), "[" + operation + "]", message);
        return temp;
    }
}
