import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class SortingGrads {
	public static void main(String[] args) throws FileNotFoundException {
		StudentInput si = new StudentInput();
		Students s;
		HashMap<String, Double> hm = new HashMap<>();

		si.getExcelData();

		for (int i = 0; i < si.getStudents().size(); i++) {
			int j = 1;
			String addStr = si.getStudents().get(i);

			//if a name already exists, make a student <student name> + number, to prevent false collisions
			if (hm.containsKey(si.getStudents().get(i))) {
				while (true) {
					if ( !(hm.containsKey(si.getStudents().get(i) + " " + j)) ) {
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

	public static boolean isParsableInteger(String input) {
		boolean parsable = true;

		try {
			Integer.parseInt(input);
		} catch (NumberFormatException nfe) {
			parsable = false;
		}

		return parsable;
	}

	public static double round(double value, int places) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}
}
