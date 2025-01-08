# Expense Tracker
A simple expense tracker application to manage your finances. This application can allow users to add, update, delete, and view their expenses. It can also allow users to set a budget for each month and set a category of each expenses. The application can provide a summary of the expenses as well.  
  
This project idea is from [roadmap.sh](https://roadmap.sh/projects/expense-tracker)

## Example Usage
```bash
$ expense-tracker>> add --description "launch" --amount 10
$ expense-category> food

Expense added successfully (ID: 1)

$ expense-tracker>> add --description "dinner" --amount 15
$ expense-category> food

Expense added successfully (ID: 2)

$ expense-tracker>> add --description "tuition fee" --amount 30
$ expense-category> education

Expense added successfully (ID: 3)

$ expense-tracker>> list
+----+------------+-------------+--------+-----------+
| ID | Date       | Description | Amount | Category  |
+----+------------+-------------+--------+-----------+
| 1  | 2025-01-07 | launch      | $10    | food      |
+----+------------+-------------+--------+-----------+
| 2  | 2025-01-07 | dinner      | $15    | food      |
+----+------------+-------------+--------+-----------+
| 3  | 2025-01-07 | tuition fee | $30    | education |
+----+------------+-------------+--------+-----------+

$ expense-tracker>> list --category food
+----+------------+-------------+--------+----------+
| ID | Date       | Description | Amount | Category |
+----+------------+-------------+--------+----------+
| 1  | 2025-01-07 | launch      | $10    | food     |
+----+------------+-------------+--------+----------+
| 2  | 2025-01-07 | dinner      | $15    | food     |
+----+------------+-------------+--------+----------+

$ expense-tracker>> list summary
Invalid Input

$ expense-tracker>> summary
-----------------------
| Total Expenses: $55 |
-----------------------

$ expense-tracker>> summary --month 2

No data on month FEBRUARY.

$ expense-tracker>> summary month 1
Invalid Input

$ expense-tracker>> summary --month 1
----------------------------------
| Total expenses on JANUARY: $55 |
----------------------------------

$ expense-tracker>> set-budget --amount 100 --month 1
---------------------------------------
| Budget set to $100 on month JANUARY |
---------------------------------------

$ expense-tracker>> list-budget
+-----------+---------+
| Month     | Budget  |
+-----------+---------+
| JANUARY   | 100     |
+-----------+---------+
| FEBRUARY  | Not set |
+-----------+---------+
| MARCH     | Not set |
+-----------+---------+
| APRIL     | Not set |
+-----------+---------+
| MAY       | Not set |
+-----------+---------+
| JUNE      | Not set |
+-----------+---------+
| JULY      | Not set |
+-----------+---------+
| AUGUST    | Not set |
+-----------+---------+
| SEPTEMBER | Not set |
+-----------+---------+
| OCTOBER   | Not set |
+-----------+---------+
| NOVEMBER  | Not set |
+-----------+---------+
| DECEMBER  | Not set |
+-----------+---------+

$ expense-tracker>> get-budget --month 1
--------------------------------
| Month: JANUARY | Budget: 100 |
--------------------------------

$ expense-tracker>> list
+----+------------+-------------+--------+-----------+
| ID | Date       | Description | Amount | Category  |
+----+------------+-------------+--------+-----------+
| 1  | 2025-01-07 | launch      | $10    | food      |
+----+------------+-------------+--------+-----------+
| 2  | 2025-01-07 | dinner      | $15    | food      |
+----+------------+-------------+--------+-----------+
| 3  | 2025-01-07 | tuition fee | $30    | education |
+----+------------+-------------+--------+-----------+

$ expense-tracker>> add --description "battle pass" --amount 50

[Warning] You will exceed your budget in this month(1)
To continue type y or n to cancel: n

$ expense-tracker>> save

Enter the filename: myexpenses
To confirm to save in file "myexpenses.csv" type y or n to cancel>> y

Your Expense list has been save to "myexpenses.csv"
```

## List of commands

Example of all valid command
```bash
add --description "dinner" --amount 20                 (add expense)
update --id 1 --description "launch" --amount 30       (update expenses by their id)
delete --id 1                                          (remove expenses by their id)

list                                                   (display all expenses)
list --category food                                   (display the expenses filtered with category)

summary                                                (show the total of expenses)
summary --month 12                                     (show the total of expenses of specific month)

set-budget --amount 10 --month 12                      (set a budget for specific month)
get-budget --month 12                                  (get the budget of specific month)
list-budget                                            (display the budget of each month) 

exit                                                   (exit program)
save                                                   (save expense record in a file .csv)
```
## How to run

You can clone this ```git clone git@github.com:Lo-icg/Expense-Tracker.git``` and for simplicity  
open this project in your IDE and go to ```run``` package and execute the ```Main```
