package com.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientImpl {

	interface ClientImplCallBack {
		void callback(String message);
	}

	private String serverAddr, chatName;

	private MulticastSocket clientReceiverSocket;
	private InetAddress multiSocketGroupAddr;
	private ClientImplCallBack cicb;

	public ClientImpl(ClientImplCallBack cicb, String serverAddr,
			String chatName) {

		this.serverAddr = serverAddr;
		this.chatName = chatName;
		this.cicb = cicb;

		try {
			this.clientReceiverSocket = new MulticastSocket(4446);
			this.multiSocketGroupAddr = InetAddress.getByName("230.0.0.1");

			clientReceiverSocket.joinGroup(multiSocketGroupAddr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startClientImpl() {
		new ReceiverThread(this.cicb).start();
	}
	
	public void sendPacket(String entered) {

		entered = chatName + " >> " + entered; // add the chat name to the
												// message

		byte[] buffer = entered.getBytes();

		InetAddress server = null;
		DatagramSocket socket = null;

		try {

			socket = new DatagramSocket();

			server = InetAddress.getByName(serverAddr);

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
					server, 9770);

			socket.send(packet);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		socket.close();
	}

	private String receivePackets() throws IOException {
		DatagramPacket packet;

		byte[] buf = new byte[60000];
		packet = new DatagramPacket(buf, buf.length);
		clientReceiverSocket.receive(packet);

		String received = new String(packet.getData(), 0, packet.getLength())
				.trim();
		System.out.println(received.length());

		// append(received.trim());
		return received.trim();
	}

	class ReceiverThread extends Thread {

		private ClientImplCallBack ciCallBack;

		public ReceiverThread(ClientImplCallBack ciCallBack) {
			this.ciCallBack = ciCallBack;
		}

		@Override
		public void run() {
			while (true) {
				try {
					ciCallBack.callback(receivePackets());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
