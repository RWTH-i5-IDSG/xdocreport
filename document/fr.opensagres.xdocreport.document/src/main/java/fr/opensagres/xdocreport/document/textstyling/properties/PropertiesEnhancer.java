package fr.opensagres.xdocreport.document.textstyling.properties;

import org.xml.sax.Attributes;

import java.util.function.Supplier;

/**
 * @author Fabian Ohler <fabian.ohler1@rwth-aachen.de>
 */
public interface PropertiesEnhancer {
    <T extends ContainerProperties> T enhance(Attributes attributes, T properties, Supplier<T> supplier);
}
