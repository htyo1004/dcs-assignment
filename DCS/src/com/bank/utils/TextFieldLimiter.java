/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Kenny
 */
public class TextFieldLimiter extends DocumentFilter {

    private Pattern pattern;

    public TextFieldLimiter(String pattern) {
        super();
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
        Matcher m = pattern.matcher(newStr);
        if (m.matches()) {
            super.insertString(fb, offset, string, attr);
        } else {
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset,
            int length, String string, AttributeSet attr) throws
            BadLocationException {

        if (length > 0) {
            fb.remove(offset, length);
        }
        insertString(fb, offset, string, attr);
    }
}
