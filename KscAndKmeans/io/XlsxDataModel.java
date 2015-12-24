package com.research.io;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxDataModel
{
	public static Workbook workbook;
	public static Sheet sheet;

	public XlsxDataModel(String sheetName)
	{
		this.workbook = new XSSFWorkbook();;
		this.sheet = workbook.createSheet(sheetName);
	}
}
