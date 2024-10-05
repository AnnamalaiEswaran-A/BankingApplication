package ServletExcercises.DBLayer;

import java.util.List;

import ServletExcercises.Customer;
import ServletExcercises.History;

public interface IDLogin {
	Customer loginUser(String email, String password);
	List<Customer> getAllUsers();
	String insertRecord(String name, String email, String password);
	int updateBalance(int id,  int amount, int balance, String action);
	List<History> getHistory(int id);
	String amountTransfer(int id, int amount, int accno);

}
