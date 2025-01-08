package eTracker.command.behavior;

import java.util.List;

import eTracker.builder.ETrackerUtility;
import eTracker.object.Expense;

// for list command (i.e, list)
public class List_ extends ETrackerUtility {
	
	// display all expense record in a tabular format                        
	private static void showAll(List<Expense> expenseRecord) {

		var dL = 11; // default description field length -> | Description |  
		var aL = 6;  // default amount field length      -> | Amount |  
		var iL = 2;  // default id field length          -> | ID |      
		var ddL = 4; // default date field length        -> | Date |    
		var cL = 8;  // default category field length    -> | Category |

		for (Expense ee : expenseRecord) { 
			var ee_dL = ee.getDescription().length();                // record description length
			var ee_aL = String.valueOf(ee.getAmount()).length() + 1; // record amount length
			var ee_iL = String.valueOf(ee.getId()).length();         // record id length
			var ee_ddL = ee.getDateAsString().length();              // record date length
			var ee_cL = ee.getCategory().length();                   // record category length

			// find the max length of each field to justify the border line,
			// if records length are greater than the default length
			if (ee_dL > dL) dL = ee_dL;
			if (ee_aL > aL) aL = ee_aL;
			if (ee_iL > iL) iL = ee_iL;
			if (ee_ddL > ddL) ddL = ee_ddL;
			if (ee_cL > cL) cL = ee_cL;
		}

		// form a border
		var border = "+" + "-".repeat(iL + 2) + "+" + "-".repeat(ddL + 2) + "+" + "-".repeat(dL + 2) + "+" + "-".repeat(aL + 2) + "+" + "-".repeat(cL + 2) + "+\n";
		// form a field
		var fields = String.format("| %-" + iL + "s | %-" + ddL + "s | %-" + dL + "s | %-" + aL + "s | %-" + cL + "s |\n",
				"ID", "Date", "Description", "Amount", "Category");
		// write in StringBuilder
		StringBuilder tableFormat = new StringBuilder(border + fields + border); // generate fields with border

		for (Expense ee : expenseRecord) {
			// form a record
			var record = String.format("| %-" + iL + "s | %-" + ddL + "s | %-" + dL + "s | $%-" + (aL - 1) + "s | %-" + cL + "s |\n",
					ee.getId(), ee.getDate(),ee.getDescription(), ee.getAmount(), ee.getCategory());

			tableFormat.append(record + border); // appends every expense record with border
		}

		// output data in a table format if expense record not empty
		System.out.println(expenseRecord.isEmpty() ? "\nNo Expenses record\n" : tableFormat.toString()); 
	}

	private static void showFiltered(List<Expense> e, String input) {

		var category = input.split(" ")[2];
		// list of expense record with same categories 
		List<Expense> expenseFiltered = e.stream().filter(f -> f.getCategory().equals(category)).toList();

		if (expenseFiltered.isEmpty()) System.out.println("\nNon-existent expense category.\n");
		else showAll(expenseFiltered);
	}

	public static void show(List<Expense> expenseRecord, String input) {
		
		if (hasArg(input)) showFiltered(expenseRecord, input);
		else showAll(expenseRecord);
	}
}