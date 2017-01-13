package exo3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class Chat {

	final static String ADDRESS = "224.0.0.1";
	final static int PORT = 7654;

	public static void main(String[] args) {
		Thread sender = new Thread() {
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
						String userID = packet.getAddress().getHostAddress();
						System.out.println("[" + userID + "]: " + messageReceived);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		};

		Thread receiver = new Thread() {
			public void run() {
				try {
					InetAddress hostAdress = InetAddress.getByName(ADDRESS);
					MulticastSocket socket = new MulticastSocket(PORT);

					while (true) {
						Scanner sc = new Scanner(System.in);
						byte[] message = sc.nextLine().getBytes();
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
}
