package com.search.alltest;

import java.io.IOException;

import com.research.Alg.KmeansAlg;
import com.research.io.DataRead;
import com.research.io.DataWrite;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;
import com.research.pojo.TimeSeries;

/**
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class TimesSeriesKmeansTest 
{
	public static void main(String[] args) throws IOException 
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
		KmeansAlg kmeansAlg; //kmeans 聚类算法对象
		ClusterParam kp; //kmeans聚类算法的 参数包装类	
		
		cluster = 6;
		dimension = 128;
		rowno = 1000;
		clusterExeNum = 20;
		kp = new ClusterParam(cluster, clusterExeNum);
		items = new double[rowno][dimension + 1];// the last field holds the cluster tag
		itemTag = new String[rowno];
		centroid = new double[cluster][dimension];
		clusterMemberNum = new int[cluster];		
		//directory = "E:/bench-cluster/temp-resource/QiaoGuide/originalTimeSeries.xlsx";
		directory = "E://bench-cluster//temp-resource//QiaoGuide//151231_machine_learning//";
		dataRead = new DataRead(directory + "originalTimeSeries.xlsx");
		timeSeries = new TimeSeries(items, itemTag, centroid, clusterMemberNum, rowno, dimension, directory);
		kmeansAlg = new KmeansAlg();
		
		dataRead.readDataToArray(timeSeries); // reading data from xlsx to array over
		kmeansAlg.clusterAlg(); //进行kmeans聚类;
		
		System.out.println("kmeans alg over");
		
		//聚类完后，我们 写入聚类结果到文件 + 写入聚类评估结果为文件
//		directory += "\\151231_machine_learning\\";
		dataWrite = new DataWrite(directory);
		dataWrite.writeCentroid(ClusterData.centroid);
		
		System.out.println("===time series kmeans test over===");
	}
}
