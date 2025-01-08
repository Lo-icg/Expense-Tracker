package eTracker.builder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import eTracker.object.Expense;
import eTracker.object.ExpenseList;

public class ETrackerUtility {

	static protected Scanner read = new Scanner(System.in);
	static protected boolean running = true;

	// return the user input with a prompt defined in parameter
	static protected String prompt(String label) {
		System.out.print(label);
		return read.nextLine();
	}

	// return only a valid option(y or n) of user input with a prompt defined in parameter
	static protected char prompt2(String label) {

		char input;
		System.out.print(label);

		do {
			input = read.next().charAt(0);
			if (input == 'y' || input == 'n') break;
			else System.out.print("Invalid input try again: ");
		} while (true);

		read.nextLine();
		return input;
	}

	protected class Program {

		protected static void exit() {

			var c = prompt2("Enter y to confirm exit or n to cancel>> ");

			if (c == 'y') {
				running = false;
				read.close();
			}
			System.out.println();
		}

		protected static void saveToFile(ExpenseList<Expense> expenseRecord) {

			var filename = prompt("Enter the filename: ");
			filename += ".csv";

			var confirmed = prompt2("To confirm to save in file \"" +
					filename + "\" type y or n to cancel>> ");

			if (confirmed == 'y') {

				try (FileWriter write = new FileWriter(filename)) {

					write.append("ID,Date,Description,Amount,Category\n");

					for (Expense e : expenseRecord.getExpenses()) {
						write.append(String.format("%s,%s,%s,%s,%s%n",
								e.getId(),
								e.getDate(),
								e.getDescription(),
								e.getAmount(),
								e.getCategory()));
					}

					System.out.println("\nYour Expense list has been save to \"" + filename + "\".");

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else System.out.println();
		}
	}

	// return false if the user input is not valid and not matched to any regex pattern
	protected boolean invalid(String input) {
		// patterns
		var add = "add --description (?:\"\\S.*\\S|\\S{1}\") --amount (?:\\d{1,9}|-\\d{1,9})";  // add
		var update = "update --id \\d+ " + add.substring(4);
		var list = "list";                // list
		var sum = "summary";              // summary
		var del = "delete --id \\d+";     // delete
		var fsum = sum + " --month (?:\\d+|-\\d+)"; // summary with argument
		var flist = list + " --category " + Category.cPattern; // list with argument

		var budget = "set-budget --amount (?:\\d|-\\d)+ --month (?:\\d|-\\d)+"; // set-budget
		var lsBudget = "list-budget"; // list-budget_month
		var fBudget = "get-budget --month (?:\\d+|-\\d+)"; // get-budget --month n

		var exit = "exit";
		var save = "save";

		//combine all pattern
		var pattern = add + "|" + update + "|" + list + "|" + sum + "|" + del + "|" + fsum + "|"
				+ flist + "|" + budget + "|" + lsBudget + "|" + fBudget + "|" + exit + "|" + save;
		return !input.matches(pattern);
	}

	// return the first word of a user input as a command 
	protected String getCommand(String input) {
		return input.split(" ")[0];	
	}

	/** if the id parameter matched to any expense object property id,
	 *  it returns the expense object reference stored in the parameter List<Expense> e
	 *  if id not match to any expense object stored in List it returns null **/
	// utility for delete and update command
	static protected Expense findId(List<Expense> e, int id) {
		for (Expense ee : e) {
			if (ee.getId() == id) return ee;
		}
		return null;
	}

	// returns the description data substring of input between double quotes
	// utility for add and update command
	protected static String getDescription(String input) {
		int a  = input.indexOf('"');
		int b = input.lastIndexOf('"');
		String desc = input.substring(a + 1, b);
		return desc; 
	}

	// returns the amount substring of input using the last occurrence of index 't'
	// utility use for add and update command
	protected static int getAmount(String input) {
		int a = input.lastIndexOf('t');
		return Integer.parseInt(input.substring(a + 2));
	}

	// return the id data
	// only use for delete and update command
	protected static int getId(String input) {
		return Integer.parseInt(input.split(" ")[2]); 
	}

	// return true if input is more than one word
	// utility use for list and update command
	protected static boolean hasArg(String input) {
		return input.split(" ").length > 1;
	}

	// for category data
	// utility for add and update command
	protected class Category {

		// category input pattern to use
		private static String cPattern = "([a-zA-Z])+|\\1{1}";

		// return false if pattern not match 
		private static boolean invalid(String input) {
			return !input.matches(cPattern);
		}

		public static String input() {
			var c = prompt("$ expense-category> ");
			// validate if pattern not match
			while (Category.invalid(c)) {
				c = prompt("Invalid Input\n\n$ expense-category> ");
			}
			return c;
		}
	}

	// utility use for set-budget command
	protected static boolean invalidBudget(int budget) {
		return budget == 0 || budget < 0;
	}

	// utility for get-budget, set-budget, and summary command
	protected static boolean invalidMonthValue(int month) {
		return month < 1 || month > 12;
	}	
}