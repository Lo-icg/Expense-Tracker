package expense_tracker.builder;

import expense_tracker.object.Expense;
import expense_tracker.object.ExpenseList;

public class ExpenseTracker extends ExpenseTrackerUtility {

	// private List<Expense> expenseList = new LinkedList<>();

	private ExpenseList<Expense> record = new ExpenseList<>();

	private ExpenseTracker(boolean running) {

		while (running) {

			var input = prompt("$ expense-tracker>> ");

			// if input is only invalid, following loop will execute
			while (invalid(input)) {
				input = prompt("Invalid Input\n\n$ expense-tracker>> ");
			}

			var command = getCommand(input);

			switch (command) {
			case Command.ADD       -> Add.saveTo(record, input);
			case Command.UPDATE    -> Update.saveTo(record.getExpenses(), input);
			case Command.LIST      -> Expense_List.show(record.getExpenses(), input);
			case Command.SUMMARY   -> Summary.check(record.getExpenses(), input);
			case Command.DELETE    -> Delete.removeIfExist(record.getExpenses(), input);
			case Command.SETBUDGET -> BUDGET.applyTo(record, input);
			case Command.LISTBUDGET -> BUDGET.show(record.getMonth_budget());
			case Command.GETBUDGET -> BUDGET.showFiltered(record.getMonth_budget(), input);
			
			}
		}
		read.close();
	}

	public static void run() {
		var launch = true;
		new ExpenseTracker(launch);
	}
}
