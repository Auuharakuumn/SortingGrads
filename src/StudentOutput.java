import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentOutput {
	private String[] orderedStudents;
	private String[][] box;

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

			c[0].setCellValue(i + 1);
			c[1].setCellValue(orderedStudents[i]);
		}

		try {
			workbook.write(fos);
		} catch (IOException ignored) {

		}
	}

	private void setBox() {
		ArrayList<String> nameList = new ArrayList<>(Arrays.asList(orderedStudents));

		while (nameList.size() % 30 != 0) {
			nameList.add("Empty");
		}

		int rows = nameList.size() / 30;

		this.box = new String[rows][30];

		int allCtr = 0;
		for (int i = 0; i < box.length; i++) {
			for (int j = 0; j < 30; j++) {
				box[i][j] = nameList.get(allCtr);
				allCtr++;
			}
		}
	}

	public void displayBox() {
		this.setBox();

		NameGrid frame = new NameGrid("Boxen");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addComponents(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}

	private class NameGrid extends JFrame {
		public NameGrid(String name) {
			super(name);
			setResizable(false);
		}

		public void addComponents(final Container pane) {
			final JPanel jpanel = new JPanel();
			JScrollPane scroll = new JScrollPane(jpanel);
			GridLayout grid = new GridLayout(box.length, 30, 5, 5);
			jpanel.setLayout(grid);
			int count = 0, count2 = 0;

			for (String s[] : box) {
				for (String ss : s) {
					Button tmp = new Button(ss);
					jpanel.add(tmp);

					count++;
					count2++;
					if (count == 15) {
						count = 0;

						if (count2 == 30) {
							count2 = 0;
						} else {
							jpanel.add(new JSeparator(SwingConstants.VERTICAL));
						}
					}
				}
			}
			setPreferredSize(new Dimension(900, 450));

			pane.add(scroll);

			setResizable(true);
		}
	}
}
