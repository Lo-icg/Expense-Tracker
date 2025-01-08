package eTracker.command.behavior;

import java.time.LocalDate;

import eTracker.builder.ETrackerUtility;
import eTracker.object.Expense;
import eTracker.object.ExpenseList;

// for add command (e.g, add --description "Food" --amount 100)
public class Add extends ETrackerUtility {

	private final static int currentMonth = LocalDate.now().getMonthValue();

	private static boolean budgetWillExceed(ExpenseList<Expense> record, int amountToAdd) {

		if (record.getMonth_budget()[currentMonth] == 0) return false;

		return record.getExpenses().stream()
				.filter(ee -> ee.getDate().getMonthValue() == currentMonth)
				.mapToInt(Expense::getAmount)
				.sum() + amountToAdd > record.getMonth_budget()[currentMonth];
	}

	private static void add(ExpenseList<Expense> record, String input) {

		var id = Expense.idAutoIncrement();
		var description = getDescription(input);
		var category = Category.input();
		var amount = getAmount(input);

		record.getExpenses().add(new Expense(id, description, category, amount));
	}

	public static void saveTo(ExpenseList<Expense> record, String input) {

		var amount = getAmount(input);

		if (amount < 0) {
			System.out.println("\nNegative amount not allowed.\n");
			return;
		}

		if (amount == 0) {
			System.out.println("\nAmount zero not allowed\n");
			return;
		}

		if (budgetWillExceed(record, amount)) {

			var in = prompt2("\n[Warning] You will exceed your budget in this month(" + currentMonth + ")\nTo continue type y or n to cancel: ");
			System.out.println();
			
			if (in == 'y') add(record, input);
			
		} else add(record, input);
	}
}
