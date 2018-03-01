package fr.opensagres.xdocreport.document;

import javax.activation.MimeType;
import javax.activation.MimeTypeParameterList;
import javax.activation.MimeTypeParseException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class RFC2397Parser {

    private static final String DATA = "data:";
    private static final String BASE64 = ";base64";
    private static final String CHARSET = "charset";

    public static RFC2397Container parseDataURL(final String url)
            throws MimeTypeParseException, UnsupportedEncodingException {
        if (!url.startsWith(DATA)) {
            return null;
        }
        final int commaPosition = url.indexOf(",");
        final String mediaTypeOrBase64 = url.substring(DATA.length(), commaPosition);
        final String data = url.substring(commaPosition + 1);

        final byte[] decodedData;
        final String mimeType;

        final Charset charset;
        final boolean base64 = mediaTypeOrBase64.endsWith(BASE64);

        final String mediaType = base64
                ? mediaTypeOrBase64.substring(0, mediaTypeOrBase64.length() - BASE64.length())
                : mediaTypeOrBase64;

        if (mediaTypeOrBase64.length() == 0) {
            // no mime type and charset specified, use defaults
            mimeType = "text/plain";
            charset = StandardCharsets.US_ASCII;
        } else if (mediaType.startsWith(";")) {
            // if the media type starts with ';', no mime type is given, but parameters are present (possibly including the charset)
            mimeType = "text/plain";
            final MimeTypeParameterList mimeTypeParameterList = new MimeTypeParameterList(mediaType);
            final String charsetName = mimeTypeParameterList.get(CHARSET);
            charset = null == charsetName ? StandardCharsets.US_ASCII : Charset.forName(charsetName);
        } else {
            final MimeType jMimeType = new MimeType(mediaType);
            mimeType = jMimeType.getBaseType();
            final String charsetName = jMimeType.getParameter(CHARSET);
            charset = null == charsetName ? StandardCharsets.US_ASCII : Charset.forName(charsetName);
        }

        final byte[] bytes = data.getBytes(charset);
        decodedData = base64 ? Base64.getDecoder().decode(bytes) : URLDecoder.decode(data, charset.name()).getBytes();

        return new RFC2397Container(decodedData, mimeType);
    }

}
