package expense_tracker.object;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
	
	private String description;
	private int amount;
	
	private int id;
	private LocalDate date;
	
	// getters setters
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDateAsString() {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(dateFormat);
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public Expense(int id, String desc, int amount) {
		this.id = id;
		description = desc;
		this.amount = amount;
		date = LocalDate.now();
		System.out.printf("\nExpense added successfully (ID: %s)\n\n", id_);
	}
	
	private static int id_;
	public static int idAutoIncrement() {
		return id_ = id_ + 1;
	}
	
}
