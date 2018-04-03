package xyz.hnnknk.magnit;

public class Main {
    /**
     * Main class.
     *
     * @param args the args
     */
    public static void main(final String[] args)  {
        XMLParser xmlParser = new XMLParser(100, "C:/sqlite/main.db");
        xmlParser.setDirectoryPath("C:/users/savineu/");
        xmlParser.start();
    }
}
