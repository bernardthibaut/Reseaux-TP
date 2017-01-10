package exo2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSending {

	final static String ADDRESS = "224.0.0.1";
	final static int PORT = 7654;

	public static void main(String[] args) {
		try {
			InetAddress hostAdress = InetAddress.getByName(ADDRESS);
			MulticastSocket socket = new MulticastSocket(PORT);

			byte[] message = args[0].getBytes();
			DatagramPacket packet = new DatagramPacket(message, message.length, hostAdress, PORT);
			socket.send(packet);

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
