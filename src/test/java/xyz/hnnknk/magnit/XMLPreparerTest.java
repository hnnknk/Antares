package xyz.hnnknk.magnit;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlunit.matchers.CompareMatcher;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

import static org.junit.Assert.*;

public class XMLPreparerTest {

    private XMLPreparer x;

    @Before
    public void setUp() {
        x = new XMLPreparer("C:/users/savineu/1.xml", "C:/users/savineu/2.xml");
    }

    private void prepareXsl() throws IOException {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><xsl:stylesheet " +
                "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:template match=\"/\">" +
                "<entries><xsl:for-each select=\"entries/entry\"><entry><xsl:attribute name=\"field\">" +
                "<xsl:value-of select=\"field\"/></xsl:attribute></entry></xsl:for-each></entries>" +
                "</xsl:template></xsl:stylesheet>";
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:/users/savineu/2.xsl"));
        writer.write(str);

        writer.close();
    }

    private void prepareXml() throws IOException {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<entries>\n" +
                "<entry>\n" +
                "<field>1</field>\n" +
                "</entry>\n" +
                "</entries>";
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:/users/savineu/1.xml"));
        writer.write(str);

        writer.close();
    }

    private void initDb() {
        Database.createNewDatabase("C:/sqlite/test.db");
        Database.createTable();
        String insertTableSQL = "INSERT INTO TEST"
                + "(TEST_FIELD) VALUES"
                + "(?)";
        Database.dbExecutePrepareStatement(insertTableSQL, 1);
    }

    @Test
    public void createAndSaveXML() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        initDb();
        x.createAndSaveXML();

        File fXmlFile = new File(x.getPath());
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.toString();

        String controlXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<entries>\n" +
                "<entry>\n" +
                "<field>1</field>\n" +
                "</entry>\n" +
                "</entries>";
        assertThat(output, CompareMatcher.isIdenticalTo(controlXml));
    }

    @Test
    public void transformByXsl() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        initDb();
        prepareXml();
        prepareXsl();
        x.transformByXsl("C:/users/savineu/2.xsl");

        File fXmlFile = new File(x.getTransformXMLPath());
        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.toString();

        String controlXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entries><entry field=\"1\"/></entries>";
        assertThat(output, CompareMatcher.isIdenticalTo(controlXml));
    }

    @Test
    public void getPath() {
        assertNotNull(x.getPath());
        assertEquals("C:/users/savineu/1.xml", x.getPath());
    }

    @Test
    public void getTransformXMLPath() {
        assertNotNull(x.getTransformXMLPath());
        assertEquals("C:/users/savineu/2.xml", x.getTransformXMLPath());
    }
}