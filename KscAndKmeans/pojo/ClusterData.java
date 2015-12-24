package com.research.pojo;

public class ClusterData {
	
	public static String directory;
	public static double[][] items; // 暂存数据的二维数组 
	public static String[] itemTag; // 暂存数据行标签的一维数组
	public static double[][] centroid; //暂存数据的聚类质心
	public static int[] clusterMemberNum; // 每个聚类中的成员个数
	public static int rowno; // 数据行数
	public static int dimension; // 数据维度
	
	/**
	 * 构造器
	 * @param items xlsx数据映射后的二维数组
	 * @param itemTag 数据行的所属类别 or 标签
	 * @param centroid 暂存数据执行
	 * @param clusterMemberNum 各个聚类中的成员个数
	 * @param rowno 数据行数
	 * @param dimension 每行数据维度
	 */
	public ClusterData(double[][] items, String[] itemTag, double[][] centroid, int[] clusterMemberNum, int rowno, int dimension, String directory)
	{
		this.items = items;
		this.itemTag = itemTag;
		this.centroid = centroid; 
		this.rowno = rowno;
		this.dimension = dimension;
		this.clusterMemberNum = clusterMemberNum;
		this.directory = directory;
	}

	public static double[][] getItems()
	{
		return items;
	}

	public static String[] getItemTag()
	{
		return itemTag;
	}
}
