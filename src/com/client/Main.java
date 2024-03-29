package com.client;

import javax.swing.JOptionPane;

public class Main {
	
	public static void main(String[] args) {
		try {

			String server = null;

			String name = null;

			do {
				server = JOptionPane
						.showInputDialog("Enter the servers IPv4 address");
			} while (server == null || server.trim().equals(""));

			do {
				name = JOptionPane.showInputDialog("Enter your chat username");
			} while (name == null || name.trim().equals(""));

			Client c = new Client(server, name);
			c.callback("Welcome to the multicast socket chat program");
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			System.exit(0);
		}
	}
}
