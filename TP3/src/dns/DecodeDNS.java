package dns;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DecodeDNS {

	public static void decodeMessage(DataInputStream dIS, ArrayList<Integer> ipList) throws IOException {
		dIS.skip(6); // skip ID, parameters and number of questions
		short nbResponses = dIS.readShort(); // retrieve the number of responses
		dIS.skip(4); // skip "Authority" and "Additional" fields
		skipToEndOfName(dIS); // skip each byte of the question
		dIS.skip(4); // skip type and class

		// retrieve the responses
		for (int i = 0; i < nbResponses; i++) {
			dIS.mark(1); // Save position in input stream
			byte offset = dIS.readByte();
			dIS.reset(); // Return to saved position in input stream
			if ((offset & 0xc0) == 0xc0) {
				dIS.skip(2); // skip the name field if there is an offset
			} else {
				skipToEndOfName(dIS); // skip each byte of the name
			}
			short requestType = dIS.readShort(); // retrieve the type of request
			dIS.skip(6); // skip class and TTL
			short dataSize = dIS.readShort(); // number of bytes in data field

			if (requestType == 1 && dataSize == 4) {
				// if data is an IPv4 address then add it in the list
				int ipAddress = dIS.readInt();
				ipList.add(ipAddress);
			} else {
				dIS.skip(dataSize);
			}
		}
	}

	public static void skipToEndOfName(DataInputStream input) throws IOException {
		byte nameLength = input.readByte();
		while (nameLength != 0) {
			input.skip(nameLength);
			nameLength = input.readByte();
		}
	}

}
