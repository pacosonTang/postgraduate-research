package com.zhongqi.kmeans;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Random;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Soccer {

	private String inpath = "E:/bench-cluster/temp-resource/soccer.xlsx";
	
	// 聚类个数
	static int cluster = 3;
	// 质心数组（3 个3维）
	static double[][] centroid = new double[3][3];
	// 球队数组
	static String[] team = new String[15];
	//数据项数组， 最后一列表示所属 类别
	double[][] items_data = new double[15][cluster + 1];
			
	public static void main(String[] args) {

		Soccer soccer = new Soccer();
		soccer.firstKmeans();
	}

	public void firstKmeans(){
		
		//获得遍历行 的迭代器
		Iterator<Row> itr = readIterator();
		Row row = null;
		
		// 前3行数据，分配类别
		for (int i = 0; i < cluster; i++) 
			items_data[i][cluster] = i;
		
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
		for (int i = 0; i < items_data.length; i++)  
			items_data[i][cluster] = assignment(items_data[i]);
		
		printResult(1);
		
		// 转向 其他函数 继续 kmeans(1.更新质心 + 2.分配指派簇)
		for (int i = 2; i < 4; i++) 
			refinementAndAssignment(i);
	}
	
	public void refinementAndAssignment(int ith){
		
		//1.更新质心 (refinement-精炼过程)
		setOtherCentroid();
		
		//2.指派分配 item(Assignment-指派分配过程)
		for (int i = 0; i < items_data.length; i++)  
			items_data[i][cluster] = assignment(items_data[i]);
		//打印聚类结果
		printResult(ith);
	}
	
	//将item 分类指派
	public int assignment(double[] item){
		
		double sum = 0, minSum = 0;
		int minimal = 0;
		
		for (int j = 0; j < 3; j++) {
			sum += Math.pow(item[j] - centroid[0][j], 2.0);
		}
		minSum = sum;
		
		for (int i = 1; i < 3; i++) {
			sum = 0;
			for (int j = 0; j < 3; j++) {
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

		int[] classnum = new int[3]; 
		int temp = 0;
		
		//重置质心数组
		resetCentroid();
		for (int i = 0; i < items_data.length; i++) {
			
			temp = (int)items_data[i][3];
			centroid[temp][0] += items_data[i][0];
			centroid[temp][1] += items_data[i][1];
			centroid[temp][2] += items_data[i][2];
			classnum[temp] += 1;  
		}
		
		//更新质心
		for (int i = 0; i < cluster; i++) {
			
			centroid[i][0] /= classnum[i];
			centroid[i][1] /= classnum[i];
			centroid[i][2] /= classnum[i];
		}
	}
	
	//打印 某次聚类后 的结果
	public void printResult(int ith){
		
		System.out.println("==== 第" + ith + "次聚类后的效果 ====");
		for (int i = 0; i < cluster; i++) {
			
			System.out.print("分配到类" + (i+1) + " 的球队有： ");
			for (int j = 0; j < items_data.length; j++) {
				
				if(items_data[j][cluster] == i)
					System.out.print(team[j] + "  ");
			}
			System.out.println("");
		}
		System.out.println("==== 第" + ith + "次聚类 over====\n");
	}
	
	//将质心数组 重置为 0
	public void resetCentroid(){
		
		for (int i = 0; i < centroid.length; i++) {
			centroid[i][0] = 0;
			centroid[i][1] = 0;
			centroid[i][2] = 0;
		}
	}
	
	//分配初始质心
	public void setFirstCentroid(){
		
		Random rand = new Random();
		int[] init = {1, 12, 9};
		
		for (int i = 0; i < cluster; i++) {
			//init[i] = rand.nextInt(15);
			centroid[i][0] = items_data[init[i]][0];
			centroid[i][1] = items_data[init[i]][1];
			centroid[i][2] = items_data[init[i]][2];
		}
		System.out.println("====  大家好， 初始质心为：" + team[init[0]] + "  " + team[init[1]] + "  " + team[init[2]] + "  ====");
		System.out.println("============================================================================");
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
