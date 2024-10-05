package ServletExcercises;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ServletExcercises.BusinessLayer.BLogin;
import ServletExcercises.BusinessLayer.IBLogin;

//import com.google.gson.Gson;


@WebServlet("/ExampleServlet/*")
public class ExampleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	  response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          if(request.getParameter("action") == null) return;
    	if(request.getParameter("action").equals("getAllUsers")){
    		try {
				getAllUsers(response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else if(request.getParameter("action").equals("login")) {
    		try {
    			System.out.println("login");
    			loginUser(response, request.getParameter("email"), request.getParameter("password"));
    		}catch(Exception ex) {
    			response.getWriter().write("{\"status\":\"error\",\"message\":\"" + ex.getMessage() + "\"}");
    		}
    	}else if(request.getParameter("action").equals("History")) {
    		System.out.println("doget" + " " +request.getParameter("Id"));
    		try {
				getHistory(response, request.getParameter("Id"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  	response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    if(request.getParameter("action").equals("UserCreation")) {
    	insertRecord(response, request.getParameter("name"), request.getParameter("email"), request.getParameter("password"));
    }else if(request.getParameter("action").equals("Deposit") || request.getParameter("action").equals("Withdrawal")) {
    	//amountDeposit(response, request.getParameter("id"), request.getParameter("amount"), request.getParameter("action"),  request.getParameter("balance"));
    }else if(request.getParameter("action").equals("transfer")) {
      	amountTransfer(response, request.getParameter("id"), request.getParameter("amount"), request.getParameter("accNo"));
    }
  }
    
    protected void getAllUsers(HttpServletResponse response) throws Exception {
   	 response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        List<Customer> users = null;
        IBLogin login = null;
        try {
       	 users = new ArrayList<>();
       	 login = new BLogin();
       	 users = login.getAllUsers();
       	 if(users != null) {
       		 response.getWriter().write(users.toString());
       	 }
        }catch (Exception ex){
       	 response.getWriter().write("{\"status\":\"error\",\"message\":\"" + ex.getMessage() + "\"}");
        }
   }
    
    protected void loginUser(HttpServletResponse response, String email, String password) throws Exception  {
     	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Customer customer = null;
        IBLogin login = null;
      	   try {
      		 login = new BLogin(); 
      		customer = login.loginUser(email, password);
    			if(customer == null) {
    				response.getWriter().write("{\"status\":\"error\"}");
    			}else {
    				response.getWriter().write(customer.toString());
    			} 	
    		} catch (Exception ex) {
    			// TODO Auto-generated catch block
    			response.getWriter().write("{\"status\":\"error\",\"message\":\"" + ex.getMessage() + "\"}");
    		}
    }
    
	protected void insertRecord(HttpServletResponse response, String name, String email, String password) throws IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
      	System.out.println("status");
        IBLogin login = null;
		try {
			 login = new BLogin(); 	 
			 String res = login.insertRecord(name, email, password);
			 System.out.println(res);
			 if(res.equals("Email already exists")) {
				// System.out.println("{\"status\":\"error\", \"message\":\""+"Email already exists"+  "\"}");
				 response.getWriter().write("{\"status\":\"error\", \"message\":\""+"Email already exists"+  "\"}"); 
				 return;
			 }else if(res.equals("Enter valid inputs")) {
				 response.getWriter().write("{\"status\":\"error\", \"message\":\""+"Fill all neccessary details"+  "\"}"); 
				 return;
			 }
				 response.getWriter().write(res);	
		}catch (Exception ex) {
			System.out.println(ex.getMessage());		
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));      
       	IBLogin login = null;
        try {
        	login = new BLogin(); 
			 String status = login.updateBalance(br.readLine());
			 if(status.equals("successfull")) {		 
				response.getWriter().write("{\"status\":\"success\", \"message\" : \"Transcation successfull\"}");
			}
			else if(status.equals("Enter valid amount"))
				response.getWriter().write("{\"status\":\"error\", \"message\" : \"Enter valid amount \"}");
			else  
			response.getWriter().write("{\"status\":\"error\", \"message\" : \"Insufficient balance. Minimum balance should be maintained \"}");
        }catch(Exception ex) {
        	response.getWriter().write("{\"status\":\"error\",\"message\":\"" + ex.getMessage() + "\"}");
        }finally{
        	br.close();
        }
	}
	
	protected void amountTransfer(HttpServletResponse response, String id, String amount, String accNo) throws IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        IBLogin login = null;
        try{
        	login = new BLogin(); 
        	String status = login.amountTransfer(Integer.parseInt(id), Integer.parseInt(amount), Integer.parseInt(accNo));
        	 if(status != null) {
        		 response.getWriter().write("{\"status\":\"error\",\"message\":\"" + status + "\"}"); 
			 }else {
				 response.getWriter().write("{\"status\":\"error\",\"message\":\"" + "Error occured" + "\"}"); 
			 }
        }catch (Exception ex) {
        	 response.getWriter().write("{\"status\":\"error\",\"message\":\"" + ex.getMessage() + "\"}"); 
        }
	}
	
	protected void getHistory(HttpServletResponse response, String Id) throws Exception {
		 response.setContentType("application/json");
		 IBLogin login = null;
		 List<History> records = null;
		 try {
			 records = new ArrayList<>();
			 login = new BLogin(); 
			 records = login.getHistory(Integer.parseInt(Id));
			 if(records != null) {
				 response.getWriter().write(records.toString());
			 }else {
				 response.getWriter().write("{\"status\":\"error\",\"message\":\"" + "Error occured" + "\"}"); 
			 }
			
		 }
		 catch (Exception ex) {
			 response.getWriter().write("{\"status\":\"error\",\"message\":\"" + ex.getMessage() + "\"}"); 
		 }
	}
}

