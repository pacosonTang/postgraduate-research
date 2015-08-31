package com.zhongqi.kmeans;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Random;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TimeSeriesKmeans {

	private String inpath = "E:/bench-cluster/temp-resource/timeSeries.xlsx";
	
	// 聚类个数
	static int cluster = 3;
	//数据维度
	static int dimension = 128;
	//数据项数
	static int rowno = 14;
	
	// 质心数组（3 个3维）
	static double[][] centroid = new double[cluster][dimension];
	// 球队数组
	static String[] team = new String[rowno];
	//数据项数组， 最后一列表示所属 类别
	double[][] items_data = new double[rowno][dimension + 1];
			
	public static void main(String[] args) {

		TimeSeriesKmeans soccer = new TimeSeriesKmeans();
		soccer.firstKmeans();
	}

	public void firstKmeans(){
		
		//获得遍历行 的迭代器
		Iterator<Row> itr = readIterator();
		Row row = null;
		
		int index = 0;//行索引
		
		//double[] unit = new double[128];
		if(itr.hasNext()) 
			//第一行被 忽略
       	 	row = itr.next();
		while (itr.hasNext()) {
			
			int j = 0;
			row = itr.next(); 
			Iterator<Cell> cellIterator = row.cellIterator();
			
			//第一个单元格（第一列） 被 用于存储球队名称
			Cell cell = cellIterator.next(); 
			team[index] = cell.getStringCellValue();
			
			while (cellIterator.hasNext()) {
            	
                cell = cellIterator.next();
                items_data[index][j++] = cell.getNumericCellValue();
            }//一行数据读取完毕
			
			index++;
		}

		setFirstCentroid();
		//测试用
		//iran(3), Japan(1), Bahlin(12)
		//assignment(items_data[3]);
		
		
		//初始 分派 item
		for (int i = 0; i < rowno; i++)  
			items_data[i][dimension] = assignment(items_data[i]);
		
		printResult(1);
		
		// 转向 其他函数 继续 kmeans(1.更新质心 + 2.分配指派簇)
		for (int i = 2; i < 6; i++) 
			refinementAndAssignment(i);
	}
	
	public void refinementAndAssignment(int ith){
		
		//1.更新质心 (refinement-精炼过程)
		setOtherCentroid();
		
		//2.指派分配 item(Assignment-指派分配过程)
		for (int i = 0; i < rowno; i++)  
			items_data[i][dimension] = assignment(items_data[i]);
		//打印聚类结果
		printResult(ith);
	}
	
	//将item 分类指派
	public int assignment(double[] item){
		
		double sum = 0, minSum = 0;
		int minimal = 0;
		
		for (int j = 0; j < dimension; j++) {
			sum += Math.pow(item[j] - centroid[0][j], 2.0);
		}
		minSum = sum;
		
		for (int i = 1; i < cluster; i++) {
			sum = 0;
			for (int j = 0; j < dimension; j++) {
				sum += Math.pow(item[j] - centroid[i][j], 2.0);
			}
			if(sum < minSum) {
				minSum = sum;
				minimal = i;
			}
		}
		return minimal;
	}
	
	// 分配非初始质心
	public void setOtherCentroid() {

		int[] classnum = new int[cluster]; 
		int temp = 0;
		
		//重置质心数组为零
		resetCentroid();
		//更新质心数组
		for (int i = 0; i < rowno; i++) {
			
			temp = (int)items_data[i][dimension];
			for (int j = 0; j < dimension; j++) 
				centroid[temp][j] += items_data[i][j];
			
			classnum[temp] += 1;  
		}
		
		//更新质心
		for (int i = 0; i < cluster; i++)  
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] /= classnum[i];
	}
	
	//将质心数组 重置为 0
	public void resetCentroid(){
		
		for (int i = 0; i < cluster; i++)  
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = 0;
		 
	}
	
	//分配初始质心
	public void setFirstCentroid(){
		
		Random rand = new Random();
		int[] init = {1, 12, 9};
		
		for (int i = 0; i < cluster; i++)  
			//init[i] = rand.nextInt(15);
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = items_data[init[i]][j];
		 
		System.out.println("====  大家好， 初始质心为：" + team[init[0]] + "  " + team[init[1]] + "  " + team[init[2]] + "  ====");
		System.out.println("============================================================================");
	}
	
	//打印 某次聚类后 的结果
	public void printResult(int ith){
		
		System.out.println("==== 第" + ith + "次聚类后的效果 ====");
		for (int i = 0; i < cluster; i++) {
			
			System.out.print("分配到类" + (i+1) + " 的球队有： ");
			for (int j = 0; j < rowno; j++) {
				
				if(items_data[j][dimension] == i)
					System.out.print(team[j] + "  ");
			}
			System.out.println("");
		}
		
		System.out.println("==== 第" + ith + "次聚类 over====\n");
	}
		
	//获取数据行 迭代器
	public Iterator<Row> readIterator(){

		Iterator<Row> itr = null;
		try {
			File excel = new File(this.inpath);
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
