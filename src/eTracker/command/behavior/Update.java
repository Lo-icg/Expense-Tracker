package eTracker.command.behavior;

import java.util.List;

import eTracker.builder.ETrackerUtility;
import eTracker.object.Expense;

// for update command (e.g, update --id 2 --description "Food" --amount 50)
public class Update extends ETrackerUtility {

	public static void saveTo(List<Expense> record, String input) {

		if (getAmount(input) < 0) {
			System.out.println("\nNegative amount not allowed.\n");
			return;
		}

		if (getAmount(input) == 0) {
			System.out.println("\nAmount zero not allowed\n");
			return;
		}

		var id = getId(input);
		Expense ee = findId(record, id); // expense to update

		if (ee != null) {
			
			ee.setDescription(getDescription(input));
			ee.setAmount(getAmount(input));
			ee.setCategory(Category.input());

			System.out.printf("%nExpense updated successfully (ID: %s)%n%n", ee.getId());
			
		} else System.out.println("\nNon-existent expense ID.\n");
	}
}