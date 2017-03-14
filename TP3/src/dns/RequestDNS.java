package dns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class RequestDNS {

	private static final int BUFFER_SIZE = 4096;
	private static final String DNS_ADDRESS = "193.49.225.90";
	private static final int PORT = 53;

	private static void sendRequest(DatagramSocket socket, String symbolicAddress) throws IOException {
		ByteArrayOutputStream baOS = new ByteArrayOutputStream(BUFFER_SIZE);
		DataOutputStream dOS = new DataOutputStream(baOS);
		EncodeDNS.encodeMessage(dOS, symbolicAddress);
		byte[] data = baOS.toByteArray();

		InetAddress address = InetAddress.getByName(DNS_ADDRESS);

		DatagramPacket request = new DatagramPacket(data, data.length, address, PORT);
		socket.send(request);
	}

	private static ArrayList<Integer> getResponses(DatagramSocket socket) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		ByteArrayInputStream baIS = new ByteArrayInputStream(buffer);
		DataInputStream dIS = new DataInputStream(baIS);
		DatagramPacket response = new DatagramPacket(buffer, buffer.length);
		socket.receive(response);

		ArrayList<Integer> ipList = new ArrayList<Integer>();
		DecodeDNS.decodeMessage(dIS, ipList);

		return ipList;
	}

	public static ArrayList<Integer> printIPList(String symbolicAddress) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		sendRequest(socket, symbolicAddress);
		System.out.println("Request sent to \"" + symbolicAddress + "\"");

		System.out.println("Received requests : ");
		ArrayList<Integer> responses = getResponses(socket);
		for (int ip : responses) {
			byte[] bytes = BigInteger.valueOf(ip).toByteArray();
			InetAddress adr = InetAddress.getByAddress(bytes);
			System.out.println(" - " + adr.getHostAddress());
		}

		socket.close();
		return responses;
	}

	public static void main(String[] args) throws IOException {
		RequestDNS.printIPList("www.lifl.fr");
	}

}
