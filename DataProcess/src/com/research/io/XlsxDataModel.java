package com.research.io;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxDataModel
{
	private Workbook workbook;
	private Sheet sheet;
	static XlsxDataModel model;

	private XlsxDataModel(String sheetName){
		this.workbook = new XSSFWorkbook();;
		this.sheet = workbook.createSheet(sheetName);
	}
	
	public static XlsxDataModel getInstance(String sheetName){
		if(model == null){
			model = new XlsxDataModel(sheetName);
		}
		return model;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public Sheet getSheet() {
		return sheet;
	}
}

 
 
