package com.research.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.research.pojo.ClusterData;
import com.research.pojo.TimeSeries;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class DataRead 
{
	private String dataPath;
	
	public DataRead(String dataPath)
	{
		this.dataPath = dataPath;
	}
	
	//read data from xlsx to array
	public void readDataToArray(ClusterData kmeansData)
	{
		Iterator<Row> itr ; //获得遍历行 的迭代器
		Row row ; //行对象
		int index ;//行索引
		String[] item_tag;
		double[][] items;
		
		item_tag = kmeansData.getItemTag();
		items = kmeansData.getItems();
		
		itr = readIterator();
		row = null;
		index = 0;
		
		//the first row is ommited for it stores column index
		if(itr.hasNext()) 
		{
       	 	row = itr.next();
		}
		// other rows stores time series data
		while (itr.hasNext()) 
		{
			int j ;
			Cell cell;
			
			j = 0;
			row = itr.next(); 
			Iterator<Cell> cellIterator = row.cellIterator(); //遍历每行单元格的迭代器
			cell = cellIterator.next(); 
			
			item_tag[index] = cell.getStringCellValue(); // 从 xlsx 中抽取行标签 
			while (cellIterator.hasNext()) 
			{
                cell = cellIterator.next();
                items[index][j++] = cell.getNumericCellValue(); // 依次从xlsx 中抽取一行中的每个单元格
            }//一行数据读取完毕			
			
			index++;
		} // 数据行读取完毕
	}
	
	//获取数据行 迭代器
	public Iterator<Row> readIterator()
	{
		Iterator<Row> itr = null;
		try {
			File excel = new File(this.dataPath);
		    FileInputStream fis = new FileInputStream(excel);
		    //创建工作簿
		    XSSFWorkbook book = new XSSFWorkbook(fis);
		    //创建工作簿下的第一页纸张
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
