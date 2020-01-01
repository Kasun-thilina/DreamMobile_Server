package com.dream.mobile.server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.text.Style;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.dream.mobile.rmi.interfaces.DbFunctions;
import com.dream.mobile.rmi.interfaces.RemoteInterface;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.SystemColor;

public class ServerUI {

	private JFrame frame;
	public ArrayList<String> consoleText = new ArrayList<String>();
	public static JTextPane jConsole;
	public static StyledDocument doc;
	public static Style style;
	public static StyleConstants sc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI window = new ServerUI();
					window.frame.setVisible(true);
					doc = jConsole.getStyledDocument();
					style = jConsole.addStyle("Style", null);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.info);
		frame.setBounds(100, 100, 558, 355);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.setBounds(93, 18, 117, 29);
		frame.getContentPane().add(btnStartServer);

		JButton btnEndServer = new JButton("Stop Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				print("Starting the server....",Color.GREEN);
				startServer();
			}
		});
		btnEndServer.setBounds(389, 18, 117, 29);
		frame.getContentPane().add(btnEndServer);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(32, 55, 501, 251);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 500, 250);
		panel.add(scrollPane);

		jConsole = new JTextPane();
		jConsole.setBackground(Color.DARK_GRAY);
		jConsole.setEditable(false);
		scrollPane.setViewportView(jConsole);
	}

	public static void printLine(String text, Color color) {

		if (color != null) {
			StyleConstants.setForeground(style, color);
			try {
				doc.insertString(doc.getLength(), " " + text + "\n", style);
			} catch (BadLocationException e) {
			}
		} else {
			StyleConstants.setForeground(style, Color.WHITE);
			try {
				doc.insertString(doc.getLength(), " " + text + "\n", style);
			} catch (BadLocationException e) {
			}

		}
	}
	
	public static void print(String text, Color color) {

		if (color != null) {
			StyleConstants.setForeground(style, color);
			try {
				doc.insertString(doc.getLength(), " " + text, style);
			} catch (BadLocationException e) {
			}
		} else {
			StyleConstants.setForeground(style, Color.WHITE);
			try {
				doc.insertString(doc.getLength(), " " + text, style);
			} catch (BadLocationException e) {
			}

		}
	}

	public Boolean startServer() {
		try {
			// Instantiating the implementation class
			DbFunctions dbObject = new DbFunctions();

			// Exporting the object of implementation class (here we are exporting the
			// remote object to the stub)
			RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(dbObject, 0);
			// the port and hostname for the server
			int port = 1091;
			String hostname = "0.0.0.0";

			String bindURL = "//" + hostname + ":" + port + "/Server";

			// Binding the remote object (stub) in the registry
			Registry registry = LocateRegistry.createRegistry(port);

			// To bind with custom URL
			Naming.bind(bindURL, dbObject);
			printLine("Server ready at URL: " + bindURL, Color.GREEN);
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
