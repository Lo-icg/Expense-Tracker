package eTracker.builder;

import eTracker.command.Command;
import eTracker.command.behavior.*;
import eTracker.object.Expense;
import eTracker.object.ExpenseList;

public class ExpenseTracker extends ETrackerUtility {

	// private List<Expense> expenseList = new LinkedList<>();

	private ExpenseList<Expense> record = new ExpenseList<>();

	private ExpenseTracker() {

		while (running) {

			var input = prompt("$ expense-tracker>> ");

			while (invalid(input)) {
				input = prompt("Invalid Input\n\n$ expense-tracker>> ");
			}

			var command = getCommand(input);

			switch (command) {
			
			case Command.ADD        -> Add.saveTo(record, input);
			
			case Command.UPDATE     -> Update.saveTo(record.getExpenses(), input);
			
			case Command.LIST       -> List_.show(record.getExpenses(), input);
			
			case Command.SUMMARY    -> Summary.check(record.getExpenses(), input);
			
			case Command.DELETE     -> Delete.removeIfExist(record.getExpenses(), input);
			
			case Command.SETBUDGET  -> SetBudget.applyTo(record, input);
			
			case Command.LISTBUDGET -> ListBudget.show(record.getMonth_budget());
			
			case Command.GETBUDGET  -> GetBudget.show(record.getMonth_budget(), input);
			
			case Command.EXIT	    -> Program.exit();
			
			case Command.SAVE       -> Program.saveToFile(record);
			
			}
		}
	}

	public static void run() {
		new ExpenseTracker();
	}
}