package ServletExcercises;

public class History {
	public int id;
	public String type;
	public int amount;
	public int total_balance;
	public String createdAt;
	public String toString() {
	    return "{"
	        + "\"Id\": " + id + ", "
	        + "\"type\": \"" + type + "\", "
	        + "\"total_balance\": " + total_balance + ", " 
	        + "\"amount\": " + amount +", " 
	        + "\"CreatedAt\" : \""+ createdAt +  "\""
	        + "}";
	} 
}
