package eTracker.command.behavior;

import java.util.List;
import eTracker.builder.ETrackerUtility;
import eTracker.object.Expense;
import java.time.Month;

//for summary command (i.e summary or summary --month 5)
public class Summary extends ETrackerUtility {
	
	private static int getMonth(String input) {
		return Integer.parseInt(input.split(" ")[2]);
	}

	private static void showTotal(List<Expense> expenseRecord) {
		int total = expenseRecord.stream().mapToInt(Expense::getAmount).sum();

		var data = "| Total Expenses: $" + total + " |";
		var border = "-".repeat(data.length());
		
		System.out.println(border + "\n" + data + "\n" + border + "\n");
	}

	private static void showTotal(List<Expense> expenseRecord, int month) {
		int total = expenseRecord.stream()
				.filter(t -> t.getDate().getMonth().getValue() == month)
				.mapToInt(Expense::getAmount).sum();

		var data = String.format("| Total expenses on %s: $%s |", Month.of(month), total);
		var border = "-".repeat(data.length());
		
		System.out.println(border + "\n" + data + "\n" + border + "\n");
	}

	public static void check(List<Expense> expenseRecord, String input) {

		if (hasArg(input)) {

			if (invalidMonthValue(getMonth(input))) {
				System.out.println("\nInvalid month.\n");
				return;
			} else {

				boolean hasData = expenseRecord.stream()
						.filter(e -> e.getDate().getMonthValue() == getMonth(input))
						.count() != 0;

				if (hasData) showTotal(expenseRecord, getMonth(input));
				else System.out.println("\nNo data on month " + Month.of(getMonth(input)) + ".\n");
			}

		} else {
			if (expenseRecord.isEmpty()) System.out.println("\nNo Data.\n");
			else showTotal(expenseRecord);
		}
	}
}