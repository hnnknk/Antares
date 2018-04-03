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
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class XSLPreparerTest {
    private XSLPreparer x;

    @Before
    public void init() {
        x = new XSLPreparer("C:/users/savineu/1.xsl");
    }

    @Test
    public void prepareXSLSuccessCreated() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        x.prepareXSL();

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

        String controlXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><xsl:stylesheet " +
                "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\"><xsl:template match=\"/\">" +
                "<entries><xsl:for-each select=\"entries/entry\"><entry><xsl:attribute name=\"field\">" +
                "<xsl:value-of select=\"field\"/></xsl:attribute></entry></xsl:for-each></entries>" +
                "</xsl:template></xsl:stylesheet>";
        assertThat(output, CompareMatcher.isIdenticalTo(controlXml));

    }

    @Test
    public void getCorrectPath() {
        assertNotNull(x.getPath());
        assertEquals("C:/users/savineu/1.xsl", x.getPath());
    }

}
