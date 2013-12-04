package com.bank.utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Toast extends JDialog {

    public static final long LENGTH_SHORT = 2000;
    public static final long LENGTH_LONG = 4000;
    private String text;
    private Component parent;
    private static JLabel message;
    private long duration;

    public Toast(Component root) {
        this.parent = root;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        addComponentListener(new ComponentAdapter() {
            // Give the window an rounded rect shape. LOOKS GOOD
            // If the window is resized, the shape is recalculated here.
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            }
        });

        setAlwaysOnTop(true);
        setUndecorated(true);
        getContentPane().setBackground(Color.BLACK);

        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        final boolean isTranslucencySupported =
                gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);

        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT)) {
            System.err.println("Shaped windows are not supported");
        }

        //If translucent windows aren't supported, 
        //create an opaque window.
        if (!isTranslucencySupported) {
            System.out.println(
                    "Translucency is not supported, creating an opaque window");
        }

        message = new JLabel();
        message.setForeground(Color.WHITE);
        add(message);
    }

    public static Toast makeText(Component root, String text, long duration) {
        Toast toast = new Toast(root);
        toast.setMessage(text);
        toast.setOpacity(0.0f);
        toast.setDuration(duration);
        FontMetrics fm = Toast.message.getFontMetrics(message.getFont());
        int textWidth = fm.stringWidth(text);
        toast.setSize(textWidth+30, 50);
        toast.setLocationRelativeTo(root);
        return toast;
    }
    
        public static Toast makeText(Component root,int height, String text, long duration) {
        Toast toast = new Toast(root);
        toast.setMessage(text);
        toast.setOpacity(0.0f);
        toast.setDuration(duration);
        FontMetrics fm = Toast.message.getFontMetrics(message.getFont());
        int textWidth = fm.stringWidth(text);
        toast.setSize(textWidth+30, height);
        toast.setLocationRelativeTo(root);
        return toast;
    }
    
    public void display() {
        new Thread() {
            @Override
            public void run() {
                try {
                    setVisible(true);
                    new Timer(25, new ActionListener() {
                        private int counter = 0;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            counter++;
                            if (counter == 8) {
                                ((Timer) e.getSource()).stop();
                            }
                            setOpacity(counter * 0.1f);
                        }
                    }).start();
                    Thread.sleep(duration);
                    new Timer(25, new ActionListener() {
                        private int counter = 8;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            counter--;
                            if (counter == 0) {
                                ((Timer) e.getSource()).stop();
                                close();
                            }
                            setOpacity(counter * 0.1f);
                        }
                    }).start();
                } catch (InterruptedException ex) {
                }
            }
        }.start();
    }
    
    public JLabel getMessage(){
        return message;
    }

    public void setMessage(String msg) {
        Toast.message.setText(msg);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void close() {
        setVisible(false);
        dispose();
    }
}