package ServletExcercises;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public interface BankingOperations {
	String insertRecord(Customer customer);
	Customer login();
	Customer amountDeposit(Customer customer);
	Customer amountWithdrawl(Customer customer);
	Customer amountTransfer(Customer customer);
}

class Operations implements BankingOperations{
	public String insertRecord(Customer customer) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/bank","root","root"); 
			
			CallableStatement stmt = con.prepareCall("{call createUser(?,?,?,?)}");
			
			stmt.setString(1, customer.getName());
			stmt.setString(2, customer.getEmail());
			stmt.setString(3, customer.getPassword());
			
			//stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			stmt.execute();
			String status = stmt.getString(4); 
			stmt.close();
			con.close(); 
			if(status.equals("true")) System.out.println(status);
			else System.out.println("User creation failed -------------------------------------------");
			return status;
		}catch (Exception ex) {
			System.out.println(ex.getMessage());		
		}
		
		return "false";
	}
	
	public Customer login() {
		Scanner in = new Scanner(System.in);
			try {	
				Customer currentUser = null;
				System.out.print("Enter your email: "); 
				String email = in.next();
				System.out.print("Enter your password: ");
				String password = in.next();
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection(  
						"jdbc:mysql://localhost:3306/bank","root","root"); 
				String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";
				PreparedStatement stmt = con.prepareStatement(sql);
				stmt.setString(1, email); 
				stmt.setString(2, password); 
				ResultSet rs = stmt.executeQuery();
				if(!rs.next()) {
					System.out.println("Your email and password does not match----------------");
				}else {
					currentUser = bindData(rs);
					System.out.println("Login successfull. Welcome " + currentUser.getName());
				} 
				stmt.close();
				con.close(); 
				return currentUser;
			}catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
			return null;
	}
	
	public Customer amountDeposit(Customer customer) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter amount deposit : "); 
		//Customer currentUser = new Customer();
		try {
			int amount = in.nextInt();
			if(amount <= 0) {
				System.out.println("Deposit amount must be greater than 0");
				return customer;
			}
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/bank","root","root"); 
			String sql = "Update customer set balance = ? where id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(2, customer.Id); 
			stmt.setInt(1, amount + customer.getBalance()); 
			int n  =  stmt.executeUpdate();
			if(n == 1) {
				customer.setBalance(amount + customer.getBalance());
				System.out.println("Amount deposit successfull. Your total balance is : " + customer.getBalance());
			}
			else
				System.out.println("Amount deposit failed ---------------------------------");
			return customer;
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
	
	public Customer amountWithdrawl(Customer customer) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter amount Withdrawl : "); 
		int amount = in.nextInt();
		if(1000 > customer.getBalance() - amount) {
			System.out.println("Insufficient balance. Minimum balance should be maintained. ");
			return customer;
		}
		if(amount <= 0) {
			System.out.println("Withdrawl amount must be greater than 0");
			return customer;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/bank","root","root"); 
			String sql = "Update customer set balance = ? where id = ?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(2, customer.Id); 
			stmt.setInt(1,  customer.getBalance() - amount); 
			int n  =  stmt.executeUpdate();
			if(n == 1) {
				customer.setBalance(customer.getBalance() - amount);
				System.out.println("Amount withdrwal successfull. Your balance after withdrwal is: " + customer.getBalance());
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return customer;
		
	}
	
	public Customer amountTransfer(Customer customer) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter recipient Account number : "); 
		int accountNumber = in.nextInt();
		System.out.print("Enter amount to transfer : "); 
		int amount = in.nextInt();
		if(1000 > customer.getBalance() - amount) {
			System.out.println("Insufficient balance. Minimum balance should be maintained. ");
			return customer;
		}else if(accountNumber == customer.acNo) {
			System.out.println("Sender and receiver account number can't be same.");
			return customer;
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/bank","root","root"); 
			CallableStatement stmt = con.prepareCall("{call amountTransfer(?,?,?,?)}");
			stmt.setInt(1, customer.Id);
			stmt.setInt(2, amount);
			stmt.setInt(3, accountNumber); 
			stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			 stmt.execute();
			 String status = stmt.getString(4);
			if(status == "Success") {
				customer.setBalance(customer.getBalance() - amount);
				System.out.println("Amount transfer successfull. Your balance after transfer is: " + customer.getBalance());
			}else if(status == "Invalid account number") {
				System.out.println("Invalid account number");
			}else {
				System.out.println(status);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return customer;
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
}