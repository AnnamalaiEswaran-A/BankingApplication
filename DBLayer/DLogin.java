package ServletExcercises.DBLayer;

import java.io.FileReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ServletExcercises.Customer;
import ServletExcercises.History;

public class DLogin implements IDLogin{
	
	public Customer loginUser(String email, String password) {
		PreparedStatement stmt = null;
      	Connection con = getConnection();
      	ResultSet rs = null;
      	Customer loginUser = null;
      	   try {
      		    loginUser = new Customer();
    			String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";
    			stmt = con.prepareStatement(sql);
    			stmt.setString(1, email); 
    			stmt.setString(2, password); 
    			rs = stmt.executeQuery();
    			if(!rs.next()) {
    				return loginUser;
    			}else {
    				loginUser = bindData(rs);
    				return loginUser;
    			} 	
    		} catch (Exception ex) {
    			// TODO Auto-generated catch block
    			ex.printStackTrace();
    		}
    		finally {
    	if(stmt != null)		try { stmt.close(); }catch(Exception ex) {ex.printStackTrace(); }
			 if(con != null)    try { con.close(); }catch(Exception ex) { ex.printStackTrace(); }
			if(rs != null)	 try { rs.close(); }catch(Exception ex) { ex.printStackTrace();}
    		}
      	   return loginUser;
	}
	
	public List<Customer> getAllUsers(){
		  List<Customer> customers = new ArrayList<>();
	         Statement stmt = null;
	       	 Connection con = getConnection();
	       	 ResultSet rs = null;
	         try {
	             stmt = con.createStatement();
	             rs = stmt.executeQuery("SELECT * FROM customer");     
	             while (rs.next()) {
	             	Customer currentUser = new Customer();
	             	currentUser =  bindData(rs);
	         				customers.add(currentUser);
	             }
	             System.out.println(customers.toString());
	             return customers;
	          } catch (Exception ex) {
	             ex.printStackTrace(); 
	          }
	         finally {
	        	if(stmt != null) try { stmt.close(); }catch(Exception ex) {ex.printStackTrace(); }
	        	if(con != null)  try { con.close(); }catch(Exception ex) { ex.printStackTrace(); }
	        	if(rs != null)  try { rs.close(); }catch(Exception ex) { ex.printStackTrace();}
	  		  }
	         return customers;
	}
	
	public String insertRecord(String name, String email, String password) {
		 CallableStatement stmt = null;
	      	Connection con = getConnection();
	      	ResultSet rs = null;
	      	//System.out.println("status");
	      	String status = "";
	      	 Customer user = new Customer();
			try {		
				LocalDateTime myObj = LocalDateTime.now();
				stmt = con.prepareCall("{call createUser(?,?,?,?)}");		
				stmt.setString(1, name);
				stmt.setString(2, email);
				stmt.setString(3, password);
				//stmt.setString(4, myObj.toString());
				stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
				//stmt.execute();
				boolean hasResults = stmt.execute();
				 rs = stmt.getResultSet();
				 status = stmt.getString(4); 
				 System.out.println(hasResults);
				 if(hasResults) {
					 while(rs.next()) {	
							 user = bindData(rs);
							 System.out.println(user);
						}
				 }	 
				 if(user.getName() == null)  return status;
				
			}catch (Exception ex) {
				System.out.println(ex.getMessage());		
			}finally {
				
				if(stmt != null) try { stmt.close(); }catch(Exception ex) {ex.printStackTrace(); }
				if(con != null) try { con.close(); }catch(Exception ex) { ex.printStackTrace(); }
				if(rs != null) try { rs.close(); }catch(Exception ex) { ex.printStackTrace();}
			}
			return user.toString();
	}

	public int updateBalance(int id, int amount, int balance, String action) {
		PreparedStatement stmt = null;
      	Connection con = getConnection();
      	try {  
        String sql = "";
        if(action.equals("withdrawal")) sql = "UPDATE customer SET balance = balance - ? WHERE id = ? AND (balance - ?) >= 1000";
        else sql = "update customer set balance = balance + ? where id = ?";
		 stmt = con.prepareStatement(sql);
		stmt.setInt(2, id); 
		stmt.setInt(1, amount);
		if(action.equals("withdrawal"))
		stmt.setInt(3, amount); 
		int n  =  stmt.executeUpdate();
		System.out.println(n);
		System.out.println(sql);
		if(n == 1) {
			 LocalDateTime myObj = LocalDateTime.now();
			   // System.out.println(myObj);
			String historyUpdate = "INSERT INTO history(userId, transcation_type, transcation_amount, total_amount, createdat) VALUES (?,?, ?, ?, ?)";
		    PreparedStatement stmt1 = con.prepareStatement(historyUpdate);
		    stmt1.setInt(1, id);          
		    stmt1.setString(2, action);                   
		    stmt1.setInt(3, amount);     
		    stmt1.setInt(4, balance); 
		    stmt1.setString(5, myObj.toString());  
		    int historyResult = stmt1.executeUpdate();
		    System.out.println("history" + " " + historyResult);
			return historyResult;
		}
		else
			return 0;
  }catch(Exception ex) {
  ex.printStackTrace();
     }   
      	finally{
      		
			if(stmt != null) try { stmt.close(); }catch(Exception ex) {ex.printStackTrace(); }
			if(con != null) try { con.close(); }catch(Exception ex) { ex.printStackTrace(); }
		//	if(reader != null) try { reader.close(); }catch(Exception ex) { ex.printStackTrace();}
     }
      	return 0;
}
	
	public List<History> getHistory(int id){
		 List<History> records = new ArrayList<>();
         PreparedStatement stmt = null;
       	 Connection con = getConnection();
       	 ResultSet rs = null;
         try {
             String sql = "SELECT id, transcation_type, transcation_amount, total_amount, createdat FROM history where userId = ?";
             stmt = con.prepareStatement(sql);
             stmt.setInt(1, id);
             rs = stmt.executeQuery();     
             while (rs.next()) {
                 //response.getWriter().println(rs.getInt(1) + " " + rs.getString(2) + " " +  rs.getString(3)+ " " +  rs.getString(4) + " " + rs.getInt(5)+ " " +  rs.getString(6) + " " + rs.getInt(7));
             	History history = new History();
             	history.type = rs.getString(2);
         		history.id = rs.getInt(1);
         		history.amount = rs.getInt(3);
         		history.total_balance = rs.getInt(4);
         		history.createdAt = rs.getString(5);
         		records.add(history);
             }
             System.out.println("records"+ records.toString());
             return records;
            
          } catch (Exception ex) {
             ex.printStackTrace(); 
          }
         finally {
				if(stmt != null) try { stmt.close(); }catch(Exception ex) {ex.printStackTrace(); }
				if(con != null) try { con.close(); }catch(Exception ex) { ex.printStackTrace(); }
				if(rs != null) try { rs.close(); }catch(Exception ex) { ex.printStackTrace();}
  		  }
         return records;
	}
	
	public String amountTransfer(int id, int amount, int accNo) {
		 CallableStatement stmt = null;
	      	Connection con = getConnection();
	      	System.out.println("amountTransfer");
			try {		
				stmt = con.prepareCall("{call amountTransfer(?,?,?,?,?)}");		
				stmt.setInt(1, id);
				stmt.setInt(2, amount);
				stmt.setInt(3, accNo); 
				LocalDateTime currentTime = LocalDateTime.now();
				stmt.setString(4, currentTime.toString());
				stmt.registerOutParameter(5, java.sql.Types.VARCHAR);
				stmt.execute();
				String status = stmt.getString(5);
	            System.out.println(status);
				return status;		 
			}catch (Exception ex) {
				System.out.println(ex.getMessage());		
			}finally {		
				if(stmt != null) try { stmt.close(); }catch(Exception ex) {ex.printStackTrace(); }
				if(con != null) try { con.close(); }catch(Exception ex) { ex.printStackTrace(); }
			}
			return "";	
	}
	public static Customer bindData(ResultSet rs) {
		Customer currentUser = new Customer();
		try {
				currentUser.setName(rs.getString(2));
				currentUser.setEmail(rs.getString(4));
				currentUser.setPassword(rs.getString(6));
				currentUser.setBalance(rs.getInt(5));
				currentUser.Id = rs.getInt(1);
				currentUser.acNo = rs.getInt(7);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return currentUser;	
	}
	
	public static Connection getConnection() {
		System.out.println("getConection");
		Connection con = null;
      	try(FileReader reader = new FileReader("NewFile.txt")) {       
	    Properties p=new Properties();  
	    p.load(reader);  
        Class.forName(p.getProperty("class"));
        String url = p.getProperty("url");	
        con = DriverManager.getConnection(url, "root", "root");
      	}catch(Exception ex) {
      		System.out.println(ex.getMessage());
      	}
      	return con;
	}
}
