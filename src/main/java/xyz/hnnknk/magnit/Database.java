package xyz.hnnknk.magnit;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;


/**
 * The Database class with different
 * static methods methods to execution queries.
 */
public class Database {
    /**
     * Database jdbc url.
     */
    private static String url = "";

    /**
     * Create new database.
     *
     * @param fileName the file name
     */
    public static void createNewDatabase(final String fileName) {

        url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is "
                        + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create table with name 'TEST' and one
     * column with name 'TEST_FIELD' and with type 'Integer'.
     */
    public static void createTable() {

        String createStmt = "CREATE TABLE IF NOT EXISTS "
                + "TEST (TEST_FIELD INTEGER);";
        String dropStmt = "DROP TABLE IF EXISTS TEST;";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.createStatement().execute(dropStmt);
            conn.createStatement().execute(createStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Database execute query result set.
     *
     * @param queryStmt the query statement
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet dbExecuteQuery(final String queryStmt)
            throws SQLException {

        Statement stmt;
        ResultSet resultSet;
        CachedRowSetImpl crs = new CachedRowSetImpl();
        try (Connection conn = DriverManager.getConnection(url)) {

            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(queryStmt);

            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred "
                    + "at executeQuery operation : " + e);
        }
        return crs;
    }

    /**
     * Database execute prepared statement.
     *
     * @param sqlStmt the sql statement
     * @param input   the input number to insert
     */
    public static void dbExecutePrepareStatement(
            final String sqlStmt, final int input) {
        PreparedStatement stmt;
        try (Connection conn = DriverManager.getConnection(url)) {

            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sqlStmt);

            for (int i = 1; i <= input; i++) {
                stmt.setInt(1, i);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
