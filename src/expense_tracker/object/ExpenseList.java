package expense_tracker.object;

import java.util.LinkedList;
import java.util.List;

public class ExpenseList<T> {
	
	private List<T> expenses = new LinkedList<>();
	
	private final int[] month_budget = new int[13]; // month starts with index 1 to 12, index 0 will be null;

	public List<T> getExpenses() {
		return expenses;
	}
	
	public int[] getMonth_budget() {
		return month_budget;
	}
}
