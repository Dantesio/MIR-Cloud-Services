package mir.parsing.routing;

import mir.parsing.formatters.Encoder;
import mir.models.EncodedMessage;
import mir.models.ParsedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imohsenb.ISO8583.exceptions.ISOException;

import java.io.IOException;
import java.io.StringWriter;

public class Router {

    /*
	Returns the parsedMessage which is given from the hex.
	 */
    public static ParsedMessage getParsedMessage(String hex) throws ISOException {
        Encoder encoder = new Encoder();
        ParsedMessage parsedMessage = encoder.getParsedMessageFromHex(hex);
        return parsedMessage;
    }

    /*
    Returns the hex which is given from a parsedMessage.
     */
    public static String getHex(ParsedMessage parsedMessage) throws ISOException {
        Encoder encoder = new Encoder();
        String hex = encoder.getHex(parsedMessage);
        return hex;
    }
}
