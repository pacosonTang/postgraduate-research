package com.research.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.research.io.DataWrite;

public class DataProcess {
	public static void main(String[] args) throws IOException {
		String basedir = System.getProperty("user.dir") + File.separator;
		BufferedReader reader = 
				new BufferedReader(new InputStreamReader(
						new FileInputStream(basedir+"iris_data.txt")));
		String str;
		
		// the core code begins.
		int capacity=10; // buffer size, of course you can specify other values of capacity.		
		double[][] data = new double[capacity][];
		int lineNum = 0;
		
		while((str=reader.readLine())!=null) {
			String[] array = str.split(",");
			double[] temp = new double[array.length-1];
			
			for (int i = 0; i < array.length-1; i++) {
				temp[i] = Double.valueOf(array[i]);
			}
			if(lineNum%capacity==0) { 
				double[][] datacopy = new double[lineNum+capacity][];
				for (int i = 0; i < data.length; i++) {
					datacopy[i] = data[i];
				}
				data = null;
				data = datacopy;				
			} 
			data[lineNum++] = temp;			
		}
		double[][] datacopy = new double[lineNum][];
		for (int i = 0; i < datacopy.length; i++) {
			datacopy[i] = data[i];
		}
		data = null;
		data = datacopy;
		// the core code ends.
		
		DataWrite writer = new DataWrite(basedir+"gmeans_irise1.xlsx");		
		writer.writeArray(data, "item", data[0].length, "iris");
	}
}
