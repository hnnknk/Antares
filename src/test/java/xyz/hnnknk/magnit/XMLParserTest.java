package xyz.hnnknk.magnit;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class XMLParserTest {

    private XMLParser x;

    @Before
    public void init() {
        x = new XMLParser();
    }

    @Test
    public void correctCalculateSum() throws IOException {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><entries><entry field=\"1\"/></entries>";
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:/users/savineu/3.xml"));
        writer.write(str);

        writer.close();

        assertEquals(1, x.sum("C:/users/savineu/3.xml"));
    }
}