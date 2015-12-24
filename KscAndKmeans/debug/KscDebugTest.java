package com.research.debug;

import com.research.Alg.KscAlg;
import com.research.evaluation.TimeSeriesSilhouette;
import com.research.io.DataRead;
import com.research.io.DataWrite;
import com.research.pojo.ClusterParam;
import com.research.pojo.TimeSeries;

public class KscDebugTest 
{
	private static KscAlg alg;
	
	public static void main(String[] args) 
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
		timeSeries = new TimeSeries(items, itemTag, centroid, clusterMemberNum, rowno, dimension, directory);
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
		
		tss.distanceOfSampleToSample(xVector, yVector);
	}
}
 











