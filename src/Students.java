import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Students {
	private ArrayList<String> order;
	private HashMap<String, Double> studentsGPA;
	private String[] students;
	private String[] highest;
	private int currReserved;
	private int endOfFour;

	public Students(HashMap<String, Double> studentsGPA, String[] students) {
		this.studentsGPA = studentsGPA;
		this.students = students;
		this.highest = new String[2];
		this.currReserved = 0;

		int run = 0;
		//Sets highest[0] to the valedictorian and highest[1] to the salutatorian
		for (String s : students) {

			//if its the first two runs through the loop, set highest to the first values
			if (run <= 1) {
				highest[0] = studentsGPA.get(students[0]) > studentsGPA.get(students[1]) ? students[0] : students[1];
				highest[1] = studentsGPA.get(students[0]) < studentsGPA.get(students[1]) ? students[0] : students[1];
			} else if (studentsGPA.get(s) > studentsGPA.get(highest[1])) {

				//otherwise, if the student is better than the salutatorian, set them to valedictorian or salutatorian
				if (studentsGPA.get(s) > studentsGPA.get(highest[0])) {
					highest[1] = highest[0];
					highest[0] = s;
				} else {
					highest[1] = s;
				}
			}

			run++;
		}
	}

	public void sort() {
		this.order = new ArrayList<>();

		//valedictorian and salutatorian first, obviously
		this.order.add(highest[0]);
		this.order.add(highest[1]);

		ArrayList<String> overFour = new ArrayList<>();
		ArrayList<String> underFour = new ArrayList<>();

		for (String s : order) {
			if (studentsGPA.get(s) > 4.0) {
				if ( !(s.equals(highest[0]) || s.equals(highest[1])) ) {
					overFour.add(s);
				}
			} else {
				underFour.add(s);
			}
		}

		this.endOfFour = overFour.size() + 1;

		Collections.sort(overFour, new LastNameCompare());
		Collections.sort(underFour, new LastNameCompare());

		this.order.addAll(overFour);
		this.order.addAll(underFour);
	}

	/*public String[] getStudents() {
		return students;
	}

	public HashMap<String, Double> getStudentsGPA() {
		return studentsGPA;
	}*/

	//Adds a student named reserved to the specified number of spots that are multiples of 30 and multiples of 30 plus 1, starting after 4.0 students
	public void reserveSeats(int reserved) {
		if (currReserved >= reserved) {
			return;
		}

		for (int i = endOfFour + 1; i < order.size(); i++) {
			if ((i - 1 % 30) == 0) {
				order.add(i, "Reserved");
			}

			this.currReserved++;

			if (currReserved == reserved) {
				return;
			}
		}

		for (int i = endOfFour + 1; i < order.size(); i++) {
			if ((i % 30) == 0) {
				order.add(i, "Reserved");
			}

			this.currReserved++;

			if (currReserved == reserved) {
				return;
			}
		}
	}

	//compare the two last names, to be used in the comparator
	static class LastNameCompare implements Comparator<String> {
		public int compare(String str1, String str2) {
			String[] arr1 = str1.split(" ");
			String[] arr2 = str2.split(" ");

			if (arr1.length > 1 && arr2.length > 1) {
				return arr1[arr1.length-1].compareTo(arr2[arr2.length-1]);
			} else {
				return 0;
			}
		}
	}

	public ArrayList<String> getOrder() {
		return order;
	}
}