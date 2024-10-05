package ServletExcercises.BusinessLayer;

import java.util.List;

import ServletExcercises.Customer;
import ServletExcercises.History;

public interface IBLogin {
	Customer loginUser(String email, String password);
	List<Customer> getAllUsers();
	String insertRecord(String name, String email, String password);
	String updateBalance(String data);
	List<History> getHistory(int id);
	String amountTransfer(int id, int amount, int accno);
}
