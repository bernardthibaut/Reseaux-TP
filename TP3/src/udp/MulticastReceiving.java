package udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiving {

	final static String ADDRESS = "224.0.0.1";
	final static int PORT = 7654;

	public static void main(String[] args) {
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
				System.out.println(messageReceived);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
