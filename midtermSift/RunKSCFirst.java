package com.zhongqi.running;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 
 * @author lenovo
 *
 * 只进行了 一次 聚类，初始聚类
 */
public class RunKSCFirst {
	
	private String inpath = "E:/bench-cluster/temp-resource/timeSeries.xlsx";
		
	// 聚类个数
	static final int cluster = 3;
	//数据维度
	static final int dimension = 128;
	//数据项数
	static final int rowno = 14;
	//单位阵
	static final int[][] unit = new int[dimension][dimension];
	//暂存二维结果矩阵
	// static final double[][] tempDouble = new double[dimension][dimension];
	
	// 质心数组（3 个3维）
	static double[][] centroid = new double[cluster][dimension];
	// 球队数组
	static final String[] team = new String[rowno];
	//数据项数组， 最后一列表示所属 类别
	final double[][] items_data = new double[rowno][dimension + 1];
		
	
	public static void main(String[] args) {

		RunKSCFirst rk = new RunKSCFirst();
		rk.firstKsc();
	}

	public void firstKsc(){
		
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
		
		//初始 分派 item
		for (int i = 0; i < rowno; i++) {
			double[][] tempDouble = new double[dimension][dimension];
			items_data[i][dimension] = assignment(computeTransM(tempDouble, items_data[i]));
		}
		
		printResult(1);
		
		/*// 转向 其他函数 继续 kmeans(1.更新质心 + 2.分配指派簇)
		for (int i = 2; i < 6; i++) 
			refinementAndAssignment(i);*/
	}
	
	//进行KSC算法的 精炼和 指派 
	public void refinementAndAssignment(int ith){
		
		//1.更新质心 (refinement-精炼过程)
//		setOtherCentroid();
		
		//2.指派分配 item(Assignment-指派分配过程)
		for (int i = 0; i < rowno; i++) {
			double[][] tempDouble = new double[dimension][dimension];
			items_data[i][dimension] = assignment(computeTransM(tempDouble, items_data[i]));
		}
		//打印聚类结果
		printResult(ith);
	}
	
	// 更新非初始质心
	 
	//将 item 分类指派
	public int assignment(double[][] data){
		
		double[] temp = new double[dimension];
		double[] centroid_norm = new double[3];
		double sum = 0, minSum = 0;
		int minimal = 0;
		int index = 0; //所选cluster的索引
		
		// 0.compute centroid norm
		for (int j = 0; j < cluster; j++)  
			centroid_norm[j] = l2norm(centroid[j]);
		
		// 1.mul ut and M ; 2. (ut*M) * u
		
		// 1.1  先赋值 minSum (index == 0), 经验证，矩阵乘法 算法正确		
		for (int j = 0, k = 0; j < dimension; j++, k++)    
			for (int j2 = 0; j2 < dimension; j2++)  
				temp[j] += centroid[index][j2] * data[j2][k];
		
		for (int j = 0; j < dimension; j++)  
			sum += temp[j] * centroid[index][j];
		sum /= centroid_norm[index];
		minSum = sum;
		minimal = index;
		
		// 1.2 再计算 与 其他（非初始）类的 结果(index > 0)
		for (index = 1; index < cluster; index++) { 
			sum = 0;
			for (int j = 0, k = 0; j < dimension; j++, k++)    
				for (int j2 = 0; j2 < dimension; j2++)  
					temp[j] += centroid[index][j2] * data[j2][k];
			
			for (int j = 0; j < dimension; j++) 
				sum += temp[j] * centroid[index][j]; 
			sum /= centroid_norm[index];
			if (sum < minSum) {
				minSum = sum;
				minimal = index;
			}
		}
		
		return minimal;
	}
	
	//计算 综合矩阵 M
	public double[][] computeTransM(double[][] tempDouble, double[] data){
		
		double norm = -l2norm(data);
		double temp = 0;
//			double[][] tempDouble = new double[dimension][dimension];
		//以下代码的原方法名： mulVectorAndTransposeAndDivNormAndSubUnit
		// 1.1 计算向量与转置向量的乘积; 1.2然后除以 范数; 1.3 和单位阵做加法（减法的逆运算）
		
		for (int i = 0; i < dimension; i++)  
			for (int j = 0; j < dimension; j++) {  
				temp = data[i] * data[j];
				temp /= norm;
				if (i == j)
					temp += 1;
				tempDouble[i][j] += temp;
			}
		return tempDouble;
	}
	
	// 0.计算向量的第2范数
	public double l2norm(double[] data){
		
		double result = 0;
		
		for (int i = 0; i < dimension; i++)  
			result += Math.pow(data[i], 2.0); 
		return result;
	}
	
	//产生单位阵
	public void generateUnitMatrix(){
		
		for (int i = 0; i < dimension; i++)  
			unit[i][i] = 1;
	}
	
	//分配初始质心
	public void setFirstCentroid(){
		
		int[] init = {1, 12, 9};
		
		for (int i = 0; i < cluster; i++)  
			//init[i] = rand.nextInt(15);
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = items_data[init[i]][j];
		 
		System.out.println("====  大家好， 初始质心为：" + team[init[0]] + "  " + team[init[1]] + "  " + team[init[2]] + "  ====");
		System.out.println("============================================================================");
	}
	
	//将质心数组 重置为 0
	public void resetCentroid(){
		
		for (int i = 0; i < cluster; i++)  
			for (int j = 0; j < dimension; j++)  
				centroid[i][j] = 0;
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