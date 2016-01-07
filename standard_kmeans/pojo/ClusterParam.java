package com.research.pojo;

/**
 * 用于存储kmeans算法的相关条件设置
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class ClusterParam {
	
	public static int clusterNum; //聚类个数
	public static int clusterExeNum; // 聚类算法执行次数
	
	/**
	 * KmeansAlg 构造器
	 * @param cluster
	 * @param clusterExeNum
	 */
	public ClusterParam(int cluster, int clusterExeNum)
	{
		this.clusterNum = cluster;
		this.clusterExeNum = clusterExeNum;
	}
}
