package fr.opensagres.xdocreport.document;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.document.RFC2397Container;
import fr.opensagres.xdocreport.document.RFC2397Parser;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import org.junit.Test;

import javax.activation.MimeTypeParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for RFC2397Parser.
 * Example inputs taken from https://tools.ietf.org/html/rfc2397.
 *
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public class RFC2397ParserTest {

    @Test
    public void parseInlineImage() throws MimeTypeParseException, UnsupportedEncodingException {
        final RFC2397Container container
                = RFC2397Parser.parseDataURL("data:image/gif;base64,R0lGODdhMAAwAPAAAAAAAP///ywAAAAAMAAwAAAC8IyPqcvt3wCcDkiLc7C0qwyGHhSWpjQu5yqmCYsapyuvUUlvONmOZtfzgFzByTB10QgxOR0TqBQejhRNzOfkVJ+5YiUqrXF5Y5lKh/DeuNcP5yLWGsEbtLiOSpa/TPg7JpJHxyendzWTBfX0cxOnKPjgBzi4diinWGdkF8kjdfnycQZXZeYGejmJlZeGl9i2icVqaNVailT6F5iJ90m6mvuTS4OK05M0vDk0Q4XUtwvKOzrcd3iq9uisF81M1OIcR7lEewwcLp7tuNNkM3uNna3F2JQFo97Vriy/Xl4/f1cf5VWzXyym7PHhhx4dbgYKAAA7");
        assertNotNull(container);
        assertEquals("image/gif", container.getMimeType());
    }

    @Test
    public void parsePlainText() throws MimeTypeParseException, UnsupportedEncodingException {
        final RFC2397Container container
                = RFC2397Parser.parseDataURL("data:,A%20brief%20note");
        assertNotNull(container);
        assertEquals("text/plain", container.getMimeType());
        assertEquals("A brief note", new String(container.data, StandardCharsets.US_ASCII));
    }

    // example seems broken: %fg is not hex
//    @Test
//    public void parseGreekCharacters() throws MimeTypeParseException, UnsupportedEncodingException {
//        final RFC2397Container container
//                = RFC2397Parser.parseDataURL("data:text/plain;charset=iso-8859-7,%be%fg%be");
//        assertNotNull(container);
//        assertEquals("text/plain", container.getMimeType());
//        assertEquals("A brief note", new String(container.data, Charset.forName("iso-8859-7")));
//    }

    @Test
    public void parseDatabaseQuery() throws MimeTypeParseException, UnsupportedEncodingException {
        final RFC2397Container container
                = RFC2397Parser.parseDataURL("data:application/vnd-xxx-query,select_vcount,fcol_from_fieldtable/local");
        assertNotNull(container);
        assertEquals("application/vnd-xxx-query", container.getMimeType());
        assertEquals("select_vcount,fcol_from_fieldtable/local", new String(container.data, StandardCharsets.US_ASCII));
    }

    public static void main(String[] args) throws IOException {
        final URL url = new URL("https://www.google.de/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
        final URLConnection urlConnection = url.openConnection();
        try (final InputStream inputStream = urlConnection.getInputStream()) {
            final ByteArrayImageProvider byteArrayImageProvider = new ByteArrayImageProvider(inputStream);
            byteArrayImageProvider.setUseImageSize(true);
        }
    }
}