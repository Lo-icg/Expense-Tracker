package eTracker.command.behavior;

import java.time.Month;

import eTracker.builder.ETrackerUtility;

public class GetBudget extends ETrackerUtility {

	public static void show(int[] budget, String input) {
		
		var monthValue = 0;
		var validMonthValue = true;
		
		try {
			//get-budget --month 2
			monthValue = Integer.parseInt(input.split(" ")[2]);
			if (invalidMonthValue(monthValue)) validMonthValue = false;
		} catch (NumberFormatException e) {
			validMonthValue = false;
		}
		
		if (!validMonthValue) {
			System.out.println("Invalid value for Month of Year.\n");
			return;
		}
		
		Month month = Month.of(monthValue);
		var data = "| Month: " + month + " | Budget: " + (budget[monthValue] == 0 ? "Not set" : budget[monthValue]) + " |";
		var borderLine = "-".repeat(data.length());
		
		System.out.println(borderLine + "\n" + data + "\n" + borderLine + "\n");
	}
}