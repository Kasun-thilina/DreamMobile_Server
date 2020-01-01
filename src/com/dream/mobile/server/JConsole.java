package com.dream.mobile.server;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

public class JConsole {

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("JConsole");
        JTextPane jta = new JTextPane();
        JButton button = new JButton("Run");
        frame.setLayout(new BorderLayout());
        frame.add(button,BorderLayout.NORTH);
        frame.add(jta,BorderLayout.CENTER);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Object>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        outputTest("inner");
                        return null;
                    }}.execute();
            }});

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        console(jta);
    }

    public static void outputTest(String msg){
    	System.out.println("print line");
        for(int i=0;i<10;i++){
            System.out.println(i+" test"+msg);

            System.out.println(i+" ssssss"+msg);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void console(final JTextPane area) throws IOException {
        area.setContentType("text/html");
        final PipedInputStream outPipe = new PipedInputStream();
        System.setOut(new PrintStream(new PipedOutputStream(outPipe), true));       
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                Scanner s = new Scanner(outPipe);
                while (s.hasNextLine()){
                    String line = s.nextLine();
                    publish(line + "\n");
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String line : chunks){
                    area.setText("<font size=\"3\" color=\"red\">"+line+"</font>");
                }
            }
        }.execute();
    }

}