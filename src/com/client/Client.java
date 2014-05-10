package com.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.client.ClientImpl.ClientImplCallBack;

public class Client implements ActionListener, ClientImplCallBack {

	private JFrame frame;
	private JTextField chatInput;
	private JTextArea chatField;

	private ClientImpl cImpl;

	public Client(String hostaddress, String name) throws Exception {

		frame = new JFrame("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 600);

		chatInput = new JTextField();
		chatField = new JTextArea();
		chatField.setEditable(false);

		frame.add(new JScrollPane(chatField));
		frame.add(chatInput, BorderLayout.SOUTH);

		chatInput.addActionListener(this);

		frame.setVisible(true);

		cImpl = new ClientImpl(this, hostaddress, name);
		cImpl.startClientImpl();
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		String entered = evt.getActionCommand();
		
		chatInput.setText("");

		cImpl.sendPacket(entered);
	
	}

	@Override
	public void callback(String message) {
		append(message); 
	}

	private void append(String message) {
		chatField.append(message + "\n");
	}

}
