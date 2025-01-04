package expense_tracker.builder;

import java.util.List;
import java.util.Scanner;

import expense_tracker.object.Expense;

public class ExpenseTrackerUtility {

	// command in this Expense tracker program
	protected class Command {
		final static String ADD = "add";
		final static String UPDATE = "update";
		final static String DELETE = "delete";
		final static String LIST = "list";
		final static String SUMMARY = "summary";
	}

	protected Scanner read = new Scanner(System.in);

	// return the user input with a prompt defined in parameter
	protected String prompt(String label) {
		System.out.print(label);
		return read.nextLine();
	}

	// return false if the user input is not valid and not matched to any regex pattern
	protected boolean invalid(String input) {
		//patterns
		var add = "add --description (\"\\S.*\\S|\\S{1}\") --amount \\d{1,9}";  //add
		var update = "update --id \\d+ " + add.substring(4);
		var list = "list"; //list
		var sum = "summary"; //summary
		var del = "delete --id \\d+";  // delete
		var fsum = sum + " --month \\d+"; // summary with argument
		//combine all pattern
		var pattern = add + "|" + update + "|" + list + "|" + sum + "|" + del + "|" + fsum;
		return !input.matches(pattern);
	}

	/*  return the command, the command in this program is always the first word,
	 *  thats why I split into array the input through white space and return the first index */
	protected String getCommand(String input) {
		return input.split(" ")[0];	
	}

	/** if the id parameter matched to any expense object property id,
	 *  it returns the expense object reference stored in the parameter List<Expense> e
	 *  if id not match to any expense object stored in List it returns null **/
	protected static Expense findId(List<Expense> e, int id) {
		for (Expense ee : e) {
			if (ee.getId() == id) return ee;
		}
		return null;
	}

	// returns the description data substring of input between double quotes
	private static String getDescription(String input) {
		int a  = input.indexOf('"');
		int b = input.lastIndexOf('"');
		String desc = input.substring(a + 1, b);
		return desc; 
	}

	// returns the amount substring of input using the last occurrence of index 't' 
	private static int getAmount(String input) {
		int a = input.lastIndexOf('t');
		return Integer.parseInt(input.substring(a + 2));
	}

	// return the id data
	private static int getId(String input) {
		return Integer.parseInt(input.split(" ")[2]); 
	}

	// for add command (e.g, add --description "Food" --amount 100)
	protected class Add {

		protected static void saveTo(List<Expense> e, String input) {
			var description = getDescription(input);
			var amount = getAmount(input);
			var id = Expense.idAutoIncrement();

			e.add(new Expense(id, description, amount));
		}
	}

	// for update command (e.g, update --id 2 --description "Food" --amount 50)
	protected class Update {

		protected static void saveTo(List<Expense> e, String input) {

			var id = getId(input);
			Expense ee = findId(e, id); // expenseToUpdate

			if (ee != null) {
				ee.setDescription(getDescription(input));
				ee.setAmount(getAmount(input));
				System.out.printf("%nExpense updated successfully (ID: %s)%n%n", ee.getId());
			} else {
				System.out.println("%nExpense Id not found.%n");
			}
		}
	}

	// for delete command (e.g, delete --id 2)
	protected class Delete {

		protected static void removeIfExist(List<Expense> e, String input) {
			Expense ee = findId(e, getId(input)); // expenseToRemove

			if (ee != null) {
				e.remove(ee);
				System.out.printf("\nExpense deleted successfully (ID: %s)\n\n", ee.getId());
			} else System.out.println("\nExpense Id not found.\n");
		}
	}

	// for list command (i.e, list)
	protected class Expense_List {

		// display all expense record in a tabular format
		protected static void show(List<Expense> e) {
			var dL = 11; // default description field length -> | Description |
			var aL = 6;  // default amount field length      -> | Amount |
			var iL = 2;  // default id field length          -> | ID |
			var ddL = 4; // default date field length        -> | Date |

			for (Expense ee : e) {
				var ee_dL = ee.getDescription().length(); // record description length
				var ee_aL = String.valueOf(ee.getAmount()).length() + 1; // record amount length
				var ee_iL = String.valueOf(ee.getId()).length(); // record id length
				var ee_ddL = ee.getDateAsString().length(); // record date length

				// find the max length of each field to justify the border line,
				// if records length are greater than the default length
				if (ee_dL > dL) dL = ee_dL;
				if (ee_aL > aL) aL = ee_aL;
				if (ee_iL > iL) iL = ee_iL;
				if (ee_ddL > ddL) ddL = ee_ddL;
			}

			// form a border
			var border = "+" + "-".repeat(iL + 2) + "+" + "-".repeat(ddL + 2) + "+" + "-".repeat(dL + 2) + "+" + "-".repeat(aL + 2) + "+\n";
			// form a field
			var fields = String.format("| %-" + iL + "s | %-" + ddL + "s | %-" + dL + "s | %-" + aL + "s |\n",
					"ID", "Date", "Description", "Amount");
			// write in StringBuilder
			StringBuilder tableFormat = new StringBuilder(border + fields + border); // generate fields with border

			for (Expense ee : e) {
				// form a record
				var record = String.format("| %-" + iL + "s | %-" + ddL + "s | %-" + dL + "s | $%-" + (aL - 1) + "s |\n",
						ee.getId(), ee.getDate(),ee.getDescription(), ee.getAmount());

				tableFormat.append(record + border); // appends every expense record with border
			}
			
			// output data in a table format if expense record not empty
			System.out.println(e.isEmpty() ? "\nNo Expenses record\n" : tableFormat.toString()); 
		}
	}

	// for summary command (i.e summary or summary --month 5)
	protected class Summary {

		private static boolean hasArg(String input) {
			return input.split(" ").length > 1;
		}

		private static int getMonth(String input) {
			return Integer.parseInt(input.split(" ")[2]);
		}

		private static void total(List<Expense> e) {
			int total = e.stream().mapToInt(Expense::getAmount).sum();
			System.out.println("\nTotal Expenses: $" + total + "\n");
		}

		private static void total(List<Expense> e, int month) {
			int total = e.stream()
					.filter(t -> t.getDate().getMonth().getValue() == month)
					.mapToInt(Expense::getAmount).sum();
			System.out.println("\nTotal Expenses: $" + total + "\n");
		}

		protected static void check(List<Expense> expenseRecord, String input) {
			if (hasArg(input)) total(expenseRecord, getMonth(input));
			else total(expenseRecord);
		}
	}
}
