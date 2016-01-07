package com.research.debug;

import java.util.Arrays;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import com.research.Alg.KscAlg;
import com.research.evaluation.TimeSeriesSilhouette;
import com.research.io.DataRead;
import com.research.io.DataWrite;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;
import com.research.pojo.TimeSeries;

import static java.lang.System.*;

public class KscDebugTest 
{
	private static KscAlg alg = new KscAlg();
	
	public static void main1(String[] args) 
	{
		int cluster; //聚类个数
		int dimension; // 数据维度
		int rowno; // 数据行数
		int clusterExeNum; // 聚类算法执行次数
		double[][] items; // 暂存数据的二维数组
		String[] itemTag; // 暂存数据行标签的一维数组
		double[][] centroid;//暂存数据的聚类质心
		int[] clusterMemberNum; //暂存各个聚类中的成员个数
		String directory; // 数据文件路径
		DataRead dataRead; //读取数据对象
		DataWrite dataWrite; //写入数据到文件
		TimeSeries timeSeries; // 时间序列数据对象
		KscAlg alg; //kmeans 聚类算法对象
		ClusterParam kp; //kmeans聚类算法的 参数包装类	
		
		cluster = 6;
		dimension = 3;
		rowno = 1000;
		clusterExeNum = 50;
		kp = new ClusterParam(cluster, clusterExeNum);
		items = new double[rowno][dimension + 1];// the last field holds the cluster tag
		itemTag = new String[rowno];
		centroid = new double[cluster][dimension];
		clusterMemberNum = new int[cluster];		
		//directory = "E:/bench-cluster/temp-resource/QiaoGuide/originalTimeSeries.xlsx";
		directory = "E://bench-cluster//temp-resource//QiaoGuide//151231_machine_learning//";
		//dataRead = new DataRead(directory + "originalTimeSeries.xlsx");
		timeSeries = new TimeSeries(items, itemTag, centroid, clusterMemberNum, rowno, dimension, directory, new int[3]);
		test_distanceOfSampleToSample();
		
	}
	
	public static void test_distanceOfSampleToSample()
	{
		TimeSeriesSilhouette tss;
		double[] xVector;
		double[] yVector;
		
		tss = new TimeSeriesSilhouette();
		xVector = new double[]{1, 2, 3};
		yVector = new double[]{3, 1, 2};
		
	}
	
	public static void main2(String[] args)
	{
		double[][] x;
		double[][] y;
		
		x = new double[][]{{1.0, 2.0}, {}};
		y = new double[][]{{1,2},{3,4}};
		// xT * y * x = 27
	}
	
	public static void main3(String[] args)
	{
		double[] a = new double[]{1.0 ,2.0 ,3.0};
		modify(a);
		for(double t : a)
			out.print(t + " ");
	}
	
	public static void modify(double[] data)
	{
		Arrays.fill(data, 0);
	}
	
	public static void main(String[] args)
	{
		double[][] data = new double[][]{{-1, -4, 1}, {1, 3, 0}, {0, 0, 2}};
		computeSmallestEigenVector(data);
	}
	
	/**
	 * 求标记为index cluster 的特征向量
	 * @param index 求标记为index cluster 的特征向量
	 * @param data  原始矩阵
	 */
	public static double[] computeSmallestEigenVector(double[][] data) { 
		
		double temp = 0, minimal = 0;
		int[] column = {0};
		int i = 0;  
		double[] array;
		
		array = new double[data.length];
		Matrix matrix2d = new Matrix(data);
		EigenvalueDecomposition ed = new EigenvalueDecomposition(matrix2d);
		
		//minimal == smallest eigenValue , column == column with smallest eigenValue 
		minimal = ed.getD().get(i, i); 
		for (i = 1; i < data.length; i++) {
			temp = ed.getD().get(i, i);
			if (temp < minimal) {
				minimal = temp;
				column[0] = i;
			}
		}
		// 通过最小特征值 的 所属列， 找出 对应的 最小特征向量
		array = ed.getV().getMatrix(0, data.length - 1, column).getColumnPackedCopy();
		//对特征向量进行整理
		temp = array[0]; 
		for (int j = 0; j < data.length; j++)    
			array[j] /= temp;
		 
		return array; 
	}
}
 
