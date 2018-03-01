package fr.opensagres.xdocreport.document;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class RFC2397Container {
    final byte[] data;
    final String mimeType;

    public RFC2397Container(byte[] data, String mimeType) {
        this.data = data;
        this.mimeType = mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }
}
