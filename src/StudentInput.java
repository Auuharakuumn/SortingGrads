import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class StudentInput {
	private ArrayList<Double> studentGPA;
	private ArrayList<String> students;
	private Workbook workbook;
	private boolean success;

	//returns 1 if the file is the wrong format, and 2 if there is an ioexception
	//Sets the workbook from a selected file
	private int setExcelFile(File file) {
		Workbook wb;

		try {
			wb = WorkbookFactory.create(file);
		} catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException ife) {
			return 1;
		} catch (IOException ioe) {
			return 2;
		}

		this.workbook = wb;

		return 0;
	}

	//Sets studentGPA and students to their respective gpas and names
	public void getExcelData() {
		JFileChooser fileDialog = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("1997 and 2007 Excel Documents",
				"xls", "xlsx");

		fileDialog.setFileFilter(filter);

		int returnVal;

		do {
			returnVal = fileDialog.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				returnVal = setExcelFile(fileDialog.getSelectedFile());
			} else if (returnVal == JFileChooser.CANCEL_OPTION) { //Quits if the user hits cancel
				this.success = false;

				return;
			}

			if (returnVal == 1) {
				JOptionPane.showMessageDialog(null, "File format is invalid.", "File Input Error", JOptionPane.WARNING_MESSAGE);
			}
		} while (returnVal != 1 && returnVal != 2); //Repeats a file dialog while the document isn't valid

		for (int i = 0; i < this.workbook.getNumberOfSheets(); i++) {
			Sheet sheet = this.workbook.getSheetAt(i);

			for (Row row : sheet) {
				for (int j = 0; j <= 1; j++) {
					CellReference ref = new CellReference(row.getCell(j));

					switch (j) {
						case 0:
							this.students.add(ref.formatAsString());

							break;
						case 1:
							this.studentGPA.add(Double.parseDouble(ref.formatAsString()));

							break;
						default:
							break;
					}
				}
			}
		}
	}

	public ArrayList<Double> getStudentGPA() {
		return studentGPA;
	}

	public ArrayList<String> getStudents() {
		return students;
	}

	public boolean isSuccess() {
		return success;
	}
}
