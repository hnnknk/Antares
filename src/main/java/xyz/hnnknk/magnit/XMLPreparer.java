package xyz.hnnknk.magnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The xml files preparer.
 */
public class XMLPreparer {
    /**
     * The path to the directory for the xml file.
     */
    private final String path;
    /**
     * The path to the directory for the transformed
     * xml file usinf xslt.
     */
    private final String transformXMLPath;

    /**
     * Instantiates a new Xml preparer.
     *
     * @param path             the path to the directory
     * @param transformXMLPath the path to the directory for transformed xml
     */
    XMLPreparer(final String path, final String transformXMLPath) {
        this.path = path;
        this.transformXMLPath = transformXMLPath;
    }

    /**
     * Create and save xml.
     */
    public final void createAndSaveXML()  {

        DocumentBuilderFactory docFactory =
                DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element personRootElement = doc.createElement("entries");
            doc.appendChild(personRootElement);

            String selectStmt = "SELECT * FROM TEST";
            ResultSet rsField = Database.dbExecuteQuery(selectStmt);
            while (rsField.next()) {
                Element entry  = doc.createElement("entry");
                Element field = doc.createElement("field");
                field.appendChild(doc.createTextNode(
                        Integer.toString(rsField.getInt(1))));
                entry.appendChild(field);
                personRootElement.appendChild(entry);
            }

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(new File(path));

            transformer.transform(source, file);
            System.out.println("Complete prepare a xml file with path = "
                    + path);
        } catch (ParserConfigurationException | TransformerException
                | SQLException  e) {
            e.printStackTrace();
        }
    }

    /**
     * Transform xml file using xsl.
     *
     * @param xslPath the xsl file path
     */
    public final void transformByXsl(final String xslPath) {

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File(xslPath));
            Transformer transformer = factory.newTransformer(xslt);

            Source text = new StreamSource(new File(path));
            transformer.transform(text, new StreamResult(
                    new File(transformXMLPath)));
        } catch (TransformerException e) {
            System.out.println("Error while transform one xml to another");
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

    /**
     * Gets transform xml path.
     *
     * @return the transform xml path
     */
    public final String getTransformXMLPath() {
        return transformXMLPath;
    }
}
