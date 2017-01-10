package exo1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendUDP {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java SendUDP [HostAdress] [PortNumber] [Message]");
		}

		try {
			InetAddress hostAdress = InetAddress.getByName(args[0]);
			int port = Integer.parseInt(args[1]);
			byte[] message = args[2].getBytes();

			DatagramPacket packet = new DatagramPacket(message, message.length, hostAdress, port);
			DatagramSocket socket = new DatagramSocket();

			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
