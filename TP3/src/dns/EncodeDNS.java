package dns;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class EncodeDNS {

	public static void encodeMessage(DataOutputStream dOS, String symbolicAddress) throws IOException {
		dOS.writeShort(0x08bb); // ID
		dOS.writeShort(0x0100); // parameters
		dOS.writeShort(0x0001); // number of questions
		dOS.writeShort(0x0000); // number of responses
		dOS.writeShort(0x0000); // number of entries in the "Authority" field
		dOS.writeShort(0x0000); // number of entries in the "Additional"
									// field

		// encode the symbolic address
		for (String word : symbolicAddress.split(Pattern.quote("."))) {
			byte wordLength = (byte) word.length();
			dOS.writeByte(wordLength);
			dOS.write(word.getBytes());
		}
		dOS.writeByte(0x00); // end of name

		dOS.writeShort(0x0001); // type of request
		dOS.writeShort(0x0001); // type of protocole
	}

}
