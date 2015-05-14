import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

public class StudentOutput {
	private String[] orderedStudents;
	private Workbook workbook;

	public StudentOutput(String[] orderedStudents) {
		this.orderedStudents = orderedStudents;
	}

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

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void writeExcel() {
		File output;
		String fileName = "output";
		boolean fileAvailable;
		int loop = 1;

		do {
			fileAvailable = true;
			output = new File(fileName + ".xlsx");

			if (output.exists()) {
				output.delete();
			}

			try {
				output.createNewFile();
			} catch (IOException ioe) {
				fileName = "output " + loop;
				fileAvailable = false;
				loop++;
			}
		} while (!fileAvailable);

		setExcelFile(output);

		Sheet sheet = workbook.createSheet();

		for (int i = 0; i < this.orderedStudents.length; i++) {
			Row r = sheet.createRow(i);

			Cell[] c = new Cell[]{r.createCell(0), r.createCell(1)};

			c[0].setCellValue(1);
			c[1].setCellValue(orderedStudents[i]);
		}
	}
}
