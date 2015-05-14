import javax.swing.*;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class SortingGrads {
	public static void main(String[] args) throws FileNotFoundException {
		StudentInput si = new StudentInput();
		StudentOutput so;
		Students s = new Students(); //Empty Constructor doesn't actually do anything, suppressing error
		HashMap<String, Double> hm;
		int sw;
		boolean haveAnotherGo;
		Object[] options = new String[] {"Excel Import", "Manual Input", "Quit"};

		do {
			sw = JOptionPane.showOptionDialog(null, "Would you like to import an Excel doc, or manually input?",
					"Main Menu", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[2]);

			haveAnotherGo = false;

			//if 0 use an excel doc, if 1 manual input, but who would want that?
			switch (sw) {
				case JOptionPane.YES_OPTION: {
					si.getExcelData();

					if (si.isSuccess()) { //if file dialog is canceled, go back to the beginning
						haveAnotherGo = true;

						break;
					}

					ArrayList<String> names = standardizeNames(si.getStudents());

					hm = convertArrayLists(names, si.getStudentGPA());

					s = new Students(hm, si.getStudents().toArray(new String[si.getStudents().size()]));
				}
				break;

				case JOptionPane.NO_OPTION: {
					ArrayList<String> tmpNames = new ArrayList<>();
					ArrayList<Double> tmpGPA = new ArrayList<>();
					boolean comeAgain[] = new boolean[]{true, false};

					do {
						comeAgain[1] = false;

						do {
							String[] tmpCarry = new String[2];

							JTextField strField = new JTextField(10);
							JTextField doubField = new JTextField(10);

							//Making a dialog box with multiple inputs
							JPanel panel = new JPanel();
							panel.add(new JLabel("Name: "));
							panel.add(strField);
							panel.add(Box.createHorizontalStrut(15));
							panel.add(new JLabel("GPA: "));
							panel.add(doubField);

							int result = JOptionPane.showOptionDialog(null, panel, "Enter the name and GPA",
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
									new Object[]{"Continue", "End"}, "Continue");

							if (result == JOptionPane.YES_OPTION) {
								tmpCarry[0] = strField.getText();
								tmpCarry[1] = doubField.getText();

								//if the GPA isn't a double, and the name isn't "First Last", set condition to make the
								//dialog go again
								if (!(isParsableDouble(tmpCarry[1]) && tmpCarry[0].matches("[a-zA-Z]+ [a-zA-Z]+"))) {
									JOptionPane.showMessageDialog(null, "Enter a valid name with First name first, " +
													"and a valid GPA.",
											"Error", JOptionPane.WARNING_MESSAGE);

									comeAgain[1] = true;
								} else {
									tmpNames.add(tmpCarry[0]);
									tmpGPA.add(Double.parseDouble(tmpCarry[1]));
								}
							//make sure to not let the user supply an empty list of gpas or people
							} else if (result == JOptionPane.NO_OPTION && (tmpNames.isEmpty() || tmpGPA.isEmpty())) {
								comeAgain[1] = true;

								result = JOptionPane.showConfirmDialog(null, "The list of people cannot be empty.",
										"Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

								if (result == JOptionPane.CANCEL_OPTION) {
									return;
								}
							} else {
								comeAgain[0] = false;
							}
						} while (comeAgain[1]);
					} while (comeAgain[0]);


					tmpNames = standardizeNames(tmpNames);

					hm = convertArrayLists(tmpNames, tmpGPA);

					s = new Students(hm, tmpNames.toArray(new String[tmpNames.size()]));
				}
				break;

				default:
					return;
			}
		} while (haveAnotherGo);

		//put all the students in order
		s.sort();

		String num;
		do {
			//Gets a string for the reserved number of seats
			num = JOptionPane.showInputDialog("Input a number of seats: ");
		} while (!isParsableInteger(num)); // loop again if the string cant be parsed as an integer

		//reserve the user input number of seats
		s.reserveSeats(Integer.parseInt(num));

		so = new StudentOutput(s.getOrder().toArray(new String[s.getOrder().size()]));


	}

	//Checks if string input is a valid integer
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static boolean isParsableInteger(String input) {
		boolean parsable = true;

		try {
			Integer.parseInt(input);
		} catch (NumberFormatException nfe) {
			parsable = false;
		}

		return parsable;
	}

	//Checks if string input is a valid double
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static boolean isParsableDouble(String input) {
		boolean parsable = true;

		try {
			Double.parseDouble(input);
		} catch (NumberFormatException nfe) {
			parsable = false;
		}

		return parsable;
	}

	//rounds a double value to decimal places
	public static double round(double value, int places) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	//creates a HashMap from a list of names, and their corresponding GPAs
	public static HashMap<String, Double> convertArrayLists(ArrayList<String> strArr, ArrayList<Double> doubleArr) {
		HashMap<String, Double> hm = new HashMap<>();

		for (int i = 0; i < strArr.size(); i++) {
			int j = 1;
			String addStr = strArr.get(i);

			//if a name already exists, make a student <student name> + number, to prevent false collisions
			if (hm.containsKey(strArr.get(i))) {
				while (true) {
					if (!(hm.containsKey(strArr.get(i) + " " + j))) {
						addStr += " " + j;

						break;
					}

					j++;
				}
			}

			//Shouldn't need anymore than two digits
			hm.put(addStr, round(doubleArr.get(i), 2));
		}

		return hm;
	}

	public static ArrayList<String> standardizeNames(ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			//remove leading and trailing whitespace
			arr.set(i, arr.get(i).trim());

			//If in form Last, First change to First Last
			if (arr.get(i).matches("[A-Za-z]+, [A-Za-z]+")) {
				String split[] = arr.get(i).split(", ");

				String tmp = split[1] + " " + split[0];

				arr.set(i, tmp);
			}

			//Makes sure the first letter of each name is capitalized, to look pretty.
			if (arr.get(i).matches("[A-Za-z]+ [A-Za-z]+")) {
				String split[] = arr.get(i).split(" ");

				String tmp = split[0].substring(0, 1).toUpperCase() + split[0].substring(1) +
						split[1].substring(0, 1).toUpperCase() + split[1].substring(1);

				arr.set(i, tmp);
			}
		}

		return arr;
	}
}
