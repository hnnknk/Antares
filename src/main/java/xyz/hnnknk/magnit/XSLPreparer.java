package xyz.hnnknk.magnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * The xsl file preparer.
 */
public class XSLPreparer {
    /**
     * The path to the directory for the xsl file.
     */
    private final String path;

    /**
     * Instantiates a new Xsl preparer.
     *
     * @param path the path
     */
    XSLPreparer(final String path) {
        this.path = path;
    }

    /**
     * Prepare xsl.
     */
    public final void prepareXSL() {

        try {
            DocumentBuilderFactory docFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("xsl:stylesheet");
            rootElement.setAttribute("xmlns:xsl",
                    "http://www.w3.org/1999/XSL/Transform");
            rootElement.setAttribute("version", "1.0");

            doc.appendChild(rootElement);
            Element em = doc.createElement("xsl:template");
            em.setAttribute("match", "/");

            Element entries = doc.createElement("entries");
            Element forEach = doc.createElement("xsl:for-each");
            forEach.setAttribute("select", "entries/entry");
            Element entry = doc.createElement("entry");
            Element attr = doc.createElement("xsl:attribute");
            attr.setAttribute("name", "field");
            Element value = doc.createElement("xsl:value-of");
            value.setAttribute("select", "field");

            attr.appendChild(value);
            entry.appendChild(attr);
            forEach.appendChild(entry);
            entries.appendChild(forEach);
            em.appendChild(entries);
            rootElement.appendChild(em);

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(new File(path));

            transformer.transform(source, file);
            System.out.println("Complete prepare a xsl "
                    + "file with path = " + path);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public final String getPath() {
        return path;
    }
}
