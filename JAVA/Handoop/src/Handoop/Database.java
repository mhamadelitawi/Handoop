package Handoop;

import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {
    Connection cnx;
    PreparedStatement ps;
    Statement st;
    ResultSet rs;
    
    
    Database()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://127.0.0.1:3306/handoop";
                String user = ""; //enter your database user 
                String pass = ""; // enter your database password
              this.cnx = DriverManager.getConnection(url,user,pass);
              this.st= (Statement) cnx.createStatement();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    boolean queryWithNoResult(String sql)
    {
        try
        {
            this.st.executeUpdate(sql);
        }
        catch(Exception e){
        	 System.out.println(e.getMessage());
        	return false;
        	}
        return true;
    }
    
    
     boolean queryWithResult(String sql)
    {
        try
        {
            rs = this.st.executeQuery(sql);
        }
        catch(Exception e){return false;}
        return true;
    }
    
     
    void terminer()
    {
        try {
            this.cnx.close();
        } catch (SQLException ex) {
             System.out.println(ex.getMessage());
           // Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 
     
}
