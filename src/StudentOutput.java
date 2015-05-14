import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StudentOutput {
	private String[] orderedStudents;

	public StudentOutput(String[] orderedStudents) {
		this.orderedStudents = orderedStudents;
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void writeExcel() throws FileNotFoundException {
		File output;
		String fileName = "output";
		boolean fileAvailable;
		int loop = 1;
		FileOutputStream fos;

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

		fos = new FileOutputStream(output);

		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet();

		for (int i = 0; i < this.orderedStudents.length; i++) {
			Row r = sheet.createRow(i);

			Cell[] c = new Cell[]{r.createCell(0), r.createCell(1)};

			c[0].setCellValue(i);
			c[1].setCellValue(orderedStudents[i]);
		}

		try {
			workbook.write(fos);
		} catch (IOException ignored) {

		}
	}
}
