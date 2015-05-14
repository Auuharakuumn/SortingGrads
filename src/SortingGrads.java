import javax.swing.*;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class SortingGrads {
	public static void main(String[] args) throws FileNotFoundException {
		StudentInput si = new StudentInput();
		Students s = new Students(); //Empty Constructor doesn't actually do anything, suppressing error
		HashMap<String, Double> hm = new HashMap<>();
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

					for (int i = 0; i < si.getStudents().size(); i++) {
						int j = 1;
						String addStr = si.getStudents().get(i);

						//if a name already exists, make a student <student name> + number, to prevent false collisions
						if (hm.containsKey(si.getStudents().get(i))) {
							while (true) {
								if (!(hm.containsKey(si.getStudents().get(i) + " " + j))) {
									addStr += " " + j;

									break;
								}

								j++;
							}
						}

						addStr = addStr.trim();

						hm.put(addStr, round(si.getStudentGPA().get(i), 2));
					}

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

							JTextField strField = new JTextField();
							JTextField doubField = new JTextField();

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

					for (int i = 0; i < tmpNames.size(); i++) {
						int j = 1;
						String addStr = tmpNames.get(i);

						//if a name already exists, make a student <student name> + number, to prevent false collisions
						if (hm.containsKey(tmpNames.get(i))) {
							while (true) {
								if (!(hm.containsKey(tmpNames.get(i) + " " + j))) {
									addStr += " " + j;

									break;
								}

								j++;
							}
						}

						addStr = addStr.trim();

						hm.put(addStr, round(tmpGPA.get(i), 2));
					}

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
	}

	//Checks if string input is a valid integer
	public static boolean isParsableInteger(String input) {
		boolean parsable = true;

		try {
			Integer i = Integer.parseInt(input); //assignment to get rid of warnings
		} catch (NumberFormatException nfe) {
			parsable = false;
		}

		return parsable;
	}

	//Checks if string input is a valid double
	public static boolean isParsableDouble(String input) {
		boolean parsable = true;

		try {
			Double d = Double.parseDouble(input); //assignment to get rid of warnings
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
}
