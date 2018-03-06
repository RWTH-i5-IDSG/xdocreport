package fr.opensagres.xdocreport.document.textstyling.properties;

import fr.opensagres.xdocreport.document.textstyling.html.HTMLTextStylingContentHandler;
import fr.opensagres.xdocreport.document.textstyling.html.StylesHelper;
import org.xml.sax.Attributes;

import java.util.function.Supplier;

/**
 * This class can be used to extract additional attributes of elements and set/overwrite properties for which the
 * implementations in {@link HTMLTextStylingContentHandler} and {@link StylesHelper} are not sufficient (For example:
 * to parse "class" attribute).
 *
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public interface PropertiesEnhancer {

    /**
     * @param attributes The attributes of the element
     * @param properties The properties which the standard implementation sets after processing (can be null)
     * @param supplier   A supplier to lazy-init properties, if standard implementation's properties is null
     */
    <T extends ContainerProperties> T enhance(Attributes attributes, T properties, Supplier<T> supplier);
}
