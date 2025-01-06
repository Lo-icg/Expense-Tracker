package expense_tracker.builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

import expense_tracker.object.Expense;
import expense_tracker.object.ExpenseList;

public class ExpenseTrackerUtility {

	// command in this Expense tracker program
	protected class Command {
		final static String ADD = "add";
		final static String UPDATE = "update";
		final static String DELETE = "delete";
		final static String LIST = "list";
		final static String SUMMARY = "summary";
		final static String SETBUDGET = "set-budget";
		final static String LISTBUDGET = "list-budget";
		final static String GETBUDGET = "get-budget";
	}

	static protected Scanner read = new Scanner(System.in);

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

		//combine all pattern
		var pattern = add + "|" + update + "|" + list + "|" + sum + "|" + del + "|" + fsum + "|" + flist + "|" + budget + "|" + lsBudget + "|" + fBudget;
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

	// return true if input is more than one word
	private static boolean hasArg(String input) {
		return input.split(" ").length > 1;
	}

	// for category data
	protected class Category {

		// category input pattern to use
		private static String cPattern = "([a-zA-Z])+|\\1{1}";

		// return false if pattern not match 
		private static boolean invalid(String input) {
			return !input.matches(cPattern);
		}

		private static String input() {
			var c = prompt("$ expense-category> ");
			// revalidate if pattern not match
			while (Category.invalid(c)) {
				c = prompt("Invalid Input\n\n$ expense-category> ");
			}
			return c;
		}

	}
	
	static final int currentMonth = LocalDate.now().getMonthValue();
	static final String currentMonthName = LocalDate.now().getMonth().toString(); 

	// for add command (e.g, add --description "Food" --amount 100)
	protected class Add {
		
		private static boolean budgetWillExceed(ExpenseList<Expense> record, int amountToAdd) {
			
			if (record.getMonth_budget()[currentMonth] == 0) return false;
			
		    return record.getExpenses().stream()
					 .filter(ee -> ee.getDate().getMonthValue() == currentMonth)
					 .mapToInt(Expense::getAmount).sum() + amountToAdd > record.getMonth_budget()[currentMonth];
		}
		
		protected static void saveTo(ExpenseList<Expense> record, String input) {

			if (getAmount(input) < 0) {
				System.out.println("\nNegative amount not allowed.\n");
				return;
			}

			if (getAmount(input) == 0) {
				System.out.println("\nAmount zero not allowed\n");
				return;
			}
			
						
			if (budgetWillExceed(record, getAmount(input))) {
				
				var in = prompt2("\n[Warning] You will exceed your budget in this month(" + currentMonth + ")\nTo continue type y or n to cancel: ");
				
				if (in == 'y') {
					System.out.println();
					var id = Expense.idAutoIncrement();
					record.getExpenses().add(new Expense(id, getDescription(input), Category.input(), getAmount(input)));
				}
				else System.out.println();
				
			} else {
				var id = Expense.idAutoIncrement();
				record.getExpenses().add(new Expense(id, getDescription(input), Category.input(), getAmount(input)));
			}
		}
	}

	// for update command (e.g, update --id 2 --description "Food" --amount 50)
	protected class Update {

		protected static void saveTo(List<Expense> e, String input) {

			if (getAmount(input) < 0) {
				System.out.println("\nNegative amount not allowed.\n");
				return;
			}

			if (getAmount(input) == 0) {
				System.out.println("\nAmount zero not allowed\n");
				return;
			}

			var id = getId(input);
			Expense ee = findId(e, id); // expenseToUpdate

			if (ee != null) {
				ee.setDescription(getDescription(input));
				ee.setAmount(getAmount(input));
				ee.setCategory(Category.input());

				System.out.printf("%nExpense updated successfully (ID: %s)%n%n", ee.getId());
			} else {
				System.out.println("\nNon-existent expense ID.\n");
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
			} else System.out.println("\nNon-existent expense ID.\n");
		}
	}

	// for list command (i.e, list)
	protected class Expense_List {

		// display all expense record in a tabular format                        
		private static void showAll(List<Expense> e) {

			var dL = 11; // default description field length -> | Description |  
			var aL = 6;  // default amount field length      -> | Amount |  
			var iL = 2;  // default id field length          -> | ID |      
			var ddL = 4; // default date field length        -> | Date |    
			var cL = 8;  // default category field length    -> | Category |

			for (Expense ee : e) { 
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

			for (Expense ee : e) {
				// form a record
				var record = String.format("| %-" + iL + "s | %-" + ddL + "s | %-" + dL + "s | $%-" + (aL - 1) + "s | %-" + cL + "s |\n",
						ee.getId(), ee.getDate(),ee.getDescription(), ee.getAmount(), ee.getCategory());

				tableFormat.append(record + border); // appends every expense record with border
			}

			// output data in a table format if expense record not empty
			System.out.println(e.isEmpty() ? "\nNo Expenses record\n" : tableFormat.toString()); 
		}

		private static void showFiltered(List<Expense> e, String input) {

			var category = input.split(" ")[2];
			// list of expense record same categories 
			List<Expense> expenseFiltered = e.stream().filter(f -> f.getCategory().equals(category)).toList();

			if (expenseFiltered.isEmpty()) System.out.println("\nNon-existent expense category.\n");
			else showAll(expenseFiltered);
		}

		protected static void show(List<Expense> e, String input) {

			if (hasArg(input)) showFiltered(e, input);
			else showAll(e);
		}
	}

	// for summary command (i.e summary or summary --month 5)
	protected class Summary {

		private static int getMonth(String input) {
			return Integer.parseInt(input.split(" ")[2]);
		}

		private static void total(List<Expense> e) {
			int total = e.stream().mapToInt(Expense::getAmount).sum();

			var data = "| Total Expenses: $" + total + " |";
			var border = "-".repeat(data.length());
			System.out.println(border + "\n" + data + "\n" + border + "\n");
		}

		private static void total(List<Expense> e, int month) {
			int total = e.stream()
					.filter(t -> t.getDate().getMonth().getValue() == month)
					.mapToInt(Expense::getAmount).sum();

			// var data = "| Total Expenses: $" + total + " |";

			var data = String.format("| Total expenses on %s: $%s |", 
					LocalDate.of(2024, month, 1).getMonth().toString(), total);

			var border = "-".repeat(data.length());
			System.out.println(border + "\n" + data + "\n" + border + "\n");
		}

		protected static void check(List<Expense> expenseRecord, String input) {

			if (hasArg(input)) {

				if (getMonth(input) < 0 || getMonth(input) > 12 || getMonth(input) == 0) {
					System.out.println("\nInvalid month.\n");
					return;

				} else {

					boolean hasData = expenseRecord.stream()
							.filter(e -> e.getDate().getMonthValue() == getMonth(input))
							.count() != 0;

					if (hasData) total(expenseRecord, getMonth(input));
					else {
						var month = LocalDate.of(2024, getMonth(input), 1).getMonth().toString();
						System.out.println("\nNo data on month " + month + ".\n");
					}
				}

			} else {

				if (expenseRecord.isEmpty()) System.out.println("\nNo Data.\n");
				else total(expenseRecord);
			}
		}
	}

	// for set-budget command (i.e set-budget --amount 23 --month 2)
	protected class BUDGET {

		private static boolean invalidBudget(int budget) {
			return budget == 0 || budget < 0;
		}

		private static boolean invalidMonth(int month) {
			return month == 0 || month < 0 || month > 12;
		}

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

			if (!valid_budget_format && !valid_month_format || invalidBudget(budget) && invalidMonth(month)) {
				System.out.println("Invalid month and budget amount.\n");
				return true;
			}

			if (!valid_budget_format || invalidBudget(budget)) {
				System.out.println("Invalid budget amount.\n");
				return true;
			}

			if (!valid_month_format || invalidMonth(month)) {
				System.out.println("Invalid month.\n");
				return true;
			}

			return false;
		}

		private static void warnIfBudgetExceed(ExpenseList<Expense> record) {
			
			var budgetExceeds = record.getExpenses().stream()
					.filter(e -> e.getDate().getMonthValue() == currentMonth)
					.mapToInt(Expense::getAmount).sum() > record.getMonth_budget()[currentMonth];
			
			if (budgetExceeds) System.out.println("\n[WARNING] You exceeds your budget in this month " + currentMonthName + ".");	
		}
		
		protected static void applyTo(ExpenseList<Expense> e, String input) {

			//  set-budget --amount 23 --month 

			if (invalidData(input)) return;
			
			else {
				
				var budget = Integer.parseInt(input.split(" ")[2]);
				var monthValue =  Integer.parseInt(input.split(" ")[4]);
				
				e.getMonth_budget()[monthValue] = budget; // budget set
				
				String month = LocalDate.of(2024, monthValue, 1).getMonth().toString();
				var info = "| Budget set to $" + budget + " on month " + month + " |";
				var borderLine = "-".repeat(info.length());
				warnIfBudgetExceed(e);
				System.out.println(borderLine + "\n" + info + "\n" + borderLine + "\n");
				
			}
		}
		
		
		protected static void show(int[] budget_month) {
			
			var m = 9; // Month = September
			var b = 7; // Budget = Not set
			
			for (int v : budget_month) {
				if (String.valueOf(v).length() > b) b = String.valueOf(v).length();
			}
			
			var borderLine = "+" + "-".repeat(m + 2) + "+" + "-".repeat(b + 2) + "+";
			var field = String.format("| %-" + m + "s | %-" + b + "s |", "Month", "Budget");
			
			System.out.println(borderLine + "\n" + field + "\n" + borderLine);
			
			Month[] months = Month.values();
			
			for (int i = 0; i < budget_month.length - 1; i++) {
				
				var record = String.format("| %-" + m + "s | %-" + b + "s |"
						, months[i], (budget_month[i + 1] == 0 ? "Not set" : budget_month[i + 1])); 
				System.out.println(record + "\n" + borderLine);
				
			}
			System.out.println();
		}
		
		protected static void showFiltered(int[] budget, String input) {
			
			
			var monthValue = 0;
			var validMonthValue = true;
			
			try {
				//get-budget --month 2
				monthValue = Integer.parseInt(input.split(" ")[2]);
				if (monthValue < 1 || monthValue > 12) validMonthValue = false;
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
}
