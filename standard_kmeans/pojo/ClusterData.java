package com.research.pojo;

public class ClusterData {
	
	public static String directory;  // the path input and output files corresponding to
	public static double[][] items; // 暂存数据的二维数组 
	public static String[] itemTag; // 暂存数据行标签的一维数组
	public static double[][] centroid; //暂存数据的聚类质心
	public static int[] clusterMemberNum; // 每个聚类中的成员个数
	public static int rowno; // 数据行数
	public static int dimension; // 数据维度
	public static int[] rownoWithClusterid; // item 行号 对应 的 clusterid 标识
	
	/**
	 * 构造器
	 * @param items xlsx数据映射后的二维数组
	 * @param itemTag 数据行的所属类别 or 标签
	 * @param centroid 暂存数据执行
	 * @param clusterMemberNum 各个聚类中的成员个数
	 * @param rowno 数据行数
	 * @param dimension 每行数据维度
	 */
	public ClusterData(double[][] items, String[] itemTag, double[][] centroid, int[] clusterMemberNum, int rowno, int dimension, String directory, int[] rwc)
	{
		this.items = items;
		this.itemTag = itemTag;
		this.centroid = centroid; 
		this.rowno = rowno;
		this.dimension = dimension;
		this.clusterMemberNum = clusterMemberNum;
		this.directory = directory;
		ClusterData.rownoWithClusterid = rwc;
	}
	
	/**
	 * constructor of ClusterData
	 * @param dir the directory accessing the input and output files
	 * @param rowno num of items
	 * @param clusterNum num of given cluster
	 * @param dimension a property of given time series
	 */
	public ClusterData(String dir, int rowno, int clusterNum, int dimension)
	{
		this.directory = dir;
		this.items = new double[rowno][dimension]; 
		this.itemTag = new String[rowno];
		this.centroid = new double[clusterNum][dimension];
		this.clusterMemberNum = new int[clusterNum];
		this.rowno = rowno;
		this.dimension = dimension;
		this.rownoWithClusterid = new int[rowno];
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
