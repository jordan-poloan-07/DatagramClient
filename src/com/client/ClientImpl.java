package com.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClientImpl {

	interface ClientImplCallBack {
		void callback(String message);
	}

	private String serverAddr, chatName;

	private MulticastSocket clientReceiverSocket;
	private InetAddress multiSocketGroupAddr;
	private ClientImplCallBack clientGuiCallback;

	public ClientImpl(ClientImplCallBack guiCallback, String serverAddr,
			String chatName) throws Exception {

		this.serverAddr = serverAddr;
		this.chatName = chatName;
		this.clientGuiCallback = guiCallback;

		this.clientReceiverSocket = new MulticastSocket(4446);
		this.multiSocketGroupAddr = InetAddress.getByName("230.0.0.1");

		clientReceiverSocket.joinGroup(multiSocketGroupAddr);

	}

	public void startClientImpl() {
		new ReceiverThread(clientGuiCallback).start();
	}

	public void sendPacket(String entered) {

		InetAddress server = null;
		DatagramSocket socket = null;

		try {

			socket = new DatagramSocket();

			server = InetAddress.getByName(serverAddr);

			entered = socket.getLocalAddress().getHostAddress() + " "
					+ chatName + " >> " + entered;

			byte[] buffer = entered.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
					server, 9770);

			socket.send(packet);

		} catch (Exception e) {
			clientGuiCallback.callback(e.getLocalizedMessage());
		}

		socket.close();

	}

	private String receivePackets() {

		DatagramPacket packet;

		byte[] buf = new byte[60000];

		packet = new DatagramPacket(buf, buf.length);

		// received will check receive the exception's message
		// or the messages from the socket

		String received = null;

		try {
			clientReceiverSocket.receive(packet);
		} catch (IOException e) {
			received = e.getLocalizedMessage();
		}

		received = new String(packet.getData(), 0, packet.getLength()).trim();

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
				ciCallBack.callback(receivePackets());
			}
		}

	}
}
