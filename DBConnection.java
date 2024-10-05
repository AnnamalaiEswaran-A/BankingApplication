package ServletExcercises;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;  

public class DBConnection {
	public static void main(String[] args) {
		BankingOperations operations = null;
		 boolean input = true;
		 Customer currentUser  = null;
		
		 //System.out.println(java.time.LocalDate.now());
		  while(input) {
		      System.out.println("Choose a valid option : \n 1.Create a new Account \n 2.login \n 3.Amount deposit \n 4.Amount withdrawl \n 5.Account transfer \n 6.Exit");
			  Scanner in = new Scanner(System.in);
			  System.out.print("Enter your option : ");
			  int n = -1;
			  try {
				   n = in.nextInt();
			  }catch (Exception ex) {
				  System.out.println(ex.getMessage() + " \n Please try again");
			  }
			  Customer customer  = null;
			  
			  switch(n) {
			  
			  case 1:
				  customer = new Customer();
				  System.out.print("Enter your name: ");
				  customer.setName(in.next());
				  System.out.print("Enter your email: "); 
				  customer.setEmail(in.next());
				  System.out.print("Enter your password: ");
				  customer.setPassword(in.next());
				  operations = new Operations();
				  operations.insertRecord(customer);
				  break;
			  case 2:
				  operations = new Operations();
				  currentUser =  operations.login();
				  break;
			  case 3:
				  operations = new Operations();
				  if(currentUser == null) {
					  //System.out.println(currentUser.getName());
					  currentUser =  operations.login();
				  }
					  currentUser =  operations.amountDeposit(currentUser);
				  break;
			  case 4:
				  operations = new Operations();
				  if(currentUser == null) {
					  //System.out.println(currentUser.getName());
					  currentUser =  operations.login();
				  }
					  currentUser =  operations.amountWithdrawl(currentUser);
				  break;
			  case 5:
				  operations = new Operations();
				  if(currentUser == null) {
					  //System.out.println(currentUser.getName());
					  currentUser =  operations.login();
				  }
					  currentUser =  operations.amountTransfer(currentUser);
				  break;
			  case 6:
				  input = false;
				  break;
			  default:
				  System.out.println("Please enter a valid option");
				  break;
			  }
		  }
	}
	
}
