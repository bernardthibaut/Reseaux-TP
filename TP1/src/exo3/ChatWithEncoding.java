package exo3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class ChatWithEncoding {

	final static String ADDRESS = "224.0.0.1";
	final static int PORT = 7654;
	
	final static int ENCODING_KEY = 5;

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
						String messageReceived = decode(new String(packet.getData(), 0, packet.getLength()), ENCODING_KEY);
						String userID = packet.getAddress().getHostAddress();
						System.out.println("[" + userID + "]: " + messageReceived);
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
						String input = encode(sc.nextLine(), ENCODING_KEY);
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
