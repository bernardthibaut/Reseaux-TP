package exo3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

public class ChatWithEncoding {

	final static String ADDRESS = "224.0.0.2";
	final static int PORT = 7654;

	final static String USERNAME = "Thibaut";

	public static void main(String[] args) {

		Thread receiver = new Thread() {
			public void run() {
				try {
					byte[] buf = new byte[1000];

					InetAddress hostAdress = InetAddress.getByName(ADDRESS);
					MulticastSocket socket = new MulticastSocket(PORT);
					socket.joinGroup(hostAdress);

					System.out.println("Server started");
					while (true) {
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
						socket.receive(packet);
						String messageReceived = new String(packet.getData(), 0, packet.getLength());
						if (!isGoodFormat(messageReceived)) {
							System.err.println("Message received is wrong format");
							continue;
						}

						String[] parts = messageReceived.split(":");

						if (parts.length > 3) {
							for (int i = 3; i < parts.length; i++) {
								parts[2] += ":" + parts[i];
							}
						}

						int decoding_key = Integer.parseInt(parts[0]);

						System.out.println("[" + parts[1] + "]: " + decode(parts[2], decoding_key));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		};

		Thread sender = new Thread() {
			public void run() {
				try {
					InetAddress hostAdress = InetAddress.getByName(ADDRESS);
					MulticastSocket socket = new MulticastSocket(PORT);

					while (true) {
						Scanner sc = new Scanner(System.in);
						String input = createMessage(generateKey(), USERNAME, sc.nextLine());
						byte[] message = input.getBytes();
						DatagramPacket packet = new DatagramPacket(message, message.length, hostAdress, PORT);
						socket.send(packet);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		};

		sender.start();
		receiver.start();
	}

	public static boolean isGoodFormat(String message) {
		String[] parts = message.split(":");

		if (parts.length != 3)
			return false;

		try {
			Integer.parseInt(parts[0]);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	public static String createMessage(int key, String username, String text) {
		return key + ":" + username + ":" + encode(text, key);
	}

	public static String getMacAddress() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			Enumeration<NetworkInterface> network = NetworkInterface.getNetworkInterfaces();
			while (network.hasMoreElements()) {
				NetworkInterface ni = network.nextElement();
				byte[] mac = ni.getHardwareAddress();
				if (mac != null) {
					String res = "";
					for (int i = 0; i < mac.length; i++) {
						res += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
					}
					return res;
				}
			}
		} catch (UnknownHostException | SocketException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static int generateKey() {
		String[] nbs = getMacAddress().split("-");
		return Integer.parseInt(nbs[nbs.length - 1], 16);
	}

	public static String encode(String toEncode, int key) {
		String res = "";
		for (int i = 0; i < toEncode.length(); i++) {
			res += (char) ((int) (toEncode.charAt(i) + key));
		}
		return res;
	}

	public static String decode(String toDecode, int key) {
		String res = "";

		for (int i = 0; i < toDecode.length(); i++) {
			res += (char) ((int) (toDecode.charAt(i) - key));
		}

		return res;
	}

}
