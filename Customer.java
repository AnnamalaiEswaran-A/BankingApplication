package ServletExcercises;

public class Customer{
	public int Id;
	public int acNo;
	private String name;
	private int balance;
	private String password;
	@Override
	public String toString() {
	    return "{"
	        + "\"Id\": " + Id + ", "
	        + "\"acNo\": " + acNo + ", "
	        + "\"name\": \"" + name + "\", "
	        + "\"balance\": " + balance + ", "
	        + "\"password\": \"" + password + "\", "
	        + "\"email\": \"" + email + "\""
	        + "}";
	}


	private String email;
	//public List<History> history = new ArrayList<>();
	
	public String getName() {
		return this.name;
	}
	
	public String getEmail() {
		return this.email;
	}
	public int getBalance() {
		return this.balance;
	}

	public String getPassword() {
		return this.password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBalance (int balance) {
		this.balance = balance;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}


