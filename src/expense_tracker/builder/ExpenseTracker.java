package expense_tracker.builder;

import java.util.LinkedList;
import java.util.List;

import expense_tracker.object.Expense;
import expense_tracker.object.ExpenseList;

public class ExpenseTracker extends ExpenseTrackerUtility {

	private List<Expense> expenseList = new LinkedList<>();

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
			case Command.ADD -> Add.saveTo(expenseList, input);
			case Command.UPDATE -> Update.saveTo(expenseList, input);
			case Command.LIST -> Expense_List.show(expenseList, input);
			case Command.SUMMARY -> Summary.check(expenseList, input);
			case Command.DELETE -> Delete.removeIfExist(expenseList, input);
			
			//case Command.SETBUDGET ->;
			}
		}
		read.close();
	}
	
	public static void run() {
		var launch = true;
		new ExpenseTracker(launch);
	}
}
