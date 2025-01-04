package expense_tracker.builder;

import java.util.LinkedList;
import java.util.List;

import expense_tracker.object.Expense;

public class ExpenseTracker extends ExpenseTrackerUtility {

	List<Expense> expenseList = new LinkedList<>();

	private ExpenseTracker(boolean running) {

		while (running) {

			var input = prompt("$ expenses-tracker>> ");

			// if input is only invalid, following loop will execute
			while (invalid(input)) {
				input = prompt("Invalid Input\n\n$ expense-tracker>> ");
			}

			var command = getCommand(input);

			switch (command) {

			case Command.ADD -> Add.saveTo(expenseList, input);
			case Command.UPDATE -> Update.saveTo(expenseList, input);
			case Command.LIST -> Expense_List.show(expenseList);
			case Command.SUMMARY -> Summary.check(expenseList, input);
			case Command.DELETE -> Delete.removeIfExist(expenseList, input);

			}
		}
		read.close();
	}
	
	public static void run() {
		var launch = true;
		new ExpenseTracker(launch);
	}
}
