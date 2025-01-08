package eTracker.command.behavior;

import java.time.Month;

public final class ListBudget {

	private static void showInTable(int[] budget_month) {
		var m = 9; // Month = September
		var b = 7; // Budget = Not set

		for (int v : budget_month) {
			if (String.valueOf(v).length() > b) b = String.valueOf(v).length();
		}

		var borderLine = "+" + "-".repeat(m + 2) + "+" + "-".repeat(b + 2) + "+";
		var field = String.format("| %-" + m + "s | %-" + b + "s |", "Month", "Budget");

		StringBuilder dataBuilder = new StringBuilder(borderLine + "\n" + field + "\n" + borderLine + "\n");
		Month[] month = Month.values();

		for (int i = 0; i < budget_month.length - 1; i++) {

			var record = String.format("| %-" + m + "s | %-" + b + "s |"
					, month[i], (budget_month[i + 1] == 0 ? "Not set" : budget_month[i + 1])); 
			
			dataBuilder.append(record + "\n" + borderLine + "\n");
		}
		System.out.println(dataBuilder);
	}

	public static void show(int[] budget_month) {
		showInTable(budget_month);
	}
}