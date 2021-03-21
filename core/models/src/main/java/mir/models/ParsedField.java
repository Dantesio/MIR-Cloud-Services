package mir.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imohsenb.ISO8583.enums.FIELDS;
import com.imohsenb.ISO8583.exceptions.ISOException;
import com.imohsenb.ISO8583.utils.StringUtil;
import lombok.NoArgsConstructor;
import mir.models.check_annotations.AnyOf;

import java.util.HashMap;

@JsonAutoDetect
@NoArgsConstructor
public class ParsedField {

    @AnyOf(values = {"2", "4", "12", "13", "23", "42", "48", "49", "63"})
    private int id;
    private String type;
    // According to the MIP.
    private int lengthMIP;
    // Which the field has in the message.
    // Is different from the lengthMIP if the type of the field is "n" or "b".
    private int lengthReal;
    private String content;
    private boolean hasSubfields = false;
    private boolean hasElements = false;

    private HashMap<Integer, ParsedSubfield> subfields = new HashMap<Integer, ParsedSubfield>();
    private HashMap<Integer, ParsedElement> elements = new HashMap<Integer, ParsedElement>();

    // Getters and Setters.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLengthMIP() {
        return lengthMIP;
    }

    public void setLengthMIP(int lengthMIP) {
        this.lengthMIP = lengthMIP;
    }

    public int getLengthReal() {
        return lengthReal;
    }

    public void setLengthReal(int lengthReal) {
        this.lengthReal = lengthReal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getHasSubfields() { return this.hasSubfields;}

    public void setHasSubfields(boolean hasSubfields) { this.hasSubfields = hasSubfields;}

    public boolean getHasElements() {
        return this.hasElements;
    }

    public void setHasElements(boolean hasElements) {
        this.hasElements = hasElements;
    }

    public HashMap<Integer, ParsedSubfield> getSubfields() {
        return subfields;
    }

    public void setSubfields(HashMap<Integer, ParsedSubfield> subfields) {
        this.subfields = subfields;
    }

    public HashMap<Integer, ParsedElement> getElements() {
        return elements;
    }

    public void setElements(HashMap<Integer, ParsedElement> elements) {
        this.elements = elements;
    }
    // The end of the Getters and Setters.

    @JsonIgnore
    public String getBodyOrElementsHexStr() throws ISOException {
        // The parsedField contains content in its elements.
        if (hasElements)
            return getElementsHexStr();
            // The parsedField contains content in the body directly.
        else {
            if (type == "n" || type == "b")
                return content;
            else
                return StringUtil.asciiToHex(content);
        }
    }

    @JsonIgnore
    public String getBodyOrElementsStr() throws ISOException {
        if (hasElements)
            // The parsedField contains content in its elements.
            return getElementsStr();
        else
            // The parsedField contains content in the body directly.
            return content;
    }

    private String getElementsHexStr() throws ISOException {
        if (!hasElements)
            throw new ISOException("Field has not elements!");
        StringBuilder elementsStr = new StringBuilder();
        for (Integer id : elements.keySet())
            elementsStr.append(elements.get(id).getHexString());
        return elementsStr.toString();
    }

    private String getElementsStr() throws ISOException {
        if (!hasElements)
            throw new ISOException("Field has not elements!");
        StringBuilder elementsStr = new StringBuilder();
        for (Integer id : elements.keySet())
            elementsStr.append(elements.get(id).toString());
        return elementsStr.toString();
    }

    /*
    Returns the prefix of the length of the field if it has the mutable length.
    The length of a mutable field is represented by 4 decimal numbers.
    If the length takes not all the ranks, it has the front zeros.
     */
    @JsonIgnore
    public String getFieldLengthStr() throws ISOException {
        FIELDS fieldImage = FIELDS.valueOf(id);
        if (fieldImage.isFixed())
            throw new ISOException("Field has not the prefix of the length!");
        if (content == null)
            throw new ISOException("Field has not a body!");
        int length = content.length();
        StringBuilder lengthStr = new StringBuilder(Integer.toString(length));
        while(lengthStr.length() < 4)
            lengthStr.insert(0, 0);
        return lengthStr.toString();
    }
}
