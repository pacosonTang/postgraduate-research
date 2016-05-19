package com.research.io;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class DataWrite {
	private String filepath;
	private FileOutputStream out;

	public DataWrite(String filepath) {
		this.filepath = filepath;
	}

	/**
	 * @param data is a double array storing data written into xlsx.
	 * @param colPrefix is a row tag.
	 * @param headColNum is column number.
	 * @param sheetName is the name of sheet.
	 * @throws IOException
	 */
	public void writeArray(double[][] data, String colPrefix, int headColNum, String sheetName)
			throws IOException {
		XlsxDataModel model = XlsxDataModel.getInstance(sheetName);
		Row row = model.getSheet().createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("");

		for (int i = 1; i <= headColNum; i++) {
			cell = row.createCell(i);
			cell.setCellValue(i);
		} // build the head line over

		for (int i = 0; i < data.length; i++) {
			row = model.getSheet().createRow(i + 1);

			cell = row.createCell(0);
			cell.setCellValue(colPrefix + (i + 1));
			for (int j = 0; j < data[i].length; j++) {
				cell = row.createCell(j + 1);
				cell.setCellValue(data[i][j]);
			}
		}// write the cluster result(centroid vector) into xlsx over

		out = new FileOutputStream(filepath);
		model.getWorkbook().write(out);
		out.flush();
		out.close();

		System.out.println("write " + filepath + " over");
	}
	
	/**
	 * @param data is a int array storing data written into xlsx.
	 * @param colPrefix is a row tag.
	 * @param headColNum is column number.
	 * @param sheetName is the name of sheet.
	 * @throws IOException
	 */
	public void writeArray(int[][] data, String colPrefix, int headColNum, String sheetName)
			throws IOException {
		XlsxDataModel model = XlsxDataModel.getInstance(sheetName);
		Row row = model.getSheet().createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("");

		for (int i = 1; i <= headColNum; i++) {
			cell = row.createCell(i);
			cell.setCellValue(i);
		} // build the head line over

		for (int i = 0; i < data.length; i++) {
			row = model.getSheet().createRow(i + 1);

			cell = row.createCell(0);
			cell.setCellValue(colPrefix + (i + 1));
			for (int j = 0; j < data[i].length; j++) {
				cell = row.createCell(j + 1);
				cell.setCellValue(data[i][j]);
			}
		}// write the cluster result(centroid vector) into xlsx over

		out = new FileOutputStream(filepath);
		model.getWorkbook().write(out);
		out.flush();
		out.close();

		System.out.println("write " + filepath + " over");
	}
}
