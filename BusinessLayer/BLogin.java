package ServletExcercises.BusinessLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ServletExcercises.Customer;
import ServletExcercises.History;
import ServletExcercises.DBLayer.DLogin;
import ServletExcercises.DBLayer.IDLogin;

public class BLogin implements IBLogin{
	IDLogin operations = null;
	public Customer loginUser(String email, String password) {
		Customer customer = new Customer();
		operations = new DLogin();
		if(email == null || password == null) {
			return customer;
		}
		password = encryptPassword(password);
		customer = operations.loginUser(email, password);
		return customer;
	}
	
	public List<Customer> getAllUsers(){
		operations = new DLogin();
		return operations.getAllUsers();
	}
	public String insertRecord(String name, String email, String password) {
		operations = new DLogin();
		if(name == null || email == null|| password == null) {
			return "Enter valid inputs";
		}
		password = encryptPassword(password);
		return operations.insertRecord(name, email, password);
	}
	
	public String updateBalance(String data) {
		String arr[] = data.split("&");
       	Map<String, String> map = new HashMap<>();
       	for(String str : arr) {
       		System.out.println(str);
       		String str1[] = str.split("=");
       		map.put(str1[0], str1[1]);
       	}
    	int amount =  Integer.parseInt(map.get("amount"));
    	operations = new DLogin();
    	if(amount <= 0) {
    		return "Enter valid amount";
    	}
    	if(operations.updateBalance(Integer.parseInt(map.get("id")) , amount, Integer.parseInt(map.get("balance")), map.get("action")) == 1)
    		return "successfull";
    	return "Transaction failed";
	}
	public List<History> getHistory(int id){
		operations = new DLogin();
		return operations.getHistory(id);
	}
	
	public String amountTransfer(int id, int amount, int accno) {
		operations = new DLogin();
		if(amount <= 0) {
			return "Transaction amount can't be less than 0";
		}
		return operations.amountTransfer(id, amount, accno);
	}
	
	public static String encryptPassword(String password) {
	String result = "";
	for(int i = 0 ; i < password.length(); i++) {
		char a = password.charAt(i);
		if(a <= 57 && a + 0 >= 48) {
			result+=(char)a;
		}else if((a + 0) <= 90 && (a + 0) >= 65) {
			if((a + 2) < 90) {
				result+= (char) (a+2);
			}else {
				result +=(char) (64 + ((a + 2) - 90));
			}
		}else if((a + 0) <= 122 && (a + 0) >= 97){
		    if((a + 2) < 122) {
				result+= (char) (a+2);
			}else {
				result +=(char) (96 + ((a + 2) - 122));
			}
		}else{
		    result += a;
		}
	}
	return result;
}
}
