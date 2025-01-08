package eTracker.command.behavior;

import java.util.List;

import eTracker.builder.ETrackerUtility;
import eTracker.object.Expense;

// for delete command (e.g, delete --id 2)
public class Delete extends ETrackerUtility {

	public static void removeIfExist(List<Expense> record, String input) {
		Expense ee = findId(record, getId(input)); // expenseToRemove

		if (ee != null) {
			record.remove(ee);
			System.out.printf("\nExpense deleted successfully (ID: %s)\n\n", ee.getId());
			
		} else System.out.println("\nNon-existent expense ID.\n");
	}
}