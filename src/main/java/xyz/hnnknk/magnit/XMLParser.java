package xyz.hnnknk.magnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * The xml parser.
 */
public class XMLParser {

    /**
     * Custom number of iterations of insertion
     * into the database.
     */
    private int number;
    /**
     * Xml file name.
     */
    private String xmlPath = "1.xml";
    /**
     * Xsl file name.
     */
    private String xslPath = "1.xsl";
    /**
     * Xml file name after transform using xsl.
     */
    private String transformedXMLpath = "2.xml";
    /**
     * Directory path for storing xml files.
     */
    private String directoryPath = "C:/";
    /**
     * Jdbc connection string.
     */
    private String jdbcConnectionString;
    /**
     * Support class for preparing xsl schema.
     */
    private XSLPreparer xslPreparer;
    /**
     * Support class for creating xml files.
     */
    private XMLPreparer xmlPreparer;

    /**
     * Instantiates a new Xml parser.
     *
     * @param number               the number
     * @param jdbcConnectionString the jdbc connection string
     */
    public XMLParser(final int number, final String jdbcConnectionString) {
        this.number = number;
        this.jdbcConnectionString = jdbcConnectionString;
    }

    /**
     * Init xslPreparer and xmlPreparer
     */
    private void initHelpers() {
        xslPreparer = new XSLPreparer(directoryPath + xslPath);
        xmlPreparer = new XMLPreparer(directoryPath + xmlPath,
                directoryPath + transformedXMLpath);
    }

    /**
     * Main method for calculating a sum.
     */
    public final void start() {

        Database.createNewDatabase(jdbcConnectionString);
        Database.createTable();

        initHelpers();

        insert(number);

        xslPreparer.prepareXSL();

        xmlPreparer.createAndSaveXML();
        xmlPreparer.transformByXsl(xslPreparer.getPath());

        System.out.println(sum(xmlPreparer.getTransformXMLPath()));
    }

    /**
     * Inserts the numbers in the database.
     *
     * @param input the number from user
     */
    private void insert(final int input) {
        String insertTableSQL = "INSERT INTO TEST"
                + "(TEST_FIELD) VALUES"
                + "(?)";
        Database.dbExecutePrepareStatement(insertTableSQL, input);
    }

    /**
     * Calculates sum from xml file.
     *
     * @param transormedXML the xml file after xsl transformation
     */
    public long sum(final String transormedXML) {
        long sum = 0;
        try {
            File fXmlFile = new File(transormedXML);
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            NodeList nList = doc.getElementsByTagName("entry");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    sum += Long.parseLong(eElement.getAttribute("field"));

                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Error while calculating a sum");
        }
        return sum;

    }

    /**
     * Instantiates a new Xml parser.
     */
    public XMLParser() {
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public final int getNumber() {
        return number;
    }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public final void setNumber(final int number) {
        this.number = number;
    }

    /**
     * Gets jdbc connection string.
     *
     * @return the jdbc connection string
     */
    public final String getJdbcConnectionString() {
        return jdbcConnectionString;
    }

    /**
     * Sets jdbc connection string.
     *
     * @param jdbcConnectionString the jdbc connection string
     */
    public final void setJdbcConnectionString(final String jdbcConnectionString) {
        this.jdbcConnectionString = jdbcConnectionString;
    }

    /**
     * Gets xml path.
     *
     * @return the xml path
     */
    public final String getXmlPath() {
        return xmlPath;
    }

    /**
     * Sets xml path.
     *
     * @param xmlPath the xml path
     */
    public final void setXmlPath(final String xmlPath) {
        this.xmlPath = xmlPath;
    }

    /**
     * Gets xsl path.
     *
     * @return the xsl path
     */
    public final String getXslPath() {
        return xslPath;
    }

    /**
     * Sets xsl path.
     *
     * @param xslPath the xsl path
     */
    public final void setXslPath(final String xslPath) {
        this.xslPath = xslPath;
    }

    /**
     * Gets transformed xm lpath.
     *
     * @return the transformed xm lpath
     */
    public final String getTransformedXMLpath() {
        return transformedXMLpath;
    }

    /**
     * Sets transformed xm lpath.
     *
     * @param transformedXMLpath the transformed xm lpath
     */
    public final void setTransformedXMLpath(final String transformedXMLpath) {
        this.transformedXMLpath = transformedXMLpath;
    }

    /**
     * Gets directory path.
     *
     * @return the directory path
     */
    public final String getDirectoryPath() {
        return directoryPath;
    }

    /**
     * Sets directory path.
     *
     * @param directoryPath the directory path
     */
    public final void setDirectoryPath(final String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
