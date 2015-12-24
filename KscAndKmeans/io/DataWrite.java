package com.research.io;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.research.Alg.ClusterAlg;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class DataWrite 
{
	private String dataPath;
	private FileOutputStream out; 
	
	public DataWrite(String dataPath)
	{
		this.dataPath = dataPath;
	}
	
	/**
	 * write the silhouette array into xlsx by path + pathPostfix
	 * @param array an array storing silhouette
	 * @param pathPostfix a postfix of the full file path
	 * @param firstCol first column in xlsx, which is tag
	 * @throws IOException
	 */
	public void writeSilhouette(double[][] array, String pathPostfix, String firstCol) throws IOException
	{
		Row row ; 
		Cell cell;
		XlsxDataModel model;
		
		model = new XlsxDataModel(ClusterAlg.algName);
		
		for (int i = 0; i < array[0].length; i++)
		{
			row = model.sheet.createRow(i);
			row.setHeightInPoints(12.75f);
			
			cell = row.createCell(0);
			cell.setCellValue(firstCol + (i+1));
			cell = row.createCell(1);
			cell.setCellValue(array[0][i]);
		}
   	 		   	
	    //out = new FileOutputStream(dataPath + ClusterAlg.algName + ClusterParam.cluster +  "Silhouette.xlsx");
		out = new FileOutputStream(dataPath + pathPostfix);
	     
	    XlsxDataModel.workbook.write(out);
	    out.flush();
	    out.close();
	     
	    System.out.println(ClusterAlg.algName + " writeArray executing over");
	}
	
	/**
	 * write the array into xlsx by path + pathPostfix
	 * @param array an array storing data
	 * @param pathPostfix a postfix of the full file path
	 * @param firstCol first column in xlsx, which is tag
	 * @throws IOException
	 */
	public void writeArray(double[][] array, String pathPostfix, String firstCol) throws IOException
	{
		Row row ; 
		Cell cell;
		XlsxDataModel model;
		
		model = new XlsxDataModel(ClusterAlg.algName);
		
		for (int i = 0; i < array.length; i++)
		{
			row = model.sheet.createRow(i);
			row.setHeightInPoints(12.75f);
			cell = row.createCell(0);
			cell.setCellValue(firstCol + (i+1));
			for (int j = 1; j < array[i].length; j++)
			{
				cell = row.createCell(j);
				cell.setCellValue(array[i][j]);				
			}
		}
   	 		   	
	    //out = new FileOutputStream(dataPath + ClusterAlg.algName + ClusterParam.cluster +  "Silhouette.xlsx");
		out = new FileOutputStream(dataPath + pathPostfix);
	     
	    XlsxDataModel.workbook.write(out);
	    out.flush();
	    out.close();
	     
	    System.out.println(ClusterAlg.algName + " writeArray executing over");
	}
	
	/**
	 * write the data array into the xlsx
	 * @param data data array
	 * @throws IOException
	 */
	public void writeCentroid(double[][] data) throws IOException
	{
		Row row ; 
		Cell cell;
		XlsxDataModel model;
		
		model = new XlsxDataModel(ClusterAlg.algName);
		row = model.sheet.createRow(0);
		row.setHeightInPoints(12.75f);
		cell = row.createCell(0);
   	 	cell.setCellValue(""); 
   	 
        for (int i = 1; i <= ClusterData.dimension; i++) 
        {
        	cell = row.createCell(i);
       	 	cell.setCellValue(i); 
        } //  build the head line over
                       
        for (int i = 0; i < data.length; i++) 
        {
        	row = XlsxDataModel.sheet.createRow(i+1); 
            
        	if(ClusterData.centroid[i] == null) 
            	continue;
        	
        	cell = row.createCell(0); 
        	cell.setCellValue(ClusterAlg.algName + "C"+ (i+1));
            for (int j = 0; j < ClusterData.dimension; j++) 
            {
            	cell = row.createCell(j+1); 
            	cell.setCellValue(data[i][j]);   
            }
        }// write the cluster result(centroid vector) into xlsx over
        
        out = new FileOutputStream(dataPath + ClusterAlg.algName + ClusterParam.cluster + "_centroid.xlsx");
        
        XlsxDataModel.workbook.write(out);
        out.flush();
        out.close();
        
        System.out.println(ClusterAlg.algName + " writeCentroid executing over");
	}
}



