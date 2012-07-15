package sierra.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Storage {

    String url;
    Connection con;
    public Statement stmt;
    public static Boolean NoSQLException = true;

    public Storage(String host, String username, String password, String database) {
        try {
        	
            Class.forName("com.mysql.jdbc.Driver");

            url = "jdbc:mysql://"
                    + host + "/"
                    + database;
            con = DriverManager.getConnection(
                    url,
                    username,
                    password
                    );
            stmt = con.createStatement();

        } catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC Driver not found");
        } catch (SQLException sqle) {
        	Storage.NoSQLException = false;
        }
    }

    public String ReadString(String field, String query) {
    	
    	String Retrieved = "";
        ResultSet result = null;
        try {
            result = stmt.executeQuery(query);
            result.first();
            Retrieved = result.getString(field);
        } catch (Exception e) {
        }
        return Retrieved;
    }

    public Integer ReadInt(String field, String query) {
    	Integer Retrieved = 0;
        ResultSet result = null;
        try {
            result = stmt.executeQuery(query);
            result.first();
            Retrieved = result.getInt(field);
        } catch (Exception e) {
        }
        return Retrieved;
    }
    public PreparedStatement ExecuteQuery(String query) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);//, Statement.RETURN_GENERATED_KEYS);
            
            return preparedStatement;
        } catch (Exception e) {
        }
        return null;
    }

    public boolean RowExists(String query) {
        ResultSet result;
        try {
            result = stmt.executeQuery(query);
            return result.next();
        } catch (Exception e) {
        }
        return false;
    }

    public Integer getRowCount(String q) {
        Integer count = 0;
        try {
            ResultSet resSet;
            resSet = stmt.executeQuery(q);
            while (resSet.next()) {
                ++count;
            }
            return count;
        } catch (Exception e) {
  
        }
        return count;
    }

    public Integer getRowCount(PreparedStatement pStmt)
    {
        Integer count = 0;
        try {
            ResultSet resSet;
            resSet = pStmt.executeQuery();
            while (resSet.next()) {
                ++count;
            }
            return count;
        } catch (Exception e) {
  
        }
        return count;
    }
    
    public ResultSet ReadRow(String Query) {
        ResultSet resSet = null;
        try {
            resSet = stmt.executeQuery(Query);
            while (resSet.next()) {
                return resSet;
            }
        } catch (Exception e) {
        }
        return resSet;
    }

    public ResultSet ReadTable(String Query) {
        ResultSet resSet = null;
        try{
            resSet = stmt.executeQuery(Query);
        } catch (Exception e) {
        }
        return resSet;
    }
}