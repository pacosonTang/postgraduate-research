package com.research.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.research.pojo.ClusterData;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class DataRead {
	private String dataPath;

	public DataRead(String dataPath) {
		this.dataPath = dataPath;
	}
	
	/**
	 * 
	 * @param row_start is a startup row startup index for reading. 
	 * @param col_start is a startup column index for reading.
	 * @param array is a double array storing the data read from some xlsx. 
	 */
	public final void readDataToArray(int row_start, int col_start, double[][] array) {
		
		Iterator<Row> itr = readIterator(); // 获得遍历行 的迭代器
		Row row = null; // 行对象
		int row_index = 0;// 行索引
		int col_index = 0;// 列索引
		int row_length = array.length; // 数据行数
		int col_length = array[0].length; // 数据列数
		
		// the first row is ommited for it stores column index
		if (itr.hasNext()) {
			itr.next();
		}
		
		// 定位行指针到 row_start
		while(itr.hasNext()){
			row_index++;
			if(row_index == row_start) {
				row_index = 0;
				break;
			}
			itr.next();
		}// 定位 over
		
		// other rows stores time series data
		while (itr.hasNext() && (row_index<row_length)) {
			col_index = 0;
			row = itr.next();
			Iterator<Cell> cellIterator = row.cellIterator(); // 遍历每行单元格的迭代器
			
			Cell cell = null; 
			// the first column is ommited for it stores row index
			if(cellIterator.hasNext()) { 
				cellIterator.next();
			}
			
			// 定位列指针到 col_start
			while(cellIterator.hasNext()) {
				col_index++;
				if(col_index == col_start) {
					col_index = 0;
					break;
				}
				cellIterator.next();
			}// 定位 over
			
			while (cellIterator.hasNext() && (col_index<col_length)) {
				cell = cellIterator.next();
				array[row_index][col_index++] = cell.getNumericCellValue();
			}// 一行数据读取完毕

			row_index++;			
		} // 数据行读取完毕
	}
	
	// read data from xlsx to array
	public final void readDataToArray(int row_start, int row_end) {
		
		Iterator<Row> itr = readIterator(); // 获得遍历行 的迭代器
		Row row = null; // 行对象
		int index = 0;// 行索引
		int row_length = row_end-row_start+1; // 数据行数
		
		// the first row is ommited for it stores column index
		if (itr.hasNext()) {
			row = itr.next();
		}
		
		// 定位行指针到 row_start
		while(itr.hasNext()){
			index++;
			if(index == row_start) {
				break;
			}
			row = itr.next();
		}
		
		index -= row_start;
		// other rows stores time series data
		while (itr.hasNext() && (index!=row_length)) {
			int j = 0;
			row = itr.next();
			Iterator<Cell> cellIterator = row.cellIterator(); // 遍历每行单元格的迭代器
			
			Cell cell = null; 
			// the first column is ommited for it stores row index
			if(cellIterator.hasNext())
				cell = cellIterator.next();

			while (cellIterator.hasNext()) {
				cell = cellIterator.next();
				ClusterData.items[index][j++] = cell.getNumericCellValue();  
			}// 一行数据读取完毕

			index++;			
		} // 数据行读取完毕
	}

	// 获取数据行 迭代器
	final Iterator<Row> readIterator() {
		Iterator<Row> itr = null;
		try {
			File excel = new File(this.dataPath);
			FileInputStream fis = new FileInputStream(excel);
			// 创建工作簿
			XSSFWorkbook book = new XSSFWorkbook(fis);
			// 创建工作簿下的第一页纸张
			XSSFSheet sheet = book.getSheetAt(0);

			// 纸张的迭代器，用于遍历行
			itr = sheet.iterator();
			// Iterating over Excel file in Java

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return itr;
	}
}
