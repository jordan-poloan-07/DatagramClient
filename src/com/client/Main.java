package com.client;

import java.io.IOException;

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

			new Client(server, name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
