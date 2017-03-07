package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveUDP {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java ReceiveUDP [PortNumber]");
		}

		try {
			int port = Integer.parseInt(args[0]);
			byte[] buf = new byte[1000];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			DatagramSocket socket = new DatagramSocket(port);

			System.out.println("Server started on port " + port);
			while (true) {
				socket.receive(packet);
				String messageReceived = new String(packet.getData(), 0, packet.getLength());
				System.out.println("RECEIVED: " + messageReceived);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
