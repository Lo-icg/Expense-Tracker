package eTracker.object;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
	
	private String description;
	private String category;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	
	public Expense(int id, String desc, String category, int amount) {
		this.id = id;
		description = desc;
		this.category = category;
		this.amount = amount;
		date = LocalDate.now();
		System.out.printf("\nExpense added successfully (ID: %s)\n\n", id_);
	}
	
	private static int id_;
	public static int idAutoIncrement() {
		return id_ = id_ + 1;
	}
	
}