package eTracker.command.behavior;

import java.time.LocalDate;
import java.time.Month;
import eTracker.builder.ETrackerUtility;
import eTracker.object.Expense;
import eTracker.object.ExpenseList;

public class SetBudget extends ETrackerUtility {

	private static boolean invalidData(String input) {

		var valid_budget_format = true;
		var valid_month_format = true;

		var budget = 0;
		var month = 0;

		try {
			budget = Integer.parseInt(input.split(" ")[2]);  // parse to int budget value format
		} catch (NumberFormatException e2) {
			valid_budget_format = false;
		}

		try {
			month = Integer.parseInt(input.split(" ")[4]);  // parse to int month value format
		} catch (NumberFormatException e2) {
			valid_month_format = false;
		}

		if (!valid_budget_format && !valid_month_format || invalidBudget(budget) && ETrackerUtility.invalidMonthValue(month)) {
			System.out.println("Invalid month and budget amount.\n");
			return true;
		}

		if (!valid_budget_format || invalidBudget(budget)) {
			System.out.println("Invalid budget amount.\n");
			return true;
		}

		if (!valid_month_format || invalidMonthValue(month)) {
			System.out.println("Invalid month.\n");
			return true;
		}

		return false;
	}

	private static void warnIfBudgetExceed(ExpenseList<Expense> record) {

		final var currentMonthValue = LocalDate.now().getMonthValue();
		final var currentMonth = LocalDate.now().getMonth().toString(); 

		var budgetExceeds = record.getExpenses().stream()
				.filter(e -> e.getDate().getMonthValue() == currentMonthValue)
				.mapToInt(Expense::getAmount).sum() > record.getMonth_budget()[currentMonthValue];

				if (budgetExceeds) System.out.println("\n[WARNING] You exceeds your budget in this month " + currentMonth + ".");	
	}

	public static void applyTo(ExpenseList<Expense> expenseRecord, String input) {

		//  set-budget --amount 23 --month 3

		if (invalidData(input)) return;
		else {

			var budget = Integer.parseInt(input.split(" ")[2]); // get a budget in input
			var monthValue =  Integer.parseInt(input.split(" ")[4]); // get a month value in input

			// set a budget for specific month
			expenseRecord.getMonth_budget()[monthValue] = budget;

			var data = "| Budget set to $" + budget + " on month " + Month.of(monthValue) + " |";
			var borderLine = "-".repeat(data.length());

			warnIfBudgetExceed(expenseRecord);

			System.out.println(borderLine + "\n" + data + "\n" + borderLine + "\n");		
		}
	}
}